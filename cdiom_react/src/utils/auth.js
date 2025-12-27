import Cookies from 'js-cookie'

const TOKEN_KEY = 'token'

/**
 * 获取 Token
 * @returns {string|null} Token 值
 */
export function getToken() {
  return Cookies.get(TOKEN_KEY) || localStorage.getItem(TOKEN_KEY)
}

/**
 * 设置 Token
 * @param {string} token Token 值
 * @param {number} expires 过期时间（天数），默认 7 天
 */
export function setToken(token, expires = 7) {
  Cookies.set(TOKEN_KEY, token, { expires })
  localStorage.setItem(TOKEN_KEY, token)
}

/**
 * 移除 Token
 */
export function removeToken() {
  Cookies.remove(TOKEN_KEY)
  localStorage.removeItem(TOKEN_KEY)
}

/**
 * 判断是否已登录
 * @returns {boolean} 是否已登录
 */
export function isLoggedIn() {
  return !!getToken()
}

/**
 * 检查权限
 * @param {string|string[]} permissions 需要的权限
 * @param {string[]} userPermissions 用户拥有的权限
 * @returns {boolean} 是否有权限
 */
export function hasPermission(permissions, userPermissions = []) {
  if (!permissions) {
    return true
  }

  if (Array.isArray(permissions)) {
    return permissions.some(permission => userPermissions.includes(permission))
  }

  return userPermissions.includes(permissions)
}

/**
 * 检查角色
 * @param {string|string[]} roles 需要的角色
 * @param {string[]} userRoles 用户拥有的角色
 * @returns {boolean} 是否有角色
 */
export function hasRole(roles, userRoles = []) {
  if (!roles) {
    return true
  }

  if (Array.isArray(roles)) {
    return roles.some(role => userRoles.includes(role))
  }

  return userRoles.includes(roles)
}


