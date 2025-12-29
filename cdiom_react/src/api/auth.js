import request from './request';

/**
 * 用户登录
 * @param {string} usernameOrPhone 用户名或手机号
 * @param {string} password 密码
 * @returns {Promise} 登录响应
 */
export const login = (usernameOrPhone, password) => {
  return request({
    url: '/auth/login',
    method: 'post',
    data: {
      usernameOrPhone,
      password,
    },
  });
};

/**
 * 用户登出
 * @returns {Promise} 登出响应
 */
export const logout = () => {
  return request({
    url: '/auth/logout',
    method: 'post',
  });
};




