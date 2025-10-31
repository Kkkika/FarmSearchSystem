import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { LoginResponse, NodeInfo } from '@/types/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>('')
  const refreshToken = ref<string>('')
  const userInfo = ref<LoginResponse | null>(null)
  const isLoggedIn = ref<boolean>(false)

  // 登录
  const login = (loginData: LoginResponse) => {
    token.value = loginData.accessToken || ''
    refreshToken.value = loginData.refreshToken || ''
    userInfo.value = loginData
    isLoggedIn.value = true

    // 存储到 localStorage
    localStorage.setItem('accessToken', token.value)
    localStorage.setItem('refreshToken', refreshToken.value)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  // 登出
  const logout = () => {
    token.value = ''
    refreshToken.value = ''
    userInfo.value = null
    isLoggedIn.value = false

    // 清除 localStorage
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('userInfo')
  }

  // 初始化状态
  const initAuth = () => {
    const storedToken = localStorage.getItem('accessToken')
    const storedUserInfo = localStorage.getItem('userInfo')

    if (storedToken && storedUserInfo) {
      token.value = storedToken
      userInfo.value = JSON.parse(storedUserInfo)
      isLoggedIn.value = true
    }
  }

  // 获取节点企业信息（从用户信息中提取）
  const getNodeInfo = (): NodeInfo => {
    if (!userInfo.value) {
      return {} as NodeInfo
    }

    return {
      nodeId: userInfo.value.userId,
      code: userInfo.value.username,
      name: userInfo.value.username, // 这里可能需要根据实际情况调整
      type: userInfo.value.enterpriseType
    } as NodeInfo
  }

  // 获取企业类型名称
  const getEnterpriseTypeName = (type?: number): string => {
    switch (type) {
      case 1: return '养殖企业'
      case 2: return '屠宰企业'
      case 3: return '批发企业'
      case 4: return '零售企业'
      default: return '未知类型'
    }
  }

  // 获取企业显示名称
  const getEnterpriseDisplayName = (): string => {
    return userInfo.value?.username || '未知企业'
  }

  // 获取当前企业类型
  const getCurrentEnterpriseType = (): number | undefined => {
    return userInfo.value?.enterpriseType
  }

  // 获取企业类型名称
  const getCurrentEnterpriseTypeName = (): string => {
    return getEnterpriseTypeName(userInfo.value?.enterpriseType)
  }

  // 获取节点ID（用户ID就是节点ID）
  const getNodeId = (): number | undefined => {
    return userInfo.value?.userId
  }

  return {
    token,
    refreshToken,
    userInfo,
    isLoggedIn,
    login,
    logout,
    initAuth,
    getNodeInfo,
    getEnterpriseTypeName,
    getEnterpriseDisplayName,
    getCurrentEnterpriseType,
    getCurrentEnterpriseTypeName,
    getNodeId
  }
})
