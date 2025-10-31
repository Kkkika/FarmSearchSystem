// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 DELETE /admin/delete/${param0} */
export async function deleteAdmin(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteAdminParams,
  options?: { [key: string]: any }
) {
  const { adminId: param0, ...queryParams } = params
  return request<API.BaseResponseBoolean>(`/admin/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /admin/list */
export async function getAllAdmins(options?: { [key: string]: any }) {
  return request<API.BaseResponseListAdminWithoutPasswordDTO>('/admin/list', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /admin/login */
export async function adminLogin(body: API.AdminLoginRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginResponse>('/admin/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /admin/profile */
export async function getAdminProfile(options?: { [key: string]: any }) {
  return request<API.BaseResponseAdminWithoutPasswordDTO>('/admin/profile', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /admin/refreshToken */
export async function refreshToken1(
  body: API.RefreshTokenRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLoginResponse>('/admin/refreshToken', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /admin/register */
export async function registerAdmin(
  body: API.AdminRegisterRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/admin/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /admin/resetPassword */
export async function resetAdminPassword(
  body: API.ResetPasswordRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseResetPasswordResponse>('/admin/resetPassword', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /admin/role */
export async function getAdminRole(options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/admin/role', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 PUT /admin/update */
export async function updateAdmin(body: API.Admin, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/admin/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 POST /admin/updatePassword */
export async function updateAdminPassword(
  body: API.AdminLoginRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean>('/admin/updatePassword', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
