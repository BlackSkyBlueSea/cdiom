import Cookies from 'js-cookie';

const TOKEN_KEY = 'cdiom_token';
const USER_INFO_KEY = 'cdiom_user_info';

/**
 * 获取Token
 * @returns {string|null} Token字符串
 */
export const getToken = () => {
  // 优先从Cookie获取（后端设置的）
  const cookieToken = Cookies.get(TOKEN_KEY);
  if (cookieToken) {
    return cookieToken;
  }
  // 从localStorage获取（备用）
  return localStorage.getItem(TOKEN_KEY) || null;
};

/**
 * 设置Token
 * @param {string} token Token字符串
 * @param {number} expires 过期时间（小时），默认8小时
 */
export const setToken = (token, expires = 8) => {
  // 存储到localStorage（备用）
  localStorage.setItem(TOKEN_KEY, token);
  // 存储到Cookie（与后端保持一致）
  Cookies.set(TOKEN_KEY, token, { expires: expires / 24 }); // Cookie的expires是天数
};

/**
 * 移除Token
 */
export const removeToken = () => {
  localStorage.removeItem(TOKEN_KEY);
  Cookies.remove(TOKEN_KEY);
};

/**
 * 获取用户信息
 * @returns {object|null} 用户信息对象
 */
export const getUserInfo = () => {
  const userInfo = localStorage.getItem(USER_INFO_KEY);
  if (userInfo) {
    try {
      return JSON.parse(userInfo);
    } catch (e) {
      console.error('解析用户信息失败:', e);
      return null;
    }
  }
  return null;
};

/**
 * 设置用户信息
 * @param {object} userInfo 用户信息对象
 */
export const setUserInfo = (userInfo) => {
  localStorage.setItem(USER_INFO_KEY, JSON.stringify(userInfo));
};

/**
 * 移除用户信息
 */
export const removeUserInfo = () => {
  localStorage.removeItem(USER_INFO_KEY);
};

/**
 * 检查用户是否已登录
 * @returns {boolean} 是否已登录
 */
export const isAuthenticated = () => {
  return !!getToken();
};

/**
 * 清除所有认证信息
 */
export const clearAuth = () => {
  removeToken();
  removeUserInfo();
  // 清除Cookie
  Cookies.remove(TOKEN_KEY);
};

/**
 * 检查用户权限
 * @param {string|array} roles 需要的角色（字符串或数组）
 * @returns {boolean} 是否有权限
 */
export const hasRole = (roles) => {
  const userInfo = getUserInfo();
  if (!userInfo || !userInfo.roleName) {
    return false;
  }

  const userRole = userInfo.roleName;
  const requiredRoles = Array.isArray(roles) ? roles : [roles];

  return requiredRoles.includes(userRole);
};

/**
 * 获取用户角色名称
 * @returns {string|null} 角色名称
 */
export const getUserRole = () => {
  const userInfo = getUserInfo();
  return userInfo?.roleName || null;
};







