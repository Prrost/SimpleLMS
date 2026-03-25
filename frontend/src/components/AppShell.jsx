import { Link, useLocation } from "react-router-dom";

const links = [
  { to: "/students", label: "Students" },
  { to: "/groups", label: "Groups" },
  { to: "/classrooms", label: "Classrooms" },
  { to: "/lessons", label: "Lessons" },
  { to: "/attendance", label: "Attendance" }
];

export default function AppShell({ children }) {
  const location = useLocation();

  return (
    <div className="app">
      <header className="app-header">
        <div>
          <div className="brand-title">SimpleLMS</div>
          <div className="brand-subtitle">Frontend Admin Console</div>
        </div>
        <nav className="nav">
          {links.map((link) => {
            const active =
              location.pathname === link.to ||
              (link.to === "/lessons" && location.pathname.startsWith("/lessons/"));

            return (
              <Link key={link.to} className={`nav-link ${active ? "active" : ""}`} to={link.to}>
                {link.label}
              </Link>
            );
          })}
        </nav>
      </header>
      <main className="app-main">{children}</main>
    </div>
  );
}
