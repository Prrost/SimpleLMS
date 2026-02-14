import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import StatusPill from "../components/StatusPill.jsx";

const API_BASE = import.meta.env.VITE_API_BASE || "";
const MARKS = ["PRESENT", "ABSENT", "LATE", "EXCUSED"];

function formatDate(value) {
  if (!value) return "—";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString();
}

export default function LessonAttendancePage() {
  const { lessonId } = useParams();
  const [lesson, setLesson] = useState(null);
  const [group, setGroup] = useState(null);
  const [students, setStudents] = useState([]);
  const [attendanceMap, setAttendanceMap] = useState({});
  const [selectionMap, setSelectionMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [saveMessage, setSaveMessage] = useState("");

  const attendanceList = useMemo(() => Object.values(attendanceMap), [attendanceMap]);

  useEffect(() => {
    let active = true;
    setLoading(true);
    setError("");
    setSaveMessage("");

    async function load() {
      try {
        const lessonRes = await fetch(`${API_BASE}/api/lesson/${lessonId}`);
        if (!lessonRes.ok) throw new Error("Failed to load lesson");
        const lessonData = await lessonRes.json();
        if (!active) return;
        setLesson(lessonData);

        const groupId = lessonData.studentGroup?.id;
        if (!groupId) {
          setGroup(null);
          setStudents([]);
          setAttendanceMap({});
          setSelectionMap({});
          return;
        }

        const [groupRes, attendanceRes] = await Promise.all([
          fetch(`${API_BASE}/api/student_group/${groupId}`),
          fetch(`${API_BASE}/api/attendance/all`)
        ]);

        if (!groupRes.ok) throw new Error("Failed to load student group");
        if (!attendanceRes.ok) throw new Error("Failed to load attendance list");

        const groupData = await groupRes.json();
        const attendanceData = await attendanceRes.json();
        if (!active) return;

        const groupStudents = Array.isArray(groupData.students) ? groupData.students : [];
        const filteredAttendance = Array.isArray(attendanceData)
          ? attendanceData.filter((item) => item.lesson?.id === Number(lessonId))
          : [];

        const byStudent = {};
        const selections = {};
        filteredAttendance.forEach((item) => {
          if (item.student?.id != null) {
            byStudent[item.student.id] = item;
            if (item.attendanceMark) {
              selections[item.student.id] = item.attendanceMark;
            }
          }
        });

        setGroup(groupData);
        setStudents(groupStudents);
        setAttendanceMap(byStudent);
        setSelectionMap(selections);
      } catch (err) {
        if (active) {
          setError(err.message || "Error loading attendance");
        }
      } finally {
        if (active) setLoading(false);
      }
    }

    load();

    return () => {
      active = false;
    };
  }, [lessonId]);

  const handleSelect = (studentId, mark) => {
    setSelectionMap((prev) => ({ ...prev, [studentId]: mark }));
  };

  const handleSave = async () => {
    setSaving(true);
    setSaveMessage("");
    setError("");

    const updates = students
      .filter((student) => selectionMap[student.id])
      .map((student) => {
        const selectedMark = selectionMap[student.id];
        const existing = attendanceMap[student.id];
        const payload = {
          studentId: student.id,
          lessonId: Number(lessonId),
          attendanceMark: selectedMark
        };
        if (existing?.id) {
          return {
            id: existing.id,
            method: "PUT",
            url: `${API_BASE}/api/attendance/${existing.id}`,
            payload
          };
        }
        return {
          id: null,
          method: "POST",
          url: `${API_BASE}/api/attendance`,
          payload
        };
      });

    try {
      for (const item of updates) {
        const res = await fetch(item.url, {
          method: item.method,
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(item.payload)
        });
        if (!res.ok) {
          throw new Error("Failed to save attendance");
        }
        const saved = await res.json();
        if (saved?.student?.id != null) {
          setAttendanceMap((prev) => ({ ...prev, [saved.student.id]: saved }));
        }
      }
      setSaveMessage("Attendance saved successfully");
    } catch (err) {
      setError(err.message || "Error saving attendance");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <section className="page">
        <div className="card skeleton">
          <div className="skeleton-line" />
          <div className="skeleton-line" />
        </div>
      </section>
    );
  }

  if (!lesson) {
    return (
      <section className="page">
        <div className="notice error">Lesson not found.</div>
        <Link to="/lessons" className="btn secondary">Back to lessons</Link>
      </section>
    );
  }

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <Link to="/lessons" className="back-link">← Back to lessons</Link>
          <h1 className="page-title">{lesson.name || `Lesson #${lesson.id}`}</h1>
          <p className="page-subtitle">
            Group: {lesson.studentGroup?.name || "No group assigned"} · Classroom: {lesson.classroom?.name || "—"}
          </p>
        </div>
        <div className="page-meta">
          <StatusPill text={`Starts ${formatDate(lesson.startsAt)}`} tone="info" />
          <StatusPill text={`Ends ${formatDate(lesson.endsAt)}`} tone="neutral" />
        </div>
      </div>

      {error && <div className="notice error">{error}</div>}
      {saveMessage && <div className="notice success">{saveMessage}</div>}

      {!lesson.studentGroup?.id && (
        <div className="card empty">
          <h3>No group assigned</h3>
          <p>Assign a student group to this lesson to manage attendance.</p>
        </div>
      )}

      {lesson.studentGroup?.id && (
        <div className="attendance-panel">
          <div className="panel-header">
            <div>
              <h2>Students</h2>
              <p>{group?.students?.length || students.length} students in this group</p>
            </div>
            <button className="btn" onClick={handleSave} disabled={saving}>
              {saving ? "Saving..." : "Save attendance"}
            </button>
          </div>

          <div className="attendance-table">
            <div className="attendance-row header">
              <div>Student</div>
              <div>Current</div>
              <div>Update</div>
            </div>

            {students.map((student) => {
              const existing = attendanceMap[student.id];
              const currentMark = existing?.attendanceMark || "—";
              return (
                <div key={student.id} className="attendance-row">
                  <div className="student-info">
                    <strong>{student.lastName || ""} {student.name || "Student"}</strong>
                    <span className="muted">{student.email || "No email"}</span>
                  </div>
                  <div>
                    <StatusPill
                      text={currentMark}
                      tone={currentMark === "PRESENT" ? "success" : currentMark === "ABSENT" ? "danger" : "neutral"}
                    />
                  </div>
                  <div className="radio-group">
                    {MARKS.map((mark) => (
                      <label key={mark} className={`radio-pill ${selectionMap[student.id] === mark ? "active" : ""}`}>
                        <input
                          type="radio"
                          name={`attendance-${student.id}`}
                          value={mark}
                          checked={selectionMap[student.id] === mark}
                          onChange={() => handleSelect(student.id, mark)}
                        />
                        {mark}
                      </label>
                    ))}
                  </div>
                </div>
              );
            })}

            {students.length === 0 && (
              <div className="card empty">
                <h3>No students in this group</h3>
                <p>Add students to the group to manage attendance.</p>
              </div>
            )}
          </div>
        </div>
      )}

      <div className="card info-card">
        <div>
          <h3>Attendance recap</h3>
          <p>Current saved records for this lesson.</p>
        </div>
        <div className="recap-grid">
          {MARKS.map((mark) => {
            const count = attendanceList.filter((item) => item.attendanceMark === mark).length;
            return (
              <div key={mark} className="recap-item">
                <div className="recap-count">{count}</div>
                <div className="recap-label">{mark}</div>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
