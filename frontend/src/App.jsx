import { Navigate, Route, Routes } from "react-router-dom";
import AppShell from "./components/AppShell.jsx";
import AttendancePage from "./pages/AttendancePage.jsx";
import ClassroomsPage from "./pages/ClassroomsPage.jsx";
import LessonAttendancePage from "./pages/LessonAttendancePage.jsx";
import LessonListPage from "./pages/LessonListPage.jsx";
import StudentGroupsPage from "./pages/StudentGroupsPage.jsx";
import StudentsPage from "./pages/StudentsPage.jsx";

export default function App() {
  return (
    <AppShell>
      <Routes>
        <Route path="/" element={<Navigate to="/students" replace />} />
        <Route path="/students" element={<StudentsPage />} />
        <Route path="/groups" element={<StudentGroupsPage />} />
        <Route path="/classrooms" element={<ClassroomsPage />} />
        <Route path="/lessons" element={<LessonListPage />} />
        <Route path="/lessons/:lessonId/attendance" element={<LessonAttendancePage />} />
        <Route path="/attendance" element={<AttendancePage />} />
        <Route path="*" element={<Navigate to="/students" replace />} />
      </Routes>
    </AppShell>
  );
}
