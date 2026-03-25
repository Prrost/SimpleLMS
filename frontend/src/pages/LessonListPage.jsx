import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { createEntity, fetchPage, updateEntity } from "../api.js";
import EntityLookupSelect from "../components/EntityLookupSelect.jsx";
import Pagination from "../components/Pagination.jsx";
import { formatDateTime, toNumber } from "../utils.js";

const PAGE_SIZE = 10;

const initialFilters = {
  name: "",
  classroomId: null,
  studentGroupId: null
};

const initialForm = {
  name: "",
  classroomId: null,
  studentGroupId: null,
  startsAt: "",
  endsAt: "",
  expiresAt: ""
};

function toInputDateTime(value) {
  if (!value) return "";
  return String(value).slice(0, 16);
}

export default function LessonListPage() {
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
      const lessonsPage = await fetchPage("/api/lesson", {
        name: activeFilters.name,
        classroomId: toNumber(activeFilters.classroomId),
        studentGroupId: toNumber(activeFilters.studentGroupId),
        page,
        size: PAGE_SIZE,
        sort: "id,asc"
      });
      setPageData(lessonsPage);
    } catch (err) {
      setError(err.message || "Failed to load lessons");
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
        name: form.name,
        classroomId: toNumber(form.classroomId),
        studentGroupId: toNumber(form.studentGroupId),
        startsAt: form.startsAt || null,
        endsAt: form.endsAt || null,
        expiresAt: form.expiresAt || null
      };

      if (editingId) {
        await updateEntity("/api/lesson", editingId, payload);
        setSuccess("Lesson updated");
      } else {
        await createEntity("/api/lesson", payload);
        setSuccess("Lesson created");
      }

      resetForm();
      await load(0);
    } catch (err) {
      setError(err.message || "Failed to save lesson");
    } finally {
      setSaving(false);
    }
  };

  const startEdit = (lesson) => {
    setEditingId(lesson.id);
    setForm({
      name: lesson.name || "",
      classroomId: lesson.classroom?.id ?? null,
      studentGroupId: lesson.studentGroup?.id ?? null,
      startsAt: toInputDateTime(lesson.startsAt),
      endsAt: toInputDateTime(lesson.endsAt),
      expiresAt: toInputDateTime(lesson.expiresAt)
    });
  };

  const lessons = pageData?.content ?? [];

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Lessons</h1>
          <p className="page-subtitle">Dropdowns for classroom/group support search and paging. Lessons are editable.</p>
        </div>
      </div>

      {error && <div className="notice notice-error">{error}</div>}
      {success && <div className="notice notice-success">{success}</div>}

      <div className="card form-card">
        <h2>{editingId ? `Edit Lesson #${editingId}` : "Create Lesson"}</h2>
        <form className="form-grid" onSubmit={handleSave}>
          <input value={form.name} onChange={(e) => setForm((p) => ({ ...p, name: e.target.value }))} placeholder="Lesson name" required />

          <div className="field">
            <EntityLookupSelect
              label="Classroom"
              endpoint="/api/classroom"
              value={form.classroomId}
              onChange={(id) => setForm((p) => ({ ...p, classroomId: id }))}
              buildSearchParams={(query) => ({ name: query })}
              optionLabel={(classroom) => classroom.name || `Classroom #${classroom.id}`}
              placeholder="No classroom"
            />
          </div>

          <div className="field">
            <EntityLookupSelect
              label="Student group"
              endpoint="/api/student_group"
              value={form.studentGroupId}
              onChange={(id) => setForm((p) => ({ ...p, studentGroupId: id }))}
              buildSearchParams={(query) => ({ name: query })}
              optionLabel={(group) => group.name || `Group #${group.id}`}
              placeholder="No group"
            />
          </div>

          <label className="field">
            Starts at
            <input type="datetime-local" value={form.startsAt} onChange={(e) => setForm((p) => ({ ...p, startsAt: e.target.value }))} />
          </label>
          <label className="field">
            Ends at
            <input type="datetime-local" value={form.endsAt} onChange={(e) => setForm((p) => ({ ...p, endsAt: e.target.value }))} />
          </label>
          <label className="field">
            Expires at
            <input type="datetime-local" value={form.expiresAt} onChange={(e) => setForm((p) => ({ ...p, expiresAt: e.target.value }))} />
          </label>

          <button type="submit" className="btn" disabled={saving}>{saving ? "Saving..." : editingId ? "Update lesson" : "Create lesson"}</button>
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
          <input value={filters.name} onChange={(e) => setFilters((p) => ({ ...p, name: e.target.value }))} placeholder="Lesson name" />

          <div className="field">
            <EntityLookupSelect
              label="Classroom"
              endpoint="/api/classroom"
              value={filters.classroomId}
              onChange={(id) => setFilters((p) => ({ ...p, classroomId: id }))}
              buildSearchParams={(query) => ({ name: query })}
              optionLabel={(classroom) => classroom.name || `Classroom #${classroom.id}`}
              placeholder="All classrooms"
            />
          </div>

          <div className="field">
            <EntityLookupSelect
              label="Student group"
              endpoint="/api/student_group"
              value={filters.studentGroupId}
              onChange={(id) => setFilters((p) => ({ ...p, studentGroupId: id }))}
              buildSearchParams={(query) => ({ name: query })}
              optionLabel={(group) => group.name || `Group #${group.id}`}
              placeholder="All groups"
            />
          </div>

          <button className="btn" onClick={() => load(0)}>Apply</button>
          <button className="btn btn-secondary" onClick={resetFilters}>Reset</button>
        </div>
      </div>

      <div className="card table-card">
        <div className="table-head">
          <h2>Lesson List</h2>
          <span>{loading ? "Loading..." : `${pageData?.totalElements ?? 0} total`}</span>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Classroom</th>
                <th>Group</th>
                <th>Starts</th>
                <th>Ends</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {!loading && lessons.length === 0 && (
                <tr>
                  <td colSpan={7} className="empty-row">No lessons found</td>
                </tr>
              )}
              {lessons.map((lesson) => (
                <tr key={lesson.id}>
                  <td>{lesson.id}</td>
                  <td>{lesson.name}</td>
                  <td>{lesson.classroom?.name || "-"}</td>
                  <td>{lesson.studentGroup?.name || "-"}</td>
                  <td>{formatDateTime(lesson.startsAt)}</td>
                  <td>{formatDateTime(lesson.endsAt)}</td>
                  <td>
                    <div className="actions-row">
                      <button type="button" className="btn btn-secondary" onClick={() => startEdit(lesson)}>
                        Edit
                      </button>
                      <Link className="table-link" to={`/lessons/${lesson.id}/attendance`}>
                        Attendance
                      </Link>
                    </div>
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
