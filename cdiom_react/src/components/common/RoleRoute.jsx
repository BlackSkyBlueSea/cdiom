import { Navigate } from 'react-router-dom';
import { hasRole } from '@/utils/auth';

/**
 * 角色权限路由组件
 * 用于保护需要特定角色权限才能访问的路由
 *
 * @param {ReactNode} children 子组件
 * @param {string|array} requiredRoles 需要的角色（字符串或数组）
 */
const RoleRoute = ({ children, requiredRoles }) => {
  if (hasRole(requiredRoles)) {
    return children;
  }

  return <Navigate to="/" replace />;
};

export default RoleRoute;




