import request from './request';

/**
 * 获取用户列表
 * @param {object} params 查询参数
 * @returns {Promise} 用户列表
 */
export const getUserList = (params) => {
  return request({
    url: '/users',
    method: 'get',
    params,
  });
};

/**
 * 获取用户详情
 * @param {number} userId 用户ID
 * @returns {Promise} 用户信息
 */
export const getUserById = (userId) => {
  return request({
    url: `/users/${userId}`,
    method: 'get',
  });
};

/**
 * 创建用户
 * @param {object} data 用户数据
 * @returns {Promise} 创建结果
 */
export const createUser = (data) => {
  return request({
    url: '/users',
    method: 'post',
    data,
  });
};

/**
 * 更新用户
 * @param {object} data 用户数据
 * @returns {Promise} 更新结果
 */
export const updateUser = (data) => {
  return request({
    url: '/users',
    method: 'put',
    data,
  });
};

/**
 * 更新用户状态（禁用/启用）
 * @param {number} userId 用户ID
 * @param {number} status 状态：0-禁用/1-正常
 * @returns {Promise} 操作结果
 */
export const updateUserStatus = (userId, status) => {
  return request({
    url: `/users/${userId}/status`,
    method: 'put',
    params: { status },
  });
};

/**
 * 解锁用户
 * @param {number} userId 用户ID
 * @returns {Promise} 操作结果
 */
export const unlockUser = (userId) => {
  return request({
    url: `/users/${userId}/unlock`,
    method: 'put',
  });
};

/**
 * 获取角色列表
 * @returns {Promise} 角色列表
 */
export const getRoleList = () => {
  return request({
    url: '/roles',
    method: 'get',
  });
};



