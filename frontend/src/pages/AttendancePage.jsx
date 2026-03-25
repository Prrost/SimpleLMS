import { useEffect, useState } from "react";
import { createEntity, fetchPage, updateEntity } from "../api.js";
import EntityLookupSelect from "../components/EntityLookupSelect.jsx";
import Pagination from "../components/Pagination.jsx";
import { formatDateTime, toNumber } from "../utils.js";

const PAGE_SIZE = 10;
const MARKS = ["PRESENT", "ABSENT", "LATE", "EXCUSED"];

const initialFilters = {
  studentId: null,
  lessonId: null,
  attendanceMark: ""
};

const initialForm = {
  studentId: null,
  lessonId: null,
  attendanceMark: "PRESENT"
};

export default function AttendancePage() {
  const [filters, setFilters] = useState(initialFilters);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [pageData, setPageData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const load = async (page = 0, activeFilters = filters) => {
    setLoading(true);
    setError("");

    try {
      const attendancePage = await fetchPage("/api/attendance", {
        studentId: toNumber(activeFilters.studentId),
        lessonId: toNumber(activeFilters.lessonId),
        attendanceMark: activeFilters.attendanceMark,
        page,
        size: PAGE_SIZE,
        sort: "id,asc"
      });

      setPageData(attendancePage);
    } catch (err) {
      setError(err.message || "Failed to load attendance");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load(0);
  }, []);

  const resetFilters = () => {
    const nextFilters = { ...initialFilters };
    setFilters(nextFilters);
    load(0, nextFilters);
  };

  const resetForm = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  const handleSave = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError("");
    setSuccess("");

    try {
      const payload = {
        studentId: toNumber(form.studentId),
        lessonId: toNumber(form.lessonId),
        attendanceMark: form.attendanceMark
      };

      if (editingId) {
        await updateEntity("/api/attendance", editingId, payload);
        setSuccess("Attendance updated");
      } else {
        await createEntity("/api/attendance", payload);
        setSuccess("Attendance created");
      }

      resetForm();
      await load(0);
    } catch (err) {
      setError(err.message || "Failed to save attendance");
    } finally {
      setSaving(false);
    }
  };

  const startEdit = (row) => {
    setEditingId(row.id);
    setForm({
      studentId: row.student?.id ?? null,
      lessonId: row.lesson?.id ?? null,
      attendanceMark: row.attendanceMark || "PRESENT"
    });
  };

  const rows = pageData?.content ?? [];

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Attendance</h1>
          <p className="page-subtitle">Lesson and student dropdowns support search and paging. Records are editable.</p>
        </div>
      </div>

      {error && <div className="notice notice-error">{error}</div>}
      {success && <div className="notice notice-success">{success}</div>}

      <div className="card form-card">
        <h2>{editingId ? `Edit Attendance #${editingId}` : "Create Attendance"}</h2>
        <form className="form-grid" onSubmit={handleSave}>
          <div className="field">
            <EntityLookupSelect
              label="Student"
              endpoint="/api/student"
              value={form.studentId}
              onChange={(id) => setForm((p) => ({ ...p, studentId: id }))}
              searchFields={[
                { key: "name", placeholder: "Name" },
                { key: "lastName", placeholder: "Last name" },
                { key: "email", placeholder: "Email" },
                { key: "phone", placeholder: "Phone" }
              ]}
              optionLabel={(student) => `${student.lastName || ""} ${student.name || ""} (#${student.id})`.trim()}
              optionMeta={(student) => `${student.phone || "No phone"} · ${student.email || "No email"}`}
              placeholder="Select student"
            />
          </div>

          <div className="field">
            <EntityLookupSelect
              label="Lesson"
              endpoint="/api/lesson"
              value={form.lessonId}
              onChange={(id) => setForm((p) => ({ ...p, lessonId: id }))}
              buildSearchParams={(query) => ({ name: query })}
              optionLabel={(lesson) => `${lesson.name || `Lesson #${lesson.id}`}`}
              placeholder="Select lesson"
            />
          </div>

          <label className="field">
            Mark
            <select value={form.attendanceMark} onChange={(e) => setForm((p) => ({ ...p, attendanceMark: e.target.value }))}>
              {MARKS.map((mark) => (
                <option key={mark} value={mark}>{mark}</option>
              ))}
            </select>
          </label>

          <button type="submit" className="btn" disabled={saving || !form.studentId || !form.lessonId}>
            {saving ? "Saving..." : editingId ? "Update attendance" : "Create attendance"}
          </button>
          {editingId && (
            <button type="button" className="btn btn-secondary" onClick={resetForm}>
              Cancel edit
            </button>
          )}
        </form>
      </div>

      <div className="card filter-card">
        <h2>Filters</h2>
        <div className="filter-grid">
          <div className="field">
            <EntityLookupSelect
              label="Student"
              endpoint="/api/student"
              value={filters.studentId}
              onChange={(id) => setFilters((p) => ({ ...p, studentId: id }))}
              searchFields={[
                { key: "name", placeholder: "Name" },
                { key: "lastName", placeholder: "Last name" },
                { key: "email", placeholder: "Email" },
                { key: "phone", placeholder: "Phone" }
              ]}
              optionLabel={(student) => `${student.lastName || ""} ${student.name || ""} (#${student.id})`.trim()}
              optionMeta={(student) => `${student.phone || "No phone"} · ${student.email || "No email"}`}
              placeholder="All students"
            />
          </div>

          <div className="field">
            <EntityLookupSelect
              label="Lesson"
              endpoint="/api/lesson"
              value={filters.lessonId}
              onChange={(id) => setFilters((p) => ({ ...p, lessonId: id }))}
              buildSearchParams={(query) => ({ name: query })}
              optionLabel={(lesson) => `${lesson.name || `Lesson #${lesson.id}`}`}
              placeholder="All lessons"
            />
          </div>

          <select value={filters.attendanceMark} onChange={(e) => setFilters((p) => ({ ...p, attendanceMark: e.target.value }))}>
            <option value="">All marks</option>
            {MARKS.map((mark) => (
              <option key={mark} value={mark}>{mark}</option>
            ))}
          </select>

          <button className="btn" onClick={() => load(0)}>Apply</button>
          <button className="btn btn-secondary" onClick={resetFilters}>Reset</button>
        </div>
      </div>

      <div className="card table-card">
        <div className="table-head">
          <h2>Attendance List</h2>
          <span>{loading ? "Loading..." : `${pageData?.totalElements ?? 0} total`}</span>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Student</th>
                <th>Lesson</th>
                <th>Mark</th>
                <th>Updated at</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {!loading && rows.length === 0 && (
                <tr>
                  <td colSpan={6} className="empty-row">No attendance found</td>
                </tr>
              )}
              {rows.map((row) => (
                <tr key={row.id}>
                  <td>{row.id}</td>
                  <td>{row.student?.lastName} {row.student?.name}</td>
                  <td>{row.lesson?.name || "-"}</td>
                  <td>{row.attendanceMark || "-"}</td>
                  <td>{formatDateTime(row.updatedAt)}</td>
                  <td>
                    <button type="button" className="btn btn-secondary" onClick={() => startEdit(row)}>
                      Edit
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <Pagination pageData={pageData} onPageChange={(page) => load(page)} />
      </div>
    </section>
  );
}
