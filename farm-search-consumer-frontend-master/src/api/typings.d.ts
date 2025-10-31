declare namespace API {
  type Admin = {
    adminId?: number
    adminName?: string
    adminPassword?: string
    remarks?: string
    role?: string
  }

  type AdminLoginRequest = {
    adminId?: number
    adminName?: string
    adminPassword?: string
    oldPassword?: string
    newPassword?: string
  }

  type AdminRegisterRequest = {
    adminName?: string
    adminPassword?: string
    role?: string
    remarks?: string
  }

  type AdminWithoutPasswordDTO = {
    adminId?: number
    adminName?: string
    role?: string
    remarks?: string
  }

  type BaseResponseAdminWithoutPasswordDTO = {
    code?: number
    data?: AdminWithoutPasswordDTO
    message?: string
  }

  type BaseResponseBoolean = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseFarmDetailsDto = {
    code?: number
    data?: FarmDetailsDto
    message?: string
  }

  type BaseResponseFarmSimpleDto = {
    code?: number
    data?: FarmSimpleDto
    message?: string
  }

  type BaseResponseInteger = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseListAdminWithoutPasswordDTO = {
    code?: number
    data?: AdminWithoutPasswordDTO[]
    message?: string
  }

  type BaseResponseListCity = {
    code?: number
    data?: City[]
    message?: string
  }

  type BaseResponseListFarmBatchSimpleDto = {
    code?: number
    data?: FarmBatchSimpleDto[]
    message?: string
  }

  type BaseResponseListFarmDto = {
    code?: number
    data?: FarmDto[]
    message?: string
  }

  type BaseResponseListFarmStateDto = {
    code?: number
    data?: FarmStateDto[]
    message?: string
  }

  type BaseResponseListNodeInfoTotalByMonthDto = {
    code?: number
    data?: NodeInfoTotalByMonthDto[]
    message?: string
  }

  type BaseResponseListNodeInfoTotalByProvDto = {
    code?: number
    data?: NodeInfoTotalByProvDto[]
    message?: string
  }

  type BaseResponseListNodeInfoTotalByTypeDto = {
    code?: number
    data?: NodeInfoTotalByTypeDto[]
    message?: string
  }

  type BaseResponseListProvince = {
    code?: number
    data?: Province[]
    message?: string
  }

  type BaseResponseListRetaNameDto = {
    code?: number
    data?: RetaNameDto[]
    message?: string
  }

  type BaseResponseListRetaStateDto = {
    code?: number
    data?: RetaStateDto[]
    message?: string
  }

  type BaseResponseListSlauBatchSimpleDto = {
    code?: number
    data?: SlauBatchSimpleDto[]
    message?: string
  }

  type BaseResponseListSlaughterhouseDto = {
    code?: number
    data?: SlaughterhouseDto[]
    message?: string
  }

  type BaseResponseListSlauNameDto = {
    code?: number
    data?: SlauNameDto[]
    message?: string
  }

  type BaseResponseListSlauStateDto = {
    code?: number
    data?: SlauStateDto[]
    message?: string
  }

  type BaseResponseListWholBatchSimpleDto = {
    code?: number
    data?: WholBatchSimpleDto[]
    message?: string
  }

  type BaseResponseListWholesalerDto = {
    code?: number
    data?: WholesalerDto[]
    message?: string
  }

  type BaseResponseListWholNameDto = {
    code?: number
    data?: WholNameDto[]
    message?: string
  }

  type BaseResponseListWholStateDto = {
    code?: number
    data?: WholStateDto[]
    message?: string
  }

  type BaseResponseLoginResponse = {
    code?: number
    data?: LoginResponse
    message?: string
  }

  type BaseResponseNodeInfo = {
    code?: number
    data?: NodeInfo
    message?: string
  }

  type BaseResponseNodeInfoDetailsDto = {
    code?: number
    data?: NodeInfoDetailsDto
    message?: string
  }

  type BaseResponseNodeInfoPageResponseDto = {
    code?: number
    data?: NodeInfoPageResponseDto
    message?: string
  }

  type BaseResponseResetPasswordResponse = {
    code?: number
    data?: ResetPasswordResponse
    message?: string
  }

  type BaseResponseRetaBatchDetailDto = {
    code?: number
    data?: RetaBatchDetailDto
    message?: string
  }

  type BaseResponseRetaDetailsDto = {
    code?: number
    data?: RetaDetailsDto
    message?: string
  }

  type BaseResponseSlauBatchDetailDto = {
    code?: number
    data?: SlauBatchDetailDto
    message?: string
  }

  type BaseResponseSlauDetailsDto = {
    code?: number
    data?: SlauDetailsDto
    message?: string
  }

  type BaseResponseString = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseTraceInfoDto = {
    code?: number
    data?: TraceInfoDto
    message?: string
  }

  type BaseResponseWholBatchDetailDto = {
    code?: number
    data?: WholBatchDetailDto
    message?: string
  }

  type BaseResponseWholDetailsDto = {
    code?: number
    data?: WholDetailsDto
    message?: string
  }

  type checkBatchIdExists1Params = {
    slauId: number
    batchId: string
  }

  type checkBatchIdExists2Params = {
    retaId: number
    batchId: string
  }

  type checkBatchIdExistsParams = {
    wholId: number
    batchId: string
  }

  type City = {
    cityId?: number
    cityName?: string
    provId?: number
    remarks?: string
  }

  type confirmRetaBatchParams = {
    rbId: number
  }

  type confirmSlauBatchParams = {
    sbId: number
  }

  type confirmWholBatchParams = {
    wbId: number
  }

  type CreateRetaBatchRequestDto = {
    batchId?: string
    retaId?: number
    wbId?: number
    type?: string
    remarks?: string
  }

  type CreateSlauBatchRequestDto = {
    batchId?: string
    slauId?: number
    fbId?: number
    type?: string
    quaId?: string
    testName?: string
    remarks?: string
  }

  type CreateWholBatchRequestDto = {
    batchId?: string
    wholId?: number
    sbId?: number
    type?: string
    remarks?: string
  }

  type deleteAdminParams = {
    adminId: number
  }

  type disableNodeInfoParams = {
    nodeId: number
  }

  type enableNodeInfoParams = {
    nodeId: number
  }

  type FarmBatch = {
    fbId?: number
    batchId?: string
    farmId?: number
    type?: string
    agcId?: string
    testName?: string
    batchDate?: string
    state?: number
    remarks?: string
  }

  type FarmBatchSimpleDto = {
    fbId?: number
    batchId?: string
    type?: string
  }

  type FarmDetailsDto = {
    batchId?: string
    nodeName?: string
    type?: string
    agcId?: string
    testName?: string
    batchDate?: string
    state?: string
  }

  type FarmDto = {
    nodeId?: number
    name?: string
  }

  type FarmSimpleDto = {
    batchId?: string
    province?: string
    city?: string
    type?: string
    agcId?: string
    testName?: string
  }

  type FarmStateDto = {
    fbId?: number
    batchId?: string
    type?: string
    batchDate?: string
    state?: number
  }

  type forgotPasswordParams = {
    code: string
    newPassword: string
  }

  type getFarmBatchByStateParams = {
    farmId: number
    state?: number
  }

  type getFarmBatchDetailParams = {
    fbId: number
  }

  type getFarmBatchesByFarmParams = {
    farmId: number
  }

  type getFarmBatchSimpleParams = {
    fbId: number
  }

  type getFarmInfoByIdParams = {
    farmId: number
  }

  type getFarmsByAreaParams = {
    provId: number
    cityId: number
  }

  type getNodeInfoDetailByIdParams = {
    nodeId: number
  }

  type getPendingRetaBatchParams = {
    retaName?: string
  }

  type getPendingSlauBatchParams = {
    slauName?: string
  }

  type getPendingWholBatchParams = {
    wholName?: string
  }

  type getProductTraceInfoParams = {
    sourceId: string
  }

  type getRetaBatchByStateParams = {
    retaId: number
    state?: number
  }

  type getRetaBatchDetailParams = {
    rbId: number
  }

  type getRetaDetailParams = {
    rbId: number
  }

  type getRetaInfoByIdParams = {
    retaId: number
  }

  type getSlauBatchByStateParams = {
    slauId: number
    state?: number
  }

  type getSlauBatchDetailParams = {
    sbId: number
  }

  type getSlauBatchesBySlaughterhouseParams = {
    slauId: number
  }

  type getSlauDetailParams = {
    sbId: number
  }

  type getSlaughterhousesByAreaParams = {
    provId: number
    cityId: number
  }

  type getSlauInfoByIdParams = {
    slauId: number
  }

  type getWholBatchByStateParams = {
    wholId: number
    state?: number
  }

  type getWholBatchDetailParams = {
    wbId: number
  }

  type getWholBatchesByWholesalerParams = {
    wholId: number
  }

  type getWholDetailParams = {
    wbId: number
  }

  type getWholesalersByAreaParams = {
    provId: number
    cityId: number
  }

  type getWholInfoByIdParams = {
    wholId: number
  }

  type listCityByProvIdParams = {
    provId: number
  }

  type LoginResponse = {
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

  type NodeInfo = {
    nodeId?: number
    code?: string
    password?: string
    name?: string
    type?: number
    provId?: number
    cityId?: number
    address?: string
    businessId?: string
    epId?: string
    eiaId?: string
    cirId?: string
    fbId?: string
    corporation?: string
    telephone?: string
    regDate?: string
    remarks?: string
    status?: number
    deleteTime?: string
    updateTime?: string
  }

  type NodeInfoDetailsDto = {
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

  type NodeInfoPageRequestDto = {
    pageNum?: number
    maxPageNum?: number
    name?: string
    type?: number
    provId?: number
    cityId?: number
    code?: string
    status?: number
  }

  type NodeInfoPageResponseDto = {
    totalRow?: number
    totalPageNum?: number
    currentPage?: number
    pageSize?: number
    data?: NodeInfoSimpleDto[]
  }

  type NodeInfoSimpleDto = {
    nodeId?: number
    code?: string
    name?: string
    type?: string
    province?: string
    city?: string
    regDate?: string
  }

  type NodeInfoTotalByMonthDto = {
    month?: string
    total?: number
  }

  type NodeInfoTotalByProvDto = {
    provId?: number
    provName?: string
    total?: number
  }

  type NodeInfoTotalByTypeDto = {
    type?: number
    total?: number
  }

  type NodeLoginRequest = {
    code?: string
    password?: string
  }

  type offlineFarmBatchParams = {
    fbId: number
  }

  type offlineRetaBatchParams = {
    rbId: number
  }

  type offlineSlauBatchParams = {
    sbId: number
  }

  type offlineWholBatchParams = {
    wbId: number
  }

  type permanentlyDeleteNodeInfoParams = {
    nodeId: number
  }

  type Province = {
    provId?: number
    provName?: string
    remarks?: string
  }

  type RefreshTokenRequest = {
    refreshToken?: string
  }

  type removeFarmBatchByIdParams = {
    fbId: number
  }

  type removeRetaBatchByIdParams = {
    rbId: number
  }

  type removeSlauBatchByIdParams = {
    sbId: number
  }

  type removeWholBatchByIdParams = {
    wbId: number
  }

  type resetPasswordParams = {
    nodeId: number
    newPassword: string
  }

  type ResetPasswordRequest = {
    adminId?: number
  }

  type ResetPasswordResponse = {
    adminId?: number
    adminName?: string
    newPassword?: string
  }

  type RetaBatch = {
    rbId?: number
    batchId?: string
    retaId?: number
    wbId?: number
    type?: string
    batchDate?: string
    sourceId?: string
    sourceQr?: string
    state?: number
    remarks?: string
  }

  type RetaBatchDetailDto = {
    batchId?: string
    wholProvince?: string
    wholCity?: string
    wholName?: string
    wholBatchId?: string
    wholProductType?: string
    type?: string
  }

  type RetaDetailsDto = {
    batchId?: string
    nodeName?: string
    nodeProvince?: string
    nodeCity?: string
    wholBatchId?: string
    type?: string
    batchDate?: string
    state?: string
  }

  type RetaNameDto = {
    rbId?: number
    nodeName?: string
    batchId?: string
    type?: string
    wbBatchId?: string
    batchDate?: string
    state?: number
  }

  type RetaStateDto = {
    rbId?: number
    batchId?: string
    type?: string
    wbBatchId?: string
    batchDate?: string
    state?: number
  }

  type sendRetaToWholConfirmParams = {
    rbId: number
  }

  type sendSlauToFarmConfirmParams = {
    sbId: number
  }

  type sendWholToSlauConfirmParams = {
    wbId: number
  }

  type SlauBatch = {
    sbId?: number
    batchId?: string
    slauId?: number
    fbId?: number
    type?: string
    quaId?: string
    testName?: string
    batchDate?: string
    state?: number
    remarks?: string
  }

  type SlauBatchDetailDto = {
    batchId?: string
    farmProvince?: string
    farmCity?: string
    farmName?: string
    farmBatchId?: string
    farmProductType?: string
    quaId?: string
    testName?: string
    type?: string
  }

  type SlauBatchSimpleDto = {
    sbId?: number
    batchId?: string
    type?: string
  }

  type SlauDetailsDto = {
    batchId?: string
    nodeName?: string
    nodeProvince?: string
    nodeCity?: string
    farmBatchId?: string
    quaId?: string
    testName?: string
    type?: string
    batchDate?: string
    state?: string
  }

  type SlaughterhouseDto = {
    nodeId?: number
    name?: string
  }

  type SlauNameDto = {
    sbId?: number
    nodeName?: string
    batchId?: string
    type?: string
    fbBatchId?: string
    batchDate?: string
    state?: number
  }

  type SlauStateDto = {
    sbId?: number
    batchId?: string
    type?: string
    fbBatchId?: string
    batchDate?: string
    state?: number
  }

  type TraceInfoDto = {
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

  type updatePasswordParams = {
    nodeId: number
    oldPassword: string
    newPassword: string
  }

  type WholBatch = {
    wbId?: number
    batchId?: string
    wholId?: number
    sbId?: number
    type?: string
    batchDate?: string
    state?: number
    remarks?: string
  }

  type WholBatchDetailDto = {
    batchId?: string
    slauProvince?: string
    slauCity?: string
    slauName?: string
    slauBatchId?: string
    slauProductType?: string
    type?: string
  }

  type WholBatchSimpleDto = {
    wbId?: number
    batchId?: string
    type?: string
  }

  type WholDetailsDto = {
    batchId?: string
    nodeName?: string
    nodeProvince?: string
    nodeCity?: string
    slauBatchId?: string
    type?: string
    batchDate?: string
    state?: string
  }

  type WholesalerDto = {
    nodeId?: number
    name?: string
  }

  type WholNameDto = {
    wbId?: number
    nodeName?: string
    batchId?: string
    type?: string
    sbBatchId?: string
    batchDate?: string
    state?: number
  }

  type WholStateDto = {
    wbId?: number
    batchId?: string
    type?: string
    sbBatchId?: string
    batchDate?: string
    state?: number
  }
}
