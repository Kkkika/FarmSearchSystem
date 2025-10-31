import axios from 'axios'
import { message } from 'ant-design-vue'
import { useAuthStore } from '@/stores/auth'

// 创建 Axios 实例
const myAxios = axios.create({
  baseURL: 'http://localhost:8123/api',
  timeout: 60000,
  withCredentials: true,
})

// 全局请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // 添加 JWT token
    const authStore = useAuthStore()
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }

    console.log('🚀 发送请求:', config.method?.toUpperCase(), config.url)
    return config
  },
  function (error) {
    return Promise.reject(error)
  },
)

// 全局响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    const { data } = response
    console.log('✅ 收到响应:', response.status, response.data)

    // 未登录或 token 过期
    if (data.code === 40100) {
      const authStore = useAuthStore()
      authStore.logout()
      message.warning('登录已过期，请重新登录')

      // 跳转到登录页面
      if (!window.location.pathname.includes('/node/login')) {
        window.location.href = '/node/login'
      }
    }

    return data
  },
  function (error) {
    console.error('❌ 请求错误:', error)

    if (error.response?.status === 401) {
      const authStore = useAuthStore()
      authStore.logout()
      message.warning('登录已过期，请重新登录')

      if (!window.location.pathname.includes('/node/login')) {
        window.location.href = '/node/login'
      }
    }

    return Promise.reject(error)
  },
)

export default myAxios
