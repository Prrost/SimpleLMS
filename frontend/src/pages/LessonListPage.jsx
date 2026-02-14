import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import StatusPill from "../components/StatusPill.jsx";

const API_BASE = import.meta.env.VITE_API_BASE || "";

function formatDate(value) {
  if (!value) return "—";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString();
}

export default function LessonListPage() {
  const [lessons, setLessons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;
    setLoading(true);
    fetch(`${API_BASE}/api/lesson/all`)
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to load lessons");
        }
        return res.json();
      })
      .then((data) => {
        if (active) {
          setLessons(Array.isArray(data) ? data : []);
          setError("");
        }
      })
      .catch((err) => {
        if (active) {
          setError(err.message || "Error loading lessons");
        }
      })
      .finally(() => {
        if (active) setLoading(false);
      });
    return () => {
      active = false;
    };
  }, []);

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Lessons</h1>
          <p className="page-subtitle">
            Choose a lesson to manage attendance for its student group.
          </p>
        </div>
        <div className="page-meta">
          <StatusPill text={loading ? "Loading" : `${lessons.length} lessons`} tone="info" />
        </div>
      </div>

      {error && <div className="notice error">{error}</div>}

      <div className="lesson-grid">
        {loading && (
          <div className="card skeleton">
            <div className="skeleton-line" />
            <div className="skeleton-line short" />
            <div className="skeleton-line" />
          </div>
        )}

        {!loading && lessons.length === 0 && (
          <div className="card empty">
            <h3>No lessons yet</h3>
            <p>Create lessons in the backend to see them here.</p>
          </div>
        )}

        {lessons.map((lesson) => (
          <Link key={lesson.id} className="card lesson-card" to={`/lessons/${lesson.id}`}>
            <div className="card-head">
              <h3>{lesson.name || `Lesson #${lesson.id}`}</h3>
              <StatusPill
                text={lesson.studentGroup?.name ? lesson.studentGroup.name : "No group"}
                tone={lesson.studentGroup?.name ? "success" : "warning"}
              />
            </div>
            <div className="card-body">
              <div className="meta-row">
                <span>Starts</span>
                <strong>{formatDate(lesson.startsAt)}</strong>
              </div>
              <div className="meta-row">
                <span>Ends</span>
                <strong>{formatDate(lesson.endsAt)}</strong>
              </div>
              <div className="meta-row">
                <span>Classroom</span>
                <strong>{lesson.classroom?.name || "—"}</strong>
              </div>
            </div>
            <div className="card-footer">Open attendance →</div>
          </Link>
        ))}
      </div>
    </section>
  );
}
