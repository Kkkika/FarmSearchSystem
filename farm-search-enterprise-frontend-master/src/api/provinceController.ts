// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /province/list */
export async function listProvinceAll(options?: { [key: string]: any }) {
  return request<API.BaseResponseListProvince>('/province/list', {
    method: 'GET',
    ...(options || {}),
  })
}
