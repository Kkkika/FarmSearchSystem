// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /consumer/trace/${param0} */
export async function getProductTraceInfo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getProductTraceInfoParams,
  options?: { [key: string]: any }
) {
  const { sourceId: param0, ...queryParams } = params
  return request<API.BaseResponseTraceInfoDto>(`/consumer/trace/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
