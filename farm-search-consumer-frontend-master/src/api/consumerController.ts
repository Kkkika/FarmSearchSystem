import request from '@/request'
import type {
  BaseResponseTraceInfoDto,
  getProductTraceInfoParams
} from '@/types/api'

/** 获取产品溯源信息 GET /consumer/trace/${sourceId} */
export async function getProductTraceInfo(
  params: getProductTraceInfoParams,
  options?: { [key: string]: any }
) {
  const { sourceId: param0, ...queryParams } = params
  return request<BaseResponseTraceInfoDto>(`/consumer/trace/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
