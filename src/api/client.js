import axios from 'axios'
import { useAuth } from '../store/auth'

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 15000,
})

client.interceptors.request.use((config) => {
  const { token } = useAuth()
  if (token.value) {
    config.headers['X-Token'] = token.value
    config.headers.Authorization = `Bearer ${token.value}`
  }
  return config
})

const isFallbackError = (error) => {
  const status = error.response?.status
  const message = error.response?.data?.message || error.message || ''
  return status === 503 || 
         status === 429 || 
         message.includes('熔断') || 
         message.includes('降级') || 
         message.includes('服务器繁忙') ||
         message.includes('服务不可用')
}

let fallbackPending = false

client.interceptors.response.use(
  (response) => {
    let payload = response.data
    if (payload && typeof payload.code === 'number') {
      if (payload.code !== 0) {
        if (payload.message && (payload.message.includes('熔断') || payload.message.includes('降级'))) {
          handleFallback()
        }
        return Promise.reject(new Error(payload.message || '请求失败'))
      }
      payload = payload.data
    }
    if (payload && payload.data !== undefined) {
      payload = payload.data
    }
    return payload
  },
  (error) => {
    if (isFallbackError(error)) {
      handleFallback()
    }
    return Promise.reject(new Error(error.response?.data?.message || error.message || '网络异常'))
  },
)

function handleFallback() {
  if (fallbackPending) return
  fallbackPending = true
  const currentPath = window.location.pathname
  if (currentPath !== '/fallback') {
    const redirect = encodeURIComponent(currentPath + window.location.search)
    window.location.href = `/fallback?redirect=${redirect}`
  }
}

export default client

