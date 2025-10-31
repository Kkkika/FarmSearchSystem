import axios from 'axios'

const request = axios.create({
  baseURL: 'http://localhost:8123/api', // 根据后端实际地址调整
  timeout: 10000,
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    console.log('🚀 发送请求:', config.method?.toUpperCase(), config.url, config.params || config.data)
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    console.log('✅ 收到响应:', response.status, response.data)
    return response.data
  },
  (error) => {
    console.error('❌ 请求错误:', error)
    if (error.response) {
      // 服务器返回错误状态码
      console.error('错误详情:', error.response.status, error.response.data)
    } else if (error.request) {
      // 请求发送失败
      console.error('网络错误:', error.request)
    } else {
      // 其他错误
      console.error('错误信息:', error.message)
    }
    return Promise.reject(error)
  }
)

export default request
