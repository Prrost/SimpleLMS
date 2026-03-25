import { useEffect, useState } from "react";
import { createEntity, fetchPage } from "../api.js";
import Pagination from "../components/Pagination.jsx";

const PAGE_SIZE = 10;

const initialFilters = { name: "" };

export default function ClassroomsPage() {
  const [filters, setFilters] = useState(initialFilters);
  const [name, setName] = useState("");
  const [pageData, setPageData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const load = async (page = 0, activeFilters = filters) => {
    setLoading(true);
    setError("");
    try {
      const classroomsPage = await fetchPage("/api/classroom", {
        name: activeFilters.name,
        page,
        size: PAGE_SIZE,
        sort: "id,asc"
      });
      setPageData(classroomsPage);
    } catch (err) {
      setError(err.message || "Failed to load classrooms");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load(0);
  }, []);

  const handleCreate = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError("");
    setSuccess("");

    try {
      await createEntity("/api/classroom", { name });
      setName("");
      setSuccess("Classroom created");
      await load(0);
    } catch (err) {
      setError(err.message || "Failed to create classroom");
    } finally {
      setSaving(false);
    }
  };

  const classrooms = pageData?.content ?? [];

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Classrooms</h1>
          <p className="page-subtitle">Create classrooms and filter them by name.</p>
        </div>
      </div>

      {error && <div className="notice notice-error">{error}</div>}
      {success && <div className="notice notice-success">{success}</div>}

      <div className="card form-card">
        <h2>Create Classroom</h2>
        <form className="inline-form" onSubmit={handleCreate}>
          <input value={name} onChange={(e) => setName(e.target.value)} placeholder="Classroom name" required />
          <button type="submit" className="btn" disabled={saving}>{saving ? "Creating..." : "Create classroom"}</button>
        </form>
      </div>

      <div className="card filter-card">
        <h2>Filters</h2>
        <div className="filter-grid">
          <input value={filters.name} onChange={(e) => setFilters({ name: e.target.value })} placeholder="Classroom name" />
          <button className="btn" onClick={() => load(0)}>Apply</button>
          <button
            className="btn btn-secondary"
            onClick={() => {
              const nextFilters = { ...initialFilters };
              setFilters(nextFilters);
              load(0, nextFilters);
            }}
          >
            Reset
          </button>
        </div>
      </div>

      <div className="card table-card">
        <div className="table-head">
          <h2>Classroom List</h2>
          <span>{loading ? "Loading..." : `${pageData?.totalElements ?? 0} total`}</span>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
              </tr>
            </thead>
            <tbody>
              {!loading && classrooms.length === 0 && (
                <tr>
                  <td colSpan={2} className="empty-row">No classrooms found</td>
                </tr>
              )}
              {classrooms.map((classroom) => (
                <tr key={classroom.id}>
                  <td>{classroom.id}</td>
                  <td>{classroom.name}</td>
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
