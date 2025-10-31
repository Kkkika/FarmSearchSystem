// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /whol/batch/byState/${param0} */
export async function getWholBatchByState(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getWholBatchByStateParams,
  options?: { [key: string]: any }
) {
  const { wholId: param0, ...queryParams } = params
  return request<API.BaseResponseListWholStateDto>(`/whol/batch/byState/${param0}`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /whol/batch/confirm/${param0} */
export async function confirmRetaBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.confirmRetaBatchParams,
  options?: { [key: string]: any }
) {
  const { rbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/whol/batch/confirm/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /whol/batch/create */
export async function createWholBatch(
  body: API.CreateWholBatchRequestDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInteger>('/whol/batch/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /whol/batch/create/checkBatchId */
export async function checkBatchIdExists(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkBatchIdExistsParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/whol/batch/create/checkBatchId', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /whol/batch/create/slauBatches/${param0} */
export async function getSlauBatchesBySlaughterhouse(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSlauBatchesBySlaughterhouseParams,
  options?: { [key: string]: any }
) {
  const { slauId: param0, ...queryParams } = params
  return request<API.BaseResponseListSlauBatchSimpleDto>(
    `/whol/batch/create/slauBatches/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** 此处后端没有提供注释 GET /whol/batch/create/slaughterhouses */
export async function getSlaughterhousesByArea(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSlaughterhousesByAreaParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListSlaughterhouseDto>('/whol/batch/create/slaughterhouses', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /whol/batch/detail/${param0} */
export async function getWholDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getWholDetailParams,
  options?: { [key: string]: any }
) {
  const { wbId: param0, ...queryParams } = params
  return request<API.BaseResponseWholDetailsDto>(`/whol/batch/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /whol/batch/detailBatch/${param0} */
export async function getWholBatchDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getWholBatchDetailParams,
  options?: { [key: string]: any }
) {
  const { wbId: param0, ...queryParams } = params
  return request<API.BaseResponseWholBatchDetailDto>(`/whol/batch/detailBatch/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /whol/batch/edit */
export async function editWholBatch(body: API.WholBatch, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/whol/batch/edit', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /whol/batch/offline/${param0} */
export async function offlineWholBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.offlineWholBatchParams,
  options?: { [key: string]: any }
) {
  const { wbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/whol/batch/offline/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /whol/batch/pending */
export async function getPendingRetaBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPendingRetaBatchParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListRetaNameDto>('/whol/batch/pending', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /whol/batch/remove/${param0} */
export async function removeWholBatchById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.removeWholBatchByIdParams,
  options?: { [key: string]: any }
) {
  const { wbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/whol/batch/remove/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /whol/batch/save */
export async function saveWholBatch(body: API.WholBatch, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/whol/batch/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /whol/batch/whol-to-slau/${param0} */
export async function sendWholToSlauConfirm(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.sendWholToSlauConfirmParams,
  options?: { [key: string]: any }
) {
  const { wbId: param0, ...queryParams } = params
  return request<API.BaseResponseString>(`/whol/batch/whol-to-slau/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /whol/batch/wholInfo/${param0} */
export async function getWholInfoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getWholInfoByIdParams,
  options?: { [key: string]: any }
) {
  const { wholId: param0, ...queryParams } = params
  return request<API.BaseResponseNodeInfoDetailsDto>(`/whol/batch/wholInfo/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
