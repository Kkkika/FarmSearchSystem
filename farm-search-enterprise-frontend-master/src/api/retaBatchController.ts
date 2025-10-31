// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /reta/batch/byState/${param0} */
export async function getRetaBatchByState(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRetaBatchByStateParams,
  options?: { [key: string]: any }
) {
  const { retaId: param0, ...queryParams } = params
  return request<API.BaseResponseListRetaStateDto>(`/reta/batch/byState/${param0}`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /reta/batch/create */
export async function createRetaBatch(
  body: API.CreateRetaBatchRequestDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInteger>('/reta/batch/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /reta/batch/create/checkBatchId */
export async function checkBatchIdExists2(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkBatchIdExists2Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/reta/batch/create/checkBatchId', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /reta/batch/create/wholBatches/${param0} */
export async function getWholBatchesByWholesaler(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getWholBatchesByWholesalerParams,
  options?: { [key: string]: any }
) {
  const { wholId: param0, ...queryParams } = params
  return request<API.BaseResponseListWholBatchSimpleDto>(
    `/reta/batch/create/wholBatches/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** 此处后端没有提供注释 GET /reta/batch/create/wholesalers */
export async function getWholesalersByArea(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getWholesalersByAreaParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWholesalerDto>('/reta/batch/create/wholesalers', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /reta/batch/detail/${param0} */
export async function getRetaDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRetaDetailParams,
  options?: { [key: string]: any }
) {
  const { rbId: param0, ...queryParams } = params
  return request<API.BaseResponseRetaDetailsDto>(`/reta/batch/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /reta/batch/detailBatch/${param0} */
export async function getRetaBatchDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRetaBatchDetailParams,
  options?: { [key: string]: any }
) {
  const { rbId: param0, ...queryParams } = params
  return request<API.BaseResponseRetaBatchDetailDto>(`/reta/batch/detailBatch/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /reta/batch/edit */
export async function editRetaBatch(body: API.RetaBatch, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/reta/batch/edit', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /reta/batch/offline/${param0} */
export async function offlineRetaBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.offlineRetaBatchParams,
  options?: { [key: string]: any }
) {
  const { rbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/reta/batch/offline/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /reta/batch/remove/${param0} */
export async function removeRetaBatchById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.removeRetaBatchByIdParams,
  options?: { [key: string]: any }
) {
  const { rbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/reta/batch/remove/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /reta/batch/reta-to-whol/${param0} */
export async function sendRetaToWholConfirm(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.sendRetaToWholConfirmParams,
  options?: { [key: string]: any }
) {
  const { rbId: param0, ...queryParams } = params
  return request<API.BaseResponseString>(`/reta/batch/reta-to-whol/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /reta/batch/retaInfo/${param0} */
export async function getRetaInfoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRetaInfoByIdParams,
  options?: { [key: string]: any }
) {
  const { retaId: param0, ...queryParams } = params
  return request<API.BaseResponseNodeInfoDetailsDto>(`/reta/batch/retaInfo/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /reta/batch/save */
export async function saveRetaBatch(body: API.RetaBatch, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/reta/batch/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
