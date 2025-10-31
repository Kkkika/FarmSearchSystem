import request from '@/request'
import type { BaseResponseString } from '@/types/api'

/** 健康检查 GET /health/ */
export async function healthCheck(options?: { [key: string]: any }) {
  return request<BaseResponseString>('/health/', {
    method: 'GET',
    ...(options || {}),
  })
}
