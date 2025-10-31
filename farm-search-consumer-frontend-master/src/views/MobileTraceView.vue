<template>
  <div class="mobile-wrapper">
    <div class="mobile-container">
      <div class="mobile-trace-container">
        <!-- 首页 - 扫码溯源 -->
        <div v-if="!traceInfo" class="mobile-scan-page">
          <div class="mobile-header">
            <h1 class="mobile-title">GALA肉类食品溯源平台</h1>
          </div>

          <div class="mobile-scan-section">
            <!-- 二维码区域 -->
            <div class="mobile-qr-section">
              <div class="mobile-qr-card">
                <div class="mobile-qr-placeholder">
                  <qrcode-vue v-if="showQRCode" :value="qrValue" :size="160" level="M" />
                  <div v-else class="mobile-qr-placeholder-text">
                    <scan-outlined style="font-size: 40px; color: #1890ff;" />
                    <p>扫描二维码</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 分割线 -->
            <div class="mobile-divider">
              <span>或</span>
            </div>

            <!-- 输入区域 -->
            <div class="mobile-input-section">
              <div class="mobile-input-group">
                <a-input
                  v-model:value="sourceId"
                  placeholder="请输入溯源标识码"
                  size="large"
                  class="mobile-source-input"
                  @press-enter="handleTrace"
                />
                <a-button
                  type="primary"
                  size="large"
                  class="mobile-trace-btn"
                  :loading="loading"
                  @click="handleTrace"
                  block
                >
                  {{ loading ? '查询中...' : '溯源' }}
                </a-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 溯源信息页面 -->
        <div v-else class="mobile-info-page">
          <div class="mobile-info-header">
            <a-button type="text" @click="handleBack" class="mobile-back-btn">
              <arrow-left-outlined />
              返回
            </a-button>
            <h2 class="mobile-info-title">肉类食品溯源信息</h2>
          </div>

          <div class="mobile-info-content">
            <!-- 养殖企业 -->
            <div class="mobile-info-card">
              <div class="mobile-card-header">
                <environment-outlined class="card-icon" />
                <span class="card-title">养殖企业</span>
              </div>
              <div class="mobile-card-content">
                <div class="info-row">
                  <span class="info-label">企业名称：</span>
                  <span class="info-value">{{ traceInfo.farmName || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产品品种：</span>
                  <span class="info-value">{{ traceInfo.farmType || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产品批号：</span>
                  <span class="info-value">{{ traceInfo.farmBatchId || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">动物检疫合格证：</span>
                  <span class="info-value">{{ traceInfo.farmAgcId || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产地：</span>
                  <span class="info-value">{{ getFarmLocation() }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">批号日期：</span>
                  <span class="info-value">{{ formatDate(traceInfo.farmBatchDate) }}</span>
                </div>
              </div>
            </div>

            <!-- 屠宰企业 -->
            <div class="mobile-info-card">
              <div class="mobile-card-header">
                <tool-outlined class="card-icon" />
                <span class="card-title">屠宰企业</span>
              </div>
              <div class="mobile-card-content">
                <div class="info-row">
                  <span class="info-label">企业名称：</span>
                  <span class="info-value">{{ traceInfo.slauName || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产品品种：</span>
                  <span class="info-value">{{ traceInfo.slauType || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产品批号：</span>
                  <span class="info-value">{{ traceInfo.slauBatchId || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">肉类检验合格证：</span>
                  <span class="info-value">{{ traceInfo.slauQuaId || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产地：</span>
                  <span class="info-value">{{ getSlauLocation() }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">批号日期：</span>
                  <span class="info-value">{{ formatDate(traceInfo.slauBatchDate) }}</span>
                </div>
              </div>
            </div>

            <!-- 批发商 -->
            <div class="mobile-info-card">
              <div class="mobile-card-header">
                <shop-outlined class="card-icon" />
                <span class="card-title">批发商</span>
              </div>
              <div class="mobile-card-content">
                <div class="info-row">
                  <span class="info-label">企业名称：</span>
                  <span class="info-value">{{ traceInfo.wholName || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产品品种：</span>
                  <span class="info-value">{{ traceInfo.wholType || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产品批号：</span>
                  <span class="info-value">{{ traceInfo.wholBatchId || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产地：</span>
                  <span class="info-value">{{ getWholLocation() }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">批号日期：</span>
                  <span class="info-value">{{ formatDate(traceInfo.wholBatchDate) }}</span>
                </div>
              </div>
            </div>

            <!-- 零售商 -->
            <div class="mobile-info-card">
              <div class="mobile-card-header">
                <shopping-outlined class="card-icon" />
                <span class="card-title">零售商</span>
              </div>
              <div class="mobile-card-content">
                <div class="info-row">
                  <span class="info-label">企业名称：</span>
                  <span class="info-value">{{ traceInfo.retaName || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产品品种：</span>
                  <span class="info-value">{{ traceInfo.retaType || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产品批号：</span>
                  <span class="info-value">{{ traceInfo.retaBatchId || '--' }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">产地：</span>
                  <span class="info-value">{{ getRetaLocation() }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">批号日期：</span>
                  <span class="info-value">{{ formatDate(traceInfo.retaBatchDate) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import {
  ArrowLeftOutlined,
  ScanOutlined,
  EnvironmentOutlined,
  ToolOutlined,
  ShopOutlined,
  ShoppingOutlined
} from '@ant-design/icons-vue'
import QrcodeVue from 'qrcode.vue'
import { getProductTraceInfo } from '@/api/consumerController'
import type { TraceInfoDto } from '@/types/api'

// 使用统一的类型
type TraceInfo = TraceInfoDto

const sourceId = ref('')
const traceInfo = ref<TraceInfo | null>(null)
const loading = ref(false)
const showQRCode = ref(false)
const qrValue = ref('https://trace.example.com')

const handleTrace = async () => {
  if (!sourceId.value.trim()) {
    message.warning('请输入溯源标识码')
    return
  }

  loading.value = true
  try {
    console.log('🔍 开始查询溯源信息，溯源码:', sourceId.value)

    const response = await getProductTraceInfo({
      sourceId: sourceId.value
    })

    console.log('📦 收到API响应:', response)

    if (response.data) {
      traceInfo.value = response.data
      message.success('溯源信息查询成功')
    } else {
      message.warning('未找到对应的溯源信息')
    }
  } catch (error: any) {
    console.error('❌ 溯源查询失败:', error)

    // 详细的错误处理
    if (error.response) {
      // 服务器返回错误状态码
      const status = error.response.status
      if (status === 404) {
        message.error('未找到溯源信息')
      } else if (status === 500) {
        message.error('服务器内部错误')
      } else {
        message.error(`请求失败: ${status}`)
      }
    } else if (error.request) {
      // 请求发送失败
      message.error('网络错误，请检查后端服务是否启动')
    } else {
      // 其他错误
      message.error('请求配置错误: ' + error.message)
    }
  } finally {
    loading.value = false
  }
}

const handleBack = () => {
  traceInfo.value = null
  sourceId.value = ''
}

const getFarmLocation = () => {
  const { farmProvince, farmCity } = traceInfo.value || {}
  if (farmProvince && farmCity) {
    return `${farmProvince} ${farmCity}`
  }
  return '--'
}

const getSlauLocation = () => {
  const { slauProvince, slauCity } = traceInfo.value || {}
  if (slauProvince && slauCity) {
    return `${slauProvince} ${slauCity}`
  }
  return '--'
}

const getWholLocation = () => {
  const { wholProvince, wholCity } = traceInfo.value || {}
  if (wholProvince && wholCity) {
    return `${wholProvince} ${wholCity}`
  }
  return '--'
}

const getRetaLocation = () => {
  const { retaProvince, retaCity } = traceInfo.value || {}
  if (retaProvince && retaCity) {
    return `${retaProvince} ${retaCity}`
  }
  return '--'
}

const formatDate = (dateStr?: string) => {
  if (!dateStr) return '--'
  try {
    return dateStr.split(' ')[0]
  } catch {
    return dateStr
  }
}

onMounted(() => {
  setTimeout(() => {
    showQRCode.value = true
  }, 500)
})
</script>

<style scoped>
.mobile-wrapper {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  background: #f0f2f5;
  padding: 20px;
}

.mobile-container {
  width: 100%;
  max-width: 414px;
  min-height: 736px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  position: relative;
}

.mobile-trace-container {
  min-height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 扫描页面样式 */
.mobile-scan-page {
  padding: 30px 20px;
  min-height: 100%;
  display: flex;
  flex-direction: column;
}

.mobile-header {
  text-align: center;
  margin-bottom: 40px;
  margin-top: 20px;
}

.mobile-title {
  color: white;
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.mobile-scan-section {
  background: white;
  border-radius: 16px;
  padding: 30px 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  flex: 1;
  display: flex;
  flex-direction: column;
}

.mobile-qr-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.mobile-qr-card {
  text-align: center;
}

.mobile-qr-placeholder {
  padding: 20px;
  border: 2px dashed #e8e8e8;
  border-radius: 12px;
  display: inline-block;
  background: #fafafa;
}

.mobile-qr-placeholder-text {
  color: #666;
  font-size: 14px;
}

.mobile-qr-placeholder-text p {
  margin: 8px 0 0 0;
}

.mobile-divider {
  text-align: center;
  margin: 30px 0;
  position: relative;
  color: #999;
  font-size: 14px;
}

.mobile-divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e8e8e8;
}

.mobile-divider span {
  background: white;
  padding: 0 15px;
}

.mobile-input-section {
  flex-shrink: 0;
}

.mobile-input-group {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.mobile-source-input {
  border-radius: 12px;
  height: 48px;
  font-size: 16px;
}

.mobile-trace-btn {
  border-radius: 12px;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
}

/* 信息页面样式 */
.mobile-info-page {
  min-height: 100%;
  background: #f5f5f5;
  display: flex;
  flex-direction: column;
}

.mobile-info-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.mobile-back-btn {
  color: white !important;
  padding: 4px 8px;
  flex-shrink: 0;
}

.mobile-info-title {
  color: white;
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  flex: 1;
  text-align: center;
  padding-right: 40px;
}

.mobile-info-content {
  flex: 1;
  padding: 16px;
  padding-bottom: 20px;
  overflow-y: auto;
}

.mobile-info-card {
  background: white;
  border-radius: 12px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.mobile-card-header {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.card-icon {
  color: #667eea;
  font-size: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.mobile-card-content {
  padding: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  color: #666;
  font-size: 14px;
  flex-shrink: 0;
  margin-right: 12px;
  min-width: 120px;
}

.info-value {
  color: #333;
  font-size: 14px;
  font-weight: 500;
  text-align: right;
  flex: 1;
  word-break: break-all;
}

/* 响应式调整 */
@media (max-width: 320px) {
  .mobile-wrapper {
    padding: 10px;
  }

  .mobile-container {
    min-height: 568px;
  }

  .mobile-scan-page {
    padding: 20px 16px;
  }

  .mobile-title {
    font-size: 18px;
  }

  .info-label {
    min-width: 110px;
    font-size: 13px;
  }

  .info-value {
    font-size: 13px;
  }
}

/* 防止iOS输入框缩放 */
:deep(input) {
  font-size: 16px !important;
}

/* 滚动条样式 */
.mobile-info-content::-webkit-scrollbar {
  width: 4px;
}

.mobile-info-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 2px;
}

.mobile-info-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.mobile-info-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
