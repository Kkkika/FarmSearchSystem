// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /slau/batch/byState/${param0} */
export async function getSlauBatchByState(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSlauBatchByStateParams,
  options?: { [key: string]: any }
) {
  const { slauId: param0, ...queryParams } = params
  return request<API.BaseResponseListSlauStateDto>(`/slau/batch/byState/${param0}`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /slau/batch/confirm/${param0} */
export async function confirmWholBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.confirmWholBatchParams,
  options?: { [key: string]: any }
) {
  const { wbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/slau/batch/confirm/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /slau/batch/create */
export async function createSlauBatch(
  body: API.CreateSlauBatchRequestDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInteger>('/slau/batch/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /slau/batch/create/checkBatchId */
export async function checkBatchIdExists1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.checkBatchIdExists1Params,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/slau/batch/create/checkBatchId', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /slau/batch/create/farmBatches/${param0} */
export async function getFarmBatchesByFarm(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFarmBatchesByFarmParams,
  options?: { [key: string]: any }
) {
  const { farmId: param0, ...queryParams } = params
  return request<API.BaseResponseListFarmBatchSimpleDto>(
    `/slau/batch/create/farmBatches/${param0}`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    }
  )
}

/** 此处后端没有提供注释 GET /slau/batch/create/farms */
export async function getFarmsByArea(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getFarmsByAreaParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListFarmDto>('/slau/batch/create/farms', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /slau/batch/detail/${param0} */
export async function getSlauDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSlauDetailParams,
  options?: { [key: string]: any }
) {
  const { sbId: param0, ...queryParams } = params
  return request<API.BaseResponseSlauDetailsDto>(`/slau/batch/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /slau/batch/detailBatch/${param0} */
export async function getSlauBatchDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSlauBatchDetailParams,
  options?: { [key: string]: any }
) {
  const { sbId: param0, ...queryParams } = params
  return request<API.BaseResponseSlauBatchDetailDto>(`/slau/batch/detailBatch/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /slau/batch/edit */
export async function editSlauBatch(body: API.SlauBatch, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/slau/batch/edit', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /slau/batch/offline/${param0} */
export async function offlineSlauBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.offlineSlauBatchParams,
  options?: { [key: string]: any }
) {
  const { sbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/slau/batch/offline/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /slau/batch/pending */
export async function getPendingWholBatch(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPendingWholBatchParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWholNameDto>('/slau/batch/pending', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /slau/batch/remove/${param0} */
export async function removeSlauBatchById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.removeSlauBatchByIdParams,
  options?: { [key: string]: any }
) {
  const { sbId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/slau/batch/remove/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /slau/batch/save */
export async function saveSlauBatch(body: API.SlauBatch, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/slau/batch/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /slau/batch/slau-to-farm/${param0} */
export async function sendSlauToFarmConfirm(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.sendSlauToFarmConfirmParams,
  options?: { [key: string]: any }
) {
  const { sbId: param0, ...queryParams } = params
  return request<API.BaseResponseString>(`/slau/batch/slau-to-farm/${param0}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /slau/batch/slauInfo/${param0} */
export async function getSlauInfoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSlauInfoByIdParams,
  options?: { [key: string]: any }
) {
  const { slauId: param0, ...queryParams } = params
  return request<API.BaseResponseNodeInfoDetailsDto>(`/slau/batch/slauInfo/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
