import { Routes, Route, Navigate } from "react-router-dom";
import LessonListPage from "./pages/LessonListPage.jsx";
import LessonAttendancePage from "./pages/LessonAttendancePage.jsx";
import AppShell from "./components/AppShell.jsx";

export default function App() {
  return (
    <AppShell>
      <Routes>
        <Route path="/" element={<Navigate to="/lessons" replace />} />
        <Route path="/lessons" element={<LessonListPage />} />
        <Route path="/lessons/:lessonId" element={<LessonAttendancePage />} />
        <Route path="*" element={<Navigate to="/lessons" replace />} />
      </Routes>
    </AppShell>
  );
}
