import { useEffect, useState } from "react";
import { createEntity, fetchPage, updateEntity } from "../api.js";
import EntityLookupSelect from "../components/EntityLookupSelect.jsx";
import Pagination from "../components/Pagination.jsx";

const PAGE_SIZE = 10;

const initialFilters = {
  name: "",
  isVirtual: ""
};

const initialForm = {
  name: "",
  isVirtual: false,
  studentIds: []
};

export default function StudentGroupsPage() {
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
      const params = {
        name: activeFilters.name,
        page,
        size: PAGE_SIZE,
        sort: "id,asc"
      };
      if (activeFilters.isVirtual !== "") {
        params.isVirtual = activeFilters.isVirtual;
      }

      const groupsPage = await fetchPage("/api/student_group", params);
      setPageData(groupsPage);
    } catch (err) {
      setError(err.message || "Failed to load groups");
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
        isVirtual: form.isVirtual,
        studentIds: form.studentIds
      };

      if (editingId) {
        await updateEntity("/api/student_group", editingId, payload);
        setSuccess("Group updated");
      } else {
        await createEntity("/api/student_group", payload);
        setSuccess("Group created");
      }

      resetForm();
      await load(0);
    } catch (err) {
      setError(err.message || "Failed to save group");
    } finally {
      setSaving(false);
    }
  };

  const startEdit = (group) => {
    setEditingId(group.id);
    setForm({
      name: group.name || "",
      isVirtual: Boolean(group.isVirtual),
      studentIds: Array.isArray(group.students) ? group.students.map((student) => student.id) : []
    });
  };

  const groups = pageData?.content ?? [];

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Student Groups</h1>
          <p className="page-subtitle">Create and edit groups. Student selection supports search and paging.</p>
        </div>
      </div>

      {error && <div className="notice notice-error">{error}</div>}
      {success && <div className="notice notice-success">{success}</div>}

      <div className="card form-card">
        <h2>{editingId ? `Edit Group #${editingId}` : "Create Student Group"}</h2>
        <form className="form-grid" onSubmit={handleSave}>
          <input value={form.name} onChange={(e) => setForm((p) => ({ ...p, name: e.target.value }))} placeholder="Group name" required />
          <label className="field">
            Virtual group
            <select value={String(form.isVirtual)} onChange={(e) => setForm((p) => ({ ...p, isVirtual: e.target.value === "true" }))}>
              <option value="false">No</option>
              <option value="true">Yes</option>
            </select>
          </label>

          <div className="field field-full">
            <EntityLookupSelect
              label="Students"
              endpoint="/api/student"
              multiple
              value={form.studentIds}
              onChange={(ids) => setForm((p) => ({ ...p, studentIds: ids }))}
              searchFields={[
                { key: "name", placeholder: "Name" },
                { key: "lastName", placeholder: "Last name" },
                { key: "email", placeholder: "Email" },
                { key: "phone", placeholder: "Phone" }
              ]}
              optionLabel={(student) => `${student.lastName || ""} ${student.name || ""} (#${student.id})`.trim()}
              optionMeta={(student) => `${student.phone || "No phone"} · ${student.email || "No email"}`}
              placeholder="Select students"
            />
          </div>

          <button type="submit" className="btn" disabled={saving}>{saving ? "Saving..." : editingId ? "Update group" : "Create group"}</button>
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
          <input value={filters.name} onChange={(e) => setFilters((p) => ({ ...p, name: e.target.value }))} placeholder="Group name" />
          <select value={filters.isVirtual} onChange={(e) => setFilters((p) => ({ ...p, isVirtual: e.target.value }))}>
            <option value="">All types</option>
            <option value="true">Virtual only</option>
            <option value="false">Physical only</option>
          </select>
          <button className="btn" onClick={() => load(0)}>Apply</button>
          <button className="btn btn-secondary" onClick={resetFilters}>Reset</button>
        </div>
      </div>

      <div className="card table-card">
        <div className="table-head">
          <h2>Group List</h2>
          <span>{loading ? "Loading..." : `${pageData?.totalElements ?? 0} total`}</span>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Virtual</th>
                <th>Students count</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {!loading && groups.length === 0 && (
                <tr>
                  <td colSpan={5} className="empty-row">No groups found</td>
                </tr>
              )}
              {groups.map((group) => (
                <tr key={group.id}>
                  <td>{group.id}</td>
                  <td>{group.name}</td>
                  <td>{group.isVirtual ? "Yes" : "No"}</td>
                  <td>{group.students?.length ?? 0}</td>
                  <td>
                    <button type="button" className="btn btn-secondary" onClick={() => startEdit(group)}>
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
