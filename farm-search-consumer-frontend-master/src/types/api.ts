// API 基础响应类型
export interface BaseResponse<T = any> {
  code?: number
  data?: T
  message?: string
  success?: boolean
}

// 字符串响应
export interface BaseResponseString extends BaseResponse<string> {}

// 溯源信息响应
export interface BaseResponseTraceInfoDto extends BaseResponse<TraceInfoDto> {}

// 溯源信息DTO
export interface TraceInfoDto {
  farmName?: string
  farmProvince?: string
  farmCity?: string
  farmBatchId?: string
  farmType?: string
  farmAgcId?: string
  farmTestName?: string
  farmBatchDate?: string

  slauName?: string
  slauProvince?: string
  slauCity?: string
  slauBatchId?: string
  slauType?: string
  slauQuaId?: string
  slauTestName?: string
  slauBatchDate?: string

  wholName?: string
  wholProvince?: string
  wholCity?: string
  wholBatchId?: string
  wholType?: string
  wholBatchDate?: string

  retaName?: string
  retaProvince?: string
  retaCity?: string
  retaBatchId?: string
  retaType?: string
  retaBatchDate?: string
}

// 请求参数类型
export interface getProductTraceInfoParams {
  sourceId: string
}
