import axios from 'axios';
import { message } from 'antd';
import { getToken, clearAuth } from '@/utils/auth';

// 创建axios实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api/v1',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
  },
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从localStorage获取token
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res = response.data;

    // 如果响应数据是Result格式（后端统一响应格式）
    if (res.code !== undefined) {
      // 成功响应
      if (res.code === 200 || res.code === 0) {
        return res;
      }
      // 业务错误
      message.error(res.message || '请求失败');
      return Promise.reject(new Error(res.message || '请求失败'));
    }

    // 直接返回响应数据
    return res;
  },
  (error) => {
    console.error('响应错误:', error);

    if (error.response) {
      const { status, data } = error.response;

      switch (status) {
        case 401:
          message.error('未授权，请重新登录');
          clearAuth();
          // 跳转到登录页
          setTimeout(() => {
            window.location.href = '/login';
          }, 1000);
          break;
        case 403:
          message.error('拒绝访问');
          break;
        case 404:
          message.error('请求的资源不存在');
          break;
        case 500:
          message.error('服务器内部错误');
          break;
        default:
          message.error(data?.message || `请求失败: ${status}`);
      }
    } else if (error.request) {
      message.error('网络错误，请检查网络连接');
    } else {
      message.error(error.message || '请求配置错误');
    }

    return Promise.reject(error);
  }
);

export default request;







