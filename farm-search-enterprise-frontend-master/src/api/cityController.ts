// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /city/listByProvince */
export async function listCityByProvId(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listCityByProvIdParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListCity>('/city/listByProvince', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
