// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 PUT /node/batchDisable */
export async function batchDisableNodeInfo(body: number[], options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/node/batchDisable', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /node/detail/${param0} */
export async function getNodeInfoDetailById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getNodeInfoDetailByIdParams,
  options?: { [key: string]: any }
) {
  const { nodeId: param0, ...queryParams } = params
  return request<API.BaseResponseNodeInfoDetailsDto>(`/node/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /node/disable/${param0} */
export async function disableNodeInfo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.disableNodeInfoParams,
  options?: { [key: string]: any }
) {
  const { nodeId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/node/disable/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /node/edit */
export async function editNodeInfo(body: API.NodeInfo, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/node/edit', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /node/enable/${param0} */
export async function enableNodeInfo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.enableNodeInfoParams,
  options?: { [key: string]: any }
) {
  const { nodeId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/node/enable/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /node/forgotPassword */
export async function forgotPassword(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.forgotPasswordParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInteger>('/node/forgotPassword', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /node/login */
export async function nodeLogin(body: API.NodeLoginRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginResponse>('/node/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /node/page */
export async function listNodeInfoPage(
  body: API.NodeInfoPageRequestDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseNodeInfoPageResponseDto>('/node/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 DELETE /node/permanent/${param0} */
export async function permanentlyDeleteNodeInfo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.permanentlyDeleteNodeInfoParams,
  options?: { [key: string]: any }
) {
  const { nodeId: param0, ...queryParams } = params
  return request<API.BaseResponseInteger>(`/node/permanent/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /node/profile */
export async function getNodeProfile(options?: { [key: string]: any }) {
  return request<API.BaseResponseNodeInfo>('/node/profile', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /node/refreshToken */
export async function refreshToken(
  body: API.RefreshTokenRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLoginResponse>('/node/refreshToken', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /node/resetPassword */
export async function resetPassword(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.resetPasswordParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInteger>('/node/resetPassword', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /node/save */
export async function saveNodeInfo(body: API.NodeInfo, options?: { [key: string]: any }) {
  return request<API.BaseResponseInteger>('/node/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /node/stats/month */
export async function listNodeInfoDataByMonth(options?: { [key: string]: any }) {
  return request<API.BaseResponseListNodeInfoTotalByMonthDto>('/node/stats/month', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /node/stats/prov */
export async function listNodeInfoDataByProv(options?: { [key: string]: any }) {
  return request<API.BaseResponseListNodeInfoTotalByProvDto>('/node/stats/prov', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /node/stats/type */
export async function listNodeInfoDataByType(options?: { [key: string]: any }) {
  return request<API.BaseResponseListNodeInfoTotalByTypeDto>('/node/stats/type', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /node/updatePassword */
export async function updatePassword(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updatePasswordParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInteger>('/node/updatePassword', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
