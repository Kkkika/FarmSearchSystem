<template>
  <div class="mobile-wrapper">
    <div class="mobile-container">
      <div class="batch-management-container">
        <!-- 头部 -->
        <div class="header-section">
          <a-button type="text" class="back-btn" @click="goBack">
            <arrow-left-outlined />
          </a-button>
          <h1 class="page-title">产品批号管理</h1>
        </div>

        <!-- 状态筛选 -->
        <div class="filter-section">
          <div class="status-tabs">
            <div
              class="status-tab"
              :class="{ active: activeStatus === undefined }"
              @click="handleStatusChange(undefined)"
            >
              全部
            </div>
            <div
              class="status-tab"
              :class="{ active: activeStatus === 1 }"
              @click="handleStatusChange(1)"
            >
              待发布
            </div>
            <div
              class="status-tab"
              :class="{ active: activeStatus === 2 }"
              @click="handleStatusChange(2)"
            >
              已发布
            </div>
          </div>
        </div>

        <!-- 批次列表 -->
        <div class="batch-list">
          <a-spin :spinning="loading">
            <div v-if="batchList.length === 0" class="empty-state">
              <div class="empty-icon">📭</div>
              <p>暂无产品批号数据</p>
            </div>

            <div v-else class="batch-items">
              <div
                v-for="batch in batchList"
                :key="batch.fbId"
                class="batch-item"
                @click="handleViewDetail(batch)"
              >
                <div class="batch-header">
                  <span class="batch-number">产品批号：{{ batch.batchId }}</span>
                  <span class="batch-status" :class="getStatusClass(batch.state)">
                    {{ getStatusText(batch.state) }}
                  </span>
                </div>

                <div class="batch-content">
                  <!-- 修改批次信息展示部分 -->
                  <div class="batch-info">
                    <div class="info-item">
                      <span class="label">品种：</span>
                      <span class="value">{{ batch.type || '-' }}</span>
                    </div>
                    <div class="info-item">
                      <span class="label">创建时间：</span>
                      <span class="value">{{ formatDate(batch.batchDate) }}</span>
                    </div>
                  </div>

                  <!-- 操作按钮 - 根据状态显示不同按钮 -->
                  <div class="batch-actions">
                    <!-- 待发布状态显示更新和删除按钮 -->
                    <template v-if="batch.state === 1">
                      <a-button
                        type="link"
                        size="small"
                        class="action-btn update-btn"
                        @click="handleUpdate(batch)"
                      >
                        更新
                      </a-button>
                      <a-button
                        type="link"
                        size="small"
                        class="action-btn delete-btn"
                        @click="handleDelete(batch)"
                      >
                        删除
                      </a-button>
                    </template>

                    <!-- 已发布状态显示下架按钮 -->
                    <template v-else-if="batch.state === 2">
                      <a-button
                        type="link"
                        size="small"
                        class="action-btn offline-btn"
                        @click="handleOffline(batch)"
                      >
                        下架
                      </a-button>
                    </template>
                  </div>
                </div>
              </div>
            </div>
          </a-spin>
        </div>

        <!-- 底部导航 -->
        <div class="bottom-nav">
          <div class="nav-item" @click="goBack">
            <home-outlined />
            <span>首页</span>
          </div>
          <div class="nav-item" @click="navigateToProfile">
            <user-outlined />
            <span>我的</span>
          </div>
        </div>

        <!-- 详情弹窗内容 -->
        <a-modal
          v-model:open="detailModalVisible"
          title="产品批号详情"
          width="90%"
          :footer="null"
          centered
          :after-close="handleModalClose"
        >
          <a-spin :spinning="detailLoading">
            <div v-if="currentBatchDetail" class="detail-content">
              <!-- 基本信息 -->
              <div class="detail-section">
                <h3 class="section-title">产品批号信息</h3>
                <div class="detail-grid">
                  <div class="detail-item">
                    <span class="detail-label">产品批号：</span>
                    <span class="detail-value">{{ currentBatchDetail.batchId || '-' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">养殖企业：</span>
                    <span class="detail-value">{{ currentBatchDetail.nodeName || '-' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">产品品种：</span>
                    <span class="detail-value">{{ currentBatchDetail.type || '-' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">批号状态：</span>
                    <span
                      class="detail-value"
                      :class="getDetailStatusClass(currentBatchDetail.state)"
                    >
                      {{ getDetailStatusText(currentBatchDetail.state) }}
                    </span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">检疫证号：</span>
                    <span class="detail-value">{{ currentBatchDetail.agcId || '暂无' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">检疫员：</span>
                    <span class="detail-value">{{ currentBatchDetail.testName || '暂无' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">批号时间：</span>
                    <span class="detail-value">{{ formatDate(currentBatchDetail.batchDate) }}</span>
                  </div>
                </div>
              </div>

              <!-- 备注信息 -->
              <div v-if="currentBatchDetail.remarks" class="detail-section">
                <h3 class="section-title">备注信息</h3>
                <div class="remarks-content">
                  {{ currentBatchDetail.remarks }}
                </div>
              </div>
            </div>

            <div v-else class="empty-detail">
              <div class="empty-icon">❌</div>
              <p>加载详情失败</p>
            </div>
          </a-spin>
        </a-modal>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import { ArrowLeftOutlined, HomeOutlined, UserOutlined } from '@ant-design/icons-vue'
import { useAuthStore } from '@/stores/auth'
import type {
  FarmStateDto,
  BaseResponseListFarmStateDto,
  BaseResponseFarmDetailsDto,
  FarmDetailsDto,
} from '@/types/api.ts'
import { getFarmBatchByState, getFarmBatchDetail, removeFarmBatchById, offlineFarmBatch } from '@/api/farmBatchController'
const router = useRouter()
const authStore = useAuthStore()

// 状态管理
const loading = ref(false)
const activeStatus = ref<number | undefined>(undefined)
const batchList = ref<FarmStateDto[]>([])
// 添加详情弹窗相关状态
const detailModalVisible = ref(false)
const detailLoading = ref(false)
const currentBatchDetail = ref<FarmDetailsDto | null>(null)

// 详情状态文本映射 - 处理字符串状态
const detailStatusMap = {
  '1': { text: '待发布', class: 'status-pending' },
  '2': { text: '已发布', class: 'status-published' },
  待发布: { text: '待发布', class: 'status-pending' },
  已发布: { text: '已发布', class: 'status-published' },
}

// 获取详情状态文本
const getDetailStatusText = (state: string | number | undefined) => {
  if (!state) return '未知'
  const stateStr = String(state)
  return detailStatusMap[stateStr as keyof typeof detailStatusMap]?.text || '未知'
}

// 获取详情状态样式类
const getDetailStatusClass = (state: string | number | undefined) => {
  if (!state) return 'status-unknown'
  const stateStr = String(state)
  return detailStatusMap[stateStr as keyof typeof detailStatusMap]?.class || 'status-unknown'
}

// 状态文本映射 - 修正为养殖企业状态
const statusMap = {
  1: { text: '待发布', class: 'status-pending' },
  2: { text: '已发布', class: 'status-published' },
  '1': { text: '待发布', class: 'status-pending' },
  '2': { text: '已发布', class: 'status-published' },
  待发布: { text: '待发布', class: 'status-pending' },
  已发布: { text: '已发布', class: 'status-published' },
}
// 计算属性：当前筛选文本
const currentFilterText = computed(() => {
  if (activeStatus.value === undefined) return '全部'
  if (activeStatus.value === 1) return '待发布'
  if (activeStatus.value === 2) return '已发布'
  return '全部'
})

// 状态变更处理
const handleStatusChange = (status: number | undefined) => {
  activeStatus.value = status
  loadBatchData()
}

// 获取状态文本
const getStatusText = (state: number | string | undefined) => {
  if (!state) return '未知'
  const stateStr = String(state)
  return statusMap[stateStr as keyof typeof statusMap]?.text || '未知'
}

// 获取状态样式类
const getStatusClass = (state: number | string | undefined) => {
  if (!state) return 'status-unknown'
  const stateStr = String(state)
  return statusMap[stateStr as keyof typeof statusMap]?.class || 'status-unknown'
}

// 格式化日期
// 修改时间格式化函数
const formatDate = (dateString: string | undefined) => {
  if (!dateString) return '-'

  try {
    // 直接解析日期字符串，格式为 "YYYY-MM-DD HH:mm:ss"
    const date = new Date(dateString)

    // 检查日期是否有效
    if (isNaN(date.getTime())) {
      return '-'
    }

    // 格式化为 YYYY-MM-DD
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')

    return `${year}-${month}-${day}`
  } catch (error) {
    console.error('日期格式化错误:', error)
    return '-'
  }
}

// 加载批次数据
const loadBatchData = async () => {
  try {
    loading.value = true
    const currentUserId = authStore.userInfo?.userId!

    const response: BaseResponseListFarmStateDto = await getFarmBatchByState({
      farmId: currentUserId,
      state: activeStatus.value,
    })

    if (response.code === 0 && response.data) {
      batchList.value = response.data
    } else {
      message.error(response.message || '加载数据失败')
      batchList.value = []
    }
  } catch (error) {
    console.error('加载产品批号数据失败:', error)
    message.error('加载数据失败，请重试')
    batchList.value = []
  } finally {
    loading.value = false
  }
}

// 更新操作
const handleUpdate = (batch: FarmStateDto) => {
  if (!batch.fbId) {
    message.error('批次ID不存在')
    return
  }
  // 跳转到更新页面
  router.push(`/farm/batch-update/${batch.fbId}`)
}

// 删除操作
const handleDelete = async (batch: FarmStateDto) => {
  // 在事件处理中使用 stopPropagation 阻止事件冒泡
  event?.stopPropagation()
  if (!batch.fbId) {
    message.error('批次ID不存在')
    return
  }

  try {
    // 使用Modal的标准方式，不使用HTML字符串
    Modal.confirm({
      title: '删除确认',
      content: `确定要删除产品批号 "${batch.batchId}" 吗？此操作不可恢复。`,
      okText: '确认删除',
      cancelText: '取消',
      okType: 'danger',
      centered: true,
      onOk: async () => {
        try {
          // 显示加载中
          const hideLoading = message.loading('删除中...', 0)

          const response = await removeFarmBatchById({ fbId: batch.fbId })

          hideLoading()

          if (response.code === 0) {
            message.success('删除成功')
            // 重新加载数据
            loadBatchData()
          } else {
            message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除产品批号失败:', error)
          message.error('删除失败，请重试')
        }
      },
    })
  } catch (error) {
    console.error('删除操作异常:', error)
  }
}

// 查看详情函数
const handleViewDetail = async (batch: FarmStateDto) => {
  if (!batch.fbId) {
    message.error('批次ID不存在')
    return
  }

  try {
    detailLoading.value = true
    detailModalVisible.value = true

    const response: BaseResponseFarmDetailsDto = await getFarmBatchDetail({ fbId: batch.fbId })

    if (response.code === 0 && response.data) {
      currentBatchDetail.value = response.data
    } else {
      message.error(response.message || '加载详情失败')
      currentBatchDetail.value = null
    }
  } catch (error) {
    console.error('加载批次详情失败:', error)
    message.error('加载详情失败，请重试')
    currentBatchDetail.value = null
  } finally {
    detailLoading.value = false
  }
}

// 弹窗关闭处理
const handleModalClose = () => {
  currentBatchDetail.value = null
}

// 下架操作
// 下架操作
const handleOffline = async (batch: FarmStateDto) => {
  // 阻止事件冒泡
  event?.stopPropagation()
  if (!batch.fbId) {
    message.error('批次ID不存在')
    return
  }

  try {
    Modal.confirm({
      title: '下架确认',
      content: `确定要下架产品批号 "${batch.batchId}" 吗？下架后该批号将不再显示在已发布列表中。`,
      okText: '确认下架',
      cancelText: '取消',
      okType: 'danger',
      centered: true,
      onOk: async () => {
        try {
          // 显示加载中
          const hideLoading = message.loading('下架中...', 0)

          const response = await offlineFarmBatch({ fbId: batch.fbId! })

          hideLoading()

          if (response.code === 0) {
            message.success('下架成功')
            // 重新加载数据
            loadBatchData()
            // 如果当前正在查看详情，关闭弹窗
            if (detailModalVisible.value) {
              detailModalVisible.value = false
            }
          } else {
            message.error(response.message || '下架失败')
          }
        } catch (error) {
          console.error('下架产品批号失败:', error)
          message.error('下架失败，请重试')
        }
      }
    })
  } catch (error) {
    console.error('下架操作异常:', error)
  }
}

// 返回首页
const goBack = () => {
  router.push('/node/farm/home')
}

// 跳转到个人页面
const navigateToProfile = () => {
  router.push('/profile')
}

// 页面加载时初始化数据
onMounted(() => {
  loadBatchData()
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
  height: 100vh;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  position: relative;
}

.batch-management-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 16px 20px;
  color: white;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.back-btn {
  color: white;
  border: none;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: white;
  flex: 1;
  text-align: center;
  margin-right: 40px;
}

.filter-section {
  padding: 16px 20px;
  background: white;
  border-bottom: 1px solid #f0f0f0;
  flex-shrink: 0;
}

.status-tabs {
  display: flex;
  background: #f5f5f5;
  border-radius: 8px;
  padding: 4px;
}

.status-tab {
  flex: 1;
  text-align: center;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 500;
}

.status-tab.active {
  background: white;
  color: #667eea;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.batch-list {
  flex: 1;
  overflow-y: auto;
  padding: 0 20px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.batch-items {
  padding: 16px 0;
}

.batch-item {
  background: white;
  border: 1px solid #f0f0f0;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  transition: all 0.3s ease;
}

.batch-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

.batch-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.batch-number {
  font-weight: 600;
  color: #333;
  font-size: 16px;
}

.batch-status {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 12px;
  font-weight: 500;
}

.status-pending {
  background: #fff7e6;
  color: #fa8c16;
}

.status-published {
  background: #f6ffed;
  color: #52c41a;
}

.status-unknown {
  background: #f5f5f5;
  color: #999;
}

.batch-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.batch-info {
  flex: 1;
}

.info-item {
  margin-bottom: 6px;
  display: flex;
  align-items: center;
}

.info-item:last-child {
  margin-bottom: 0;
}

.label {
  color: #666;
  font-size: 14px;
  min-width: 70px;
}

.value {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.batch-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 4px 8px;
  height: auto;
  font-size: 12px;
}

.update-btn {
  color: #1890ff;
}

.update-btn:hover {
  color: #40a9ff;
}

.delete-btn {
  color: #ff4d4f;
}

.delete-btn:hover {
  color: #ff7875;
}

.offline-btn {
  color: #fa8c16;
}

.offline-btn:hover {
  color: #ffa940;
}

.bottom-nav {
  display: flex;
  background: #fafafa;
  border-top: 1px solid #f0f0f0;
  padding: 12px 0;
  flex-shrink: 0;
}

.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px 0;
  cursor: pointer;
  transition: color 0.3s ease;
  color: #666;
}

.nav-item:hover {
  color: #667eea;
}

.nav-item span {
  font-size: 12px;
  margin-top: 4px;
}

/* 滚动条样式 */
.batch-list::-webkit-scrollbar {
  width: 4px;
}

.batch-list::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.batch-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.batch-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 添加详情弹窗样式 */
.detail-content {
  padding: 8px 0;
}

.detail-section {
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.detail-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-item {
  display: flex;
  align-items: flex-start;
  line-height: 1.5;
}

.detail-label {
  color: #666;
  font-size: 14px;
  min-width: 80px;
  flex-shrink: 0;
}

.detail-value {
  color: #333;
  font-size: 14px;
  font-weight: 500;
  flex: 1;
  word-break: break-all;
}

.remarks-content {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 6px;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

.empty-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #999;
}

.empty-detail .empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

/* 响应式调整 */
@media (max-width: 320px) {
  .detail-label {
    min-width: 70px;
    font-size: 13px;
  }

  .detail-value {
    font-size: 13px;
  }
}

/* 响应式调整 */
@media (max-width: 320px) {
  .mobile-container {
    height: 568px;
  }

  .batch-list {
    padding: 0 16px;
  }

  .header-section {
    padding: 14px 16px;
  }

  .page-title {
    font-size: 16px;
  }
}
</style>
