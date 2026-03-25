import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { createEntity, fetchById, fetchPage, updateEntity } from "../api.js";

const MARKS = ["PRESENT", "ABSENT", "LATE", "EXCUSED"];

export default function LessonAttendancePage() {
  const { lessonId } = useParams();
  const [lesson, setLesson] = useState(null);
  const [students, setStudents] = useState([]);
  const [attendanceByStudentId, setAttendanceByStudentId] = useState({});
  const [selectionMap, setSelectionMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    let active = true;

    const load = async () => {
      setLoading(true);
      setError("");
      setSuccess("");

      try {
        const lessonData = await fetchById("/api/lesson", lessonId);
        if (!active) return;

        setLesson(lessonData);

        if (!lessonData.studentGroup?.id) {
          setStudents([]);
          setAttendanceByStudentId({});
          setSelectionMap({});
          return;
        }

        const [groupData, attendancePage] = await Promise.all([
          fetchById("/api/student_group", lessonData.studentGroup.id),
          fetchPage("/api/attendance", {
            lessonId: Number(lessonId),
            page: 0,
            size: 500,
            sort: "id,asc"
          })
        ]);

        if (!active) return;

        const groupStudents = Array.isArray(groupData.students) ? groupData.students : [];
        const records = Array.isArray(attendancePage.content) ? attendancePage.content : [];

        const byStudent = {};
        const selected = {};
        records.forEach((item) => {
          const studentId = item.student?.id;
          if (studentId == null) return;
          byStudent[studentId] = item;
          if (item.attendanceMark) {
            selected[studentId] = item.attendanceMark;
          }
        });

        setStudents(groupStudents);
        setAttendanceByStudentId(byStudent);
        setSelectionMap(selected);
      } catch (err) {
        if (active) {
          setError(err.message || "Failed to load lesson attendance");
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    };

    load();

    return () => {
      active = false;
    };
  }, [lessonId]);

  const recap = useMemo(
    () =>
      MARKS.map((mark) => ({
        mark,
        count: Object.values(attendanceByStudentId).filter((item) => item.attendanceMark === mark).length
      })),
    [attendanceByStudentId]
  );

  const handleSave = async () => {
    setSaving(true);
    setError("");
    setSuccess("");

    try {
      for (const student of students) {
        const chosenMark = selectionMap[student.id];
        if (!chosenMark) continue;

        const existing = attendanceByStudentId[student.id];
        const payload = {
          studentId: student.id,
          lessonId: Number(lessonId),
          attendanceMark: chosenMark
        };

        const saved = existing?.id
          ? await updateEntity("/api/attendance", existing.id, payload)
          : await createEntity("/api/attendance", payload);

        if (saved?.student?.id != null) {
          setAttendanceByStudentId((prev) => ({ ...prev, [saved.student.id]: saved }));
        }
      }

      setSuccess("Attendance updated");
    } catch (err) {
      setError(err.message || "Failed to save attendance");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <section className="page">
        <div className="card">Loading attendance...</div>
      </section>
    );
  }

  if (!lesson) {
    return (
      <section className="page">
        <div className="notice notice-error">Lesson not found.</div>
      </section>
    );
  }

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <Link to="/lessons" className="table-link">Back to lessons</Link>
          <h1 className="page-title">{lesson.name || `Lesson #${lesson.id}`} Attendance</h1>
          <p className="page-subtitle">Group: {lesson.studentGroup?.name || "No group assigned"}</p>
        </div>
        <button className="btn" onClick={handleSave} disabled={saving}>
          {saving ? "Saving..." : "Save attendance"}
        </button>
      </div>

      {error && <div className="notice notice-error">{error}</div>}
      {success && <div className="notice notice-success">{success}</div>}

      {!lesson.studentGroup?.id && (
        <div className="card empty-row">Assign a student group to this lesson first.</div>
      )}

      {lesson.studentGroup?.id && (
        <div className="card table-card">
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Student</th>
                  <th>Current</th>
                  <th>Update</th>
                </tr>
              </thead>
              <tbody>
                {students.length === 0 && (
                  <tr>
                    <td colSpan={3} className="empty-row">No students in this group</td>
                  </tr>
                )}
                {students.map((student) => {
                  const existing = attendanceByStudentId[student.id];
                  return (
                    <tr key={student.id}>
                      <td>{student.lastName} {student.name}</td>
                      <td>{existing?.attendanceMark || "-"}</td>
                      <td>
                        <div className="radio-group">
                          {MARKS.map((mark) => (
                            <label key={mark} className="radio-pill">
                              <input
                                type="radio"
                                name={`student-${student.id}`}
                                checked={selectionMap[student.id] === mark}
                                onChange={() => setSelectionMap((prev) => ({ ...prev, [student.id]: mark }))}
                              />
                              {mark}
                            </label>
                          ))}
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      <div className="card recap-card">
        {recap.map((item) => (
          <div key={item.mark} className="recap-item">
            <strong>{item.count}</strong>
            <span>{item.mark}</span>
          </div>
        ))}
      </div>
    </section>
  );
}
