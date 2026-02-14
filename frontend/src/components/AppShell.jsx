import { Link, useLocation } from "react-router-dom";

export default function AppShell({ children }) {
  const location = useLocation();
  const inLesson = location.pathname.startsWith("/lessons/");

  return (
    <div className="app">
      <header className="app-header">
        <div className="brand">
          <div className="brand-mark">SL</div>
          <div>
            <div className="brand-title">SimpleLMS</div>
            <div className="brand-subtitle">Attendance Console</div>
          </div>
        </div>
        <nav className="nav">
          <Link className={`nav-link ${!inLesson ? "active" : ""}`} to="/lessons">
            Lessons
          </Link>
          {inLesson && (
            <Link className="nav-link active" to={location.pathname}>
              Attendance
            </Link>
          )}
        </nav>
      </header>
      <main className="app-main">{children}</main>
    </div>
  );
}
