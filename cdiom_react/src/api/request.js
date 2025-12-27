import axios from 'axios'
import { message } from 'antd'
import Cookies from 'js-cookie'
import { getToken } from '@/utils/auth'

// 创建 axios 实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api/v1',
  timeout: 30000, // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 从 Cookie 或 localStorage 获取 token
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data

    // 如果返回的状态码为 200，说明接口请求成功
    if (res.code === 200) {
      return res
    } else {
      // 如果返回的状态码不是 200，说明接口请求失败
      message.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  (error) => {
    console.error('响应错误:', error)

    if (error.response) {
      const { status, data } = error.response

      switch (status) {
        case 401:
          message.error('未授权，请重新登录')
          // 清除 token 并跳转到登录页
          Cookies.remove('token')
          window.location.href = '/login'
          break
        case 403:
          message.error('拒绝访问')
          break
        case 404:
          message.error('请求的资源不存在')
          break
        case 500:
          message.error('服务器内部错误')
          break
        default:
          message.error(data?.message || `请求失败: ${status}`)
      }
    } else if (error.request) {
      message.error('网络错误，请检查网络连接')
    } else {
      message.error(error.message || '请求失败')
    }

    return Promise.reject(error)
  }
)

export default service


