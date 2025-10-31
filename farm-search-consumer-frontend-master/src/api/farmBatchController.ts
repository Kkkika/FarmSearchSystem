// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /farm/byState/${param0} */
export async function getFarmBatchByState(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFarmBatchByStateParams,
  options?: { [key: string]: any }
) {
  const { farmId: param0, ...queryParams } = params
  return request<API.BaseResponseListFarmStateDto>(`/farm/byState/${param0}`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /farm/detail/${param0} */
export async function getFarmBatchDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFarmBatchDetailParams,
  options?: { [key: string]: any }
) {
  const { fbId: param0, ...queryParams } = params
  return request<API.BaseResponseFarmDetailsDto>(`/farm/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /farm/edit */
export async function editFarmBatch(body: API.FarmBatch, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/farm/edit', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /farm/farmInfo/${param0} */
export async function getFarmInfoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFarmInfoByIdParams,
  options?: { [key: string]: any }
) {
  const { farmId: param0, ...queryParams } = params
  return request<API.BaseResponseNodeInfoDetailsDto>(`/farm/farmInfo/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /farm/offline/${param0} */
export async function offlineFarmBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.offlineFarmBatchParams,
  options?: { [key: string]: any }
) {
  const { fbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/farm/offline/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /farm/remove/${param0} */
export async function removeFarmBatchById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.removeFarmBatchByIdParams,
  options?: { [key: string]: any }
) {
  const { fbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/farm/remove/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /farm/save */
export async function saveFarmBatch(body: API.FarmBatch, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/farm/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /farm/simple/${param0} */
export async function getFarmBatchSimple(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFarmBatchSimpleParams,
  options?: { [key: string]: any }
) {
  const { fbId: param0, ...queryParams } = params
  return request<API.BaseResponseFarmSimpleDto>(`/farm/simple/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /farm/slauBatch/confirm/${param0} */
export async function confirmSlauBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.confirmSlauBatchParams,
  options?: { [key: string]: any }
) {
  const { sbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/farm/slauBatch/confirm/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /farm/slauBatch/pending */
export async function getPendingSlauBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPendingSlauBatchParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListSlauNameDto>('/farm/slauBatch/pending', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
