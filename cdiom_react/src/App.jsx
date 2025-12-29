import { Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import PrivateRoute from './components/common/PrivateRoute';
import MainLayout from './components/layout/MainLayout';
import Dashboard from './pages/Dashboard';
import UserManagement from './pages/UserManagement';

function App() {
  return (
    <Routes>
      {/* 登录页路由（保留用于直接访问） */}
      <Route path="/login" element={<Login />} />
      {/* 首页路由（无需登录） */}
      <Route path="/" element={<Home />} />
      {/* 需要登录的主应用路由 */}
      <Route
        path="/app"
        element={
          <PrivateRoute>
            <MainLayout />
          </PrivateRoute>
        }
      >
        <Route index element={<Dashboard />} />
        <Route path="users" element={<UserManagement />} />
        {/* 其他路由将在后续添加 */}
      </Route>
      {/* 默认重定向到首页 */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;

