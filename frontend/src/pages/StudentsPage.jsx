import { useEffect, useState } from "react";
import { createEntity, fetchPage, updateEntity } from "../api.js";
import EntityLookupSelect from "../components/EntityLookupSelect.jsx";
import Pagination from "../components/Pagination.jsx";

const PAGE_SIZE = 10;

const initialFilters = {
  name: "",
  lastName: "",
  email: "",
  phone: ""
};

const initialForm = {
  name: "",
  lastName: "",
  email: "",
  phone: "",
  studentGroupsIds: []
};

export default function StudentsPage() {
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
      const studentsPage = await fetchPage("/api/student", {
        ...activeFilters,
        page,
        size: PAGE_SIZE,
        sort: "id,asc"
      });
      setPageData(studentsPage);
    } catch (err) {
      setError(err.message || "Failed to load students");
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
        lastName: form.lastName,
        email: form.email,
        phone: form.phone,
        studentGroupsIds: form.studentGroupsIds
      };

      if (editingId) {
        await updateEntity("/api/student", editingId, payload);
        setSuccess("Student updated");
      } else {
        await createEntity("/api/student", payload);
        setSuccess("Student created");
      }

      resetForm();
      await load(0);
    } catch (err) {
      setError(err.message || "Failed to save student");
    } finally {
      setSaving(false);
    }
  };

  const startEdit = (student) => {
    setEditingId(student.id);
    setForm({
      name: student.name || "",
      lastName: student.lastName || "",
      email: student.email || "",
      phone: student.phone || "",
      studentGroupsIds: Array.isArray(student.studentGroups) ? student.studentGroups.map((group) => group.id) : []
    });
  };

  const students = pageData?.content ?? [];

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Students</h1>
          <p className="page-subtitle">Create and edit students. Group selection supports search and paging.</p>
        </div>
      </div>

      {error && <div className="notice notice-error">{error}</div>}
      {success && <div className="notice notice-success">{success}</div>}

      <div className="card form-card">
        <h2>{editingId ? `Edit Student #${editingId}` : "Create Student"}</h2>
        <form className="form-grid" onSubmit={handleSave}>
          <input value={form.name} onChange={(e) => setForm((p) => ({ ...p, name: e.target.value }))} placeholder="Name" required />
          <input value={form.lastName} onChange={(e) => setForm((p) => ({ ...p, lastName: e.target.value }))} placeholder="Last name" required />
          <input type="email" value={form.email} onChange={(e) => setForm((p) => ({ ...p, email: e.target.value }))} placeholder="Email" required />
          <input value={form.phone} onChange={(e) => setForm((p) => ({ ...p, phone: e.target.value }))} placeholder="Phone" required />

          <div className="field field-full">
            <EntityLookupSelect
              label="Student Groups"
              endpoint="/api/student_group"
              multiple
              value={form.studentGroupsIds}
              onChange={(ids) => setForm((p) => ({ ...p, studentGroupsIds: ids }))}
              buildSearchParams={(query) => ({ name: query })}
              optionLabel={(group) => `${group.name || `Group #${group.id}`}${group.isVirtual ? " (virtual)" : ""}`}
              placeholder="Select groups"
            />
          </div>

          <button type="submit" className="btn" disabled={saving}>{saving ? "Saving..." : editingId ? "Update student" : "Create student"}</button>
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
          <input name="name" value={filters.name} onChange={(e) => setFilters((prev) => ({ ...prev, name: e.target.value }))} placeholder="Name" />
          <input name="lastName" value={filters.lastName} onChange={(e) => setFilters((prev) => ({ ...prev, lastName: e.target.value }))} placeholder="Last name" />
          <input name="email" value={filters.email} onChange={(e) => setFilters((prev) => ({ ...prev, email: e.target.value }))} placeholder="Email" />
          <input name="phone" value={filters.phone} onChange={(e) => setFilters((prev) => ({ ...prev, phone: e.target.value }))} placeholder="Phone" />
          <button className="btn" onClick={() => load(0)}>Apply</button>
          <button className="btn btn-secondary" onClick={resetFilters}>Reset</button>
        </div>
      </div>

      <div className="card table-card">
        <div className="table-head">
          <h2>Student List</h2>
          <span>{loading ? "Loading..." : `${pageData?.totalElements ?? 0} total`}</span>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Last name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Groups</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {!loading && students.length === 0 && (
                <tr>
                  <td colSpan={7} className="empty-row">No students found</td>
                </tr>
              )}
              {students.map((student) => (
                <tr key={student.id}>
                  <td>{student.id}</td>
                  <td>{student.name}</td>
                  <td>{student.lastName}</td>
                  <td>{student.email}</td>
                  <td>{student.phone}</td>
                  <td>{(student.studentGroups || []).map((group) => group.name).join(", ") || "-"}</td>
                  <td>
                    <button type="button" className="btn btn-secondary" onClick={() => startEdit(student)}>
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
