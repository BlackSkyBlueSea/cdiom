import { Navigate } from 'react-router-dom';
import { isAuthenticated } from '@/utils/auth';

/**
 * 私有路由组件
 * 用于保护需要登录才能访问的路由
 */
const PrivateRoute = ({ children }) => {
  if (isAuthenticated()) {
    return children;
  }

  return <Navigate to="/login" replace />;
};

export default PrivateRoute;




