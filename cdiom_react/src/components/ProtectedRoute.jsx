import { Navigate } from 'react-router-dom';
import { isAuthenticated } from '@/utils/auth';

/**
 * 受保护的路由组件
 * 用于需要登录才能访问的页面
 */
const ProtectedRoute = ({ children }) => {
  if (!isAuthenticated()) {
    // 未登录，跳转到登录页
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;

