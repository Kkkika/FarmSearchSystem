// API 基础响应类型
export interface BaseResponse<T = any> {
    code?: number
    data?: T
    message?: string
    success?: boolean
}

export interface BaseResponseInteger extends BaseResponse<number> {}
export interface BaseResponseString extends BaseResponse<string> {}
export interface BaseResponseBoolean extends BaseResponse<boolean> {}
export interface BaseResponseList<T = any> extends BaseResponse<T[]> {}

// 登录相关类型
export interface NodeLoginRequest {
    code: string
    password: string
}

export interface LoginResponse {
    userId?: number
    username?: string
    userType?: string
    enterpriseType?: number
    accessToken?: string
    refreshToken?: string
    tokenType?: string
    expiresIn?: number
    refreshExpiresIn?: number
    message?: string
    loginTime?: number
}

export interface BaseResponseLoginResponse extends BaseResponse<LoginResponse> {}

// 节点企业信息
export interface NodeInfo {
    nodeId?: number
    code?: string
    password?: string
    name?: string
    type?: number
    province?: string
    city?: string
    address?: string
    contactPerson?: string
    contactPhone?: string
    status?: number
    createTime?: string
    updateTime?: string
}

export interface BaseResponseNodeInfo extends BaseResponse<NodeInfo> {}

// 节点企业详情信息
export interface NodeInfoDetailsDto {
    nodeId?: number
    code?: string
    name?: string
    type?: string
    province?: string
    city?: string
    address?: string
    businessId?: string
    epId?: string
    eiaId?: string
    cirId?: string
    fbId?: string
    corporation?: string
    telephone?: string
    regDate?: string
}

export interface BaseResponseNodeInfoDetailsDto extends BaseResponse<NodeInfoDetailsDto> {}

// Token 刷新
export interface RefreshTokenRequest {
    refreshToken?: string
}

// 密码更新参数
export interface UpdatePasswordParams {
    nodeId: number
    oldPassword: string
    newPassword: string
}

// 重置密码参数
export interface ResetPasswordParams {
    code: string
    newPassword: string
}

// 忘记密码参数
export interface ForgotPasswordParams {
    code: string
}

// 省份信息
export interface Province {
    provId?: number
    provName?: string
}

// 添加城市接口参数类型
export interface listCityByProvIdParams {
    provId: number
}

export interface BaseResponseListProvince extends BaseResponse<Province[]> {}

// 城市信息
export interface City {
    cityId?: number
    cityName?: string
    provId?: number
}

// 养殖批次相关类型
export interface FarmBatch {
    fbId?: number
    farmId?: number
    batchId?: string
    type?: string
    agcId?: string
    testName?: string
    remarks?: string
    state?: number
    createTime?: string
    updateTime?: string
}

export interface FarmDetailsDto {
    fbId?: number;
    batchId?: string;
    nodeName?: string;        // 养殖企业名称
    type?: string;            // 产品品种
    agcId?: string;           // 动物检验检疫合格证编号
    testName?: string;        // 官方检疫员名称
    batchDate?: string;       // 批号时间
    state?: string;           // 状态（注意：后端返回的是字符串）
    remarks?: string;         // 备注信息
    farmInfo?: NodeInfoDetailsDto;
}

export interface BaseResponseFarmDetailsDto extends BaseResponse<FarmDetailsDto> {}

export interface FarmSimpleDto {
    fbId?: number
    batchId?: string
    type?: string
    agcId?: string
    testName?: string
    state?: number
}

export interface BaseResponseFarmSimpleDto extends BaseResponse<FarmSimpleDto> {}

// 修改 FarmStateDto 接口
export interface FarmStateDto {
    fbId?: number
    batchId?: string
    type?: string
    state?: number
    batchDate?: string  // 修改为实际的日期字段
}

export interface BaseResponseListFarmStateDto extends BaseResponse<FarmStateDto[]> {}

export interface FarmQueryDto {
    farmId?: number
    state?: number
}

export interface SlauNameDto {
    sbId?: number
    batchId?: string
    slauName?: string
    type?: string
    state?: number
}

export interface BaseResponseListSlauNameDto extends BaseResponse<SlauNameDto[]> {}

// 屠宰批次相关类型
export interface CreateSlauBatchRequestDto {
    batchId: string
    slauId: number
    fbId: number
    type: string
    quaId: string
    testName: string
    remarks?: string
}

export interface SlauBatch {
    sbId?: number
    slauId?: number
    batchId?: string
    fbId?: number
    type?: string
    quaId?: string
    testName?: string
    remarks?: string
    state?: number
    createTime?: string
    updateTime?: string
}

export interface SlauDetailsDto {
    sbId?: number
    batchId?: string
    type?: string
    quaId?: string
    testName?: string
    remarks?: string
    state?: number
    slauInfo?: NodeInfoDetailsDto
    farmBatch?: FarmSimpleDto
}

export interface BaseResponseSlauDetailsDto extends BaseResponse<SlauDetailsDto> {}

export interface SlauBatchDetailDto {
    sbId?: number
    batchId?: string
    type?: string
    quaId?: string
    testName?: string
    remarks?: string
    state?: number
    fbId?: number
    slauId?: number
}

export interface BaseResponseSlauBatchDetailDto extends BaseResponse<SlauBatchDetailDto> {}

export interface SlauStateDto {
    sbId?: number
    batchId?: string
    type?: string
    state?: number
    createTime?: string
}

export interface BaseResponseListSlauStateDto extends BaseResponse<SlauStateDto[]> {}

export interface FarmBatchSimpleDto {
    fbId?: number
    batchId?: string
    type?: string
    state?: number
}

export interface BaseResponseListFarmBatchSimpleDto extends BaseResponse<FarmBatchSimpleDto[]> {}

export interface FarmDto {
    nodeId?: number
    name?: string
    province?: string
    city?: string
}

export interface BaseResponseListFarmDto extends BaseResponse<FarmDto[]> {}

export interface WholNameDto {
    wbId?: number
    batchId?: string
    wholName?: string
    type?: string
    state?: number
}

export interface BaseResponseListWholNameDto extends BaseResponse<WholNameDto[]> {}

export interface SlauQueryDto {
    slauId?: number
    state?: number
}

// 批发商批次相关类型
export interface CreateWholBatchRequestDto {
    batchId: string
    wholId: number
    sbId: number
    type: string
    remarks?: string
}

export interface WholBatch {
    wbId?: number
    wholId?: number
    batchId?: string
    sbId?: number
    type?: string
    remarks?: string
    state?: number
    createTime?: string
    updateTime?: string
}

export interface WholDetailsDto {
    wbId?: number
    batchId?: string
    type?: string
    remarks?: string
    state?: number
    wholInfo?: NodeInfoDetailsDto
    slauBatch?: SlauBatchSimpleDto
}

export interface BaseResponseWholDetailsDto extends BaseResponse<WholDetailsDto> {}

export interface WholBatchDetailDto {
    wbId?: number
    batchId?: string
    type?: string
    remarks?: string
    state?: number
    sbId?: number
    wholId?: number
}

export interface BaseResponseWholBatchDetailDto extends BaseResponse<WholBatchDetailDto> {}

export interface WholStateDto {
    wbId?: number
    batchId?: string
    type?: string
    state?: number
    createTime?: string
}

export interface BaseResponseListWholStateDto extends BaseResponse<WholStateDto[]> {}

export interface SlauBatchSimpleDto {
    sbId?: number
    batchId?: string
    type?: string
    state?: number
}

export interface BaseResponseListSlauBatchSimpleDto extends BaseResponse<SlauBatchSimpleDto[]> {}

export interface SlaughterhouseDto {
    nodeId?: number
    name?: string
    province?: string
    city?: string
}

export interface BaseResponseListSlaughterhouseDto extends BaseResponse<SlaughterhouseDto[]> {}

export interface RetaNameDto {
    rbId?: number
    batchId?: string
    retaName?: string
    type?: string
    state?: number
}

export interface BaseResponseListRetaNameDto extends BaseResponse<RetaNameDto[]> {}

export interface WholQueryDto {
    wholId?: number
    state?: number
}

// 零售商批次相关类型
export interface CreateRetaBatchRequestDto {
    batchId: string
    retaId: number
    wbId: number
    type: string
    remarks?: string
}

export interface RetaBatch {
    rbId?: number
    retaId?: number
    batchId?: string
    wbId?: number
    type?: string
    remarks?: string
    state?: number
    createTime?: string
    updateTime?: string
}

export interface RetaDetailsDto {
    rbId?: number
    batchId?: string
    type?: string
    remarks?: string
    state?: number
    retaInfo?: NodeInfoDetailsDto
    wholBatch?: WholBatchSimpleDto
}

export interface BaseResponseRetaDetailsDto extends BaseResponse<RetaDetailsDto> {}

export interface RetaBatchDetailDto {
    rbId?: number
    batchId?: string
    type?: string
    remarks?: string
    state?: number
    wbId?: number
    retaId?: number
}

export interface BaseResponseRetaBatchDetailDto extends BaseResponse<RetaBatchDetailDto> {}

export interface RetaStateDto {
    rbId?: number
    batchId?: string
    type?: string
    state?: number
    createTime?: string
}

export interface BaseResponseListRetaStateDto extends BaseResponse<RetaStateDto[]> {}

export interface WholBatchSimpleDto {
    wbId?: number
    batchId?: string
    type?: string
    state?: number
}

export interface BaseResponseListWholBatchSimpleDto extends BaseResponse<WholBatchSimpleDto[]> {}

export interface WholesalerDto {
    nodeId?: number
    name?: string
    province?: string
    city?: string
}

export interface BaseResponseListWholesalerDto extends BaseResponse<WholesalerDto[]> {}

export interface RetaQueryDto {
    retaId?: number
    state?: number
}

// 参数类型定义
export interface getNodeInfoDetailByIdParams {
    nodeId: number
}

export interface disableNodeInfoParams {
    nodeId: number
}

export interface enableNodeInfoParams {
    nodeId: number
}

export interface permanentlyDeleteNodeInfoParams {
    nodeId: number
}

export interface resetPasswordParams {
    code: string
    newPassword: string
}

// 密码更新参数
export interface UpdatePasswordParams {
    nodeId: number
    oldPassword: string
    newPassword: string
}

export interface getFarmBatchByStateParams {
    farmId: number
    state?: number
}

export interface getFarmBatchDetailParams {
    fbId: number
}

export interface getFarmInfoByIdParams {
    farmId: number
}

export interface offlineFarmBatchParams {
    fbId: number
}

export interface removeFarmBatchByIdParams {
    fbId: number
}

export interface getFarmBatchSimpleParams {
    fbId: number
}

export interface confirmSlauBatchParams {
    sbId: number
}

export interface getSlauBatchByStateParams {
    slauId: number
    state?: number
}

export interface confirmWholBatchParams {
    wbId: number
}

export interface checkBatchIdExists1Params {
    slauId: number
    batchId: string
}

export interface getFarmBatchesByFarmParams {
    farmId: number
}

export interface getFarmsByAreaParams {
    provId: number
    cityId: number
}

export interface getSlauDetailParams {
    sbId: number
}

export interface getSlauBatchDetailParams {
    sbId: number
}

export interface offlineSlauBatchParams {
    sbId: number
}

export interface removeSlauBatchByIdParams {
    sbId: number
}

export interface getPendingWholBatchParams {
    wholName?: string
}

export interface sendSlauToFarmConfirmParams {
    sbId: number
}

export interface getSlauInfoByIdParams {
    slauId: number
}

export interface getWholBatchByStateParams {
    wholId: number
    state?: number
}

export interface confirmRetaBatchParams {
    rbId: number
}

export interface checkBatchIdExistsParams {
    wholId: number
    batchId: string
}

export interface getSlauBatchesBySlaughterhouseParams {
    slauId: number
}

export interface getSlaughterhousesByAreaParams {
    provId: number
    cityId: number
}

export interface getWholDetailParams {
    wbId: number
}

export interface getWholBatchDetailParams {
    wbId: number
}

export interface offlineWholBatchParams {
    wbId: number
}

export interface getPendingRetaBatchParams {
    retaName?: string
}

export interface removeWholBatchByIdParams {
    wbId: number
}

export interface sendWholToSlauConfirmParams {
    wbId: number
}

export interface getWholInfoByIdParams {
    wholId: number
}

export interface getRetaBatchByStateParams {
    retaId: number
    state?: number
}

export interface checkBatchIdExists2Params {
    retaId: number
    batchId: string
}

export interface getWholBatchesByWholesalerParams {
    wholId: number
}

export interface getWholesalersByAreaParams {
    provId: number
    cityId: number
}

export interface getRetaDetailParams {
    rbId: number
}

export interface getRetaBatchDetailParams {
    rbId: number
}

export interface offlineRetaBatchParams {
    rbId: number
}

export interface removeRetaBatchByIdParams {
    rbId: number
}

export interface sendRetaToWholConfirmParams {
    rbId: number
}

export interface getRetaInfoByIdParams {
    retaId: number
}
