<template>
  <div class="mobile-wrapper">
    <div class="mobile-container">
      <div class="create-batch-container">
        <!-- 头部 -->
        <div class="header-section">
          <a-button type="text" class="back-btn" @click="goBack">
            <arrow-left-outlined />
          </a-button>
          <h1 class="page-title">新建产品批号</h1>
        </div>

        <!-- 表单内容 -->
        <div class="form-content">
          <a-form
              ref="formRef"
              :model="formState"
              :rules="rules"
              layout="vertical"
          >
            <!-- 产品批号 -->
            <a-form-item label="产品批号" name="batchId" required>
              <a-input
                  v-model:value="formState.batchId"
                  placeholder="请输入产品批号"
                  :maxlength="50"
                  allow-clear
              />
            </a-form-item>

            <!-- 产品品种 -->
            <a-form-item label="产品品种" name="type" required>
              <a-auto-complete
                  v-model:value="formState.type"
                  :options="productTypeOptions"
                  placeholder="请选择或输入产品品种"
                  allow-clear
                  @select="onProductTypeSelect"
                  @search="onProductTypeSearch"
              >
                <template #option="item">
                  <div class="product-type-option">
                    <span>{{ item.label }}</span>
                    <span v-if="item.isCustom" class="custom-tag">自定义</span>
                  </div>
                </template>
              </a-auto-complete>
              <div class="form-tip">可从下拉列表选择或直接输入产品品种</div>
            </a-form-item>

            <!-- 批发商区域选择 -->
            <a-form-item label="批发商区域" required>
              <div class="area-selector">
                <a-form-item name="provId" class="area-item">
                  <a-select
                      v-model:value="formState.provId"
                      placeholder="选择省份"
                      :options="provinceOptions"
                      @change="handleProvinceChange"
                      allow-clear
                      :loading="loadingProvinces"
                  />
                </a-form-item>
                <a-form-item name="cityId" class="area-item">
                  <a-select
                      v-model:value="formState.cityId"
                      placeholder="选择城市"
                      :options="cityOptions"
                      :disabled="!formState.provId"
                      @change="handleCityChange"
                      allow-clear
                      :loading="loadingCities"
                  />
                </a-form-item>
              </div>
            </a-form-item>

            <!-- 批发商选择 -->
            <a-form-item label="批发商" name="wholId" required>
              <a-select
                  v-model:value="formState.wholId"
                  placeholder="请选择批发商"
                  :options="wholesalerOptions"
                  :disabled="!formState.cityId"
                  @change="handleWholesalerChange"
                  allow-clear
                  show-search
                  :filter-option="filterWholesalerOption"
                  :loading="loadingWholesalers"
              />
            </a-form-item>

            <!-- 批发商产品批号 -->
            <a-form-item label="批发商产品批号" name="wbId" required>
              <a-select
                  v-model:value="formState.wbId"
                  placeholder="请选择批发商产品批号"
                  :options="wholBatchOptions"
                  :disabled="!formState.wholId"
                  allow-clear
                  show-search
                  :filter-option="filterBatchOption"
                  :loading="loadingBatches"
              >
                <template #option="item">
                  <div class="batch-option">
                    <div class="batch-id">{{ item.batchId }}</div>
                    <div class="batch-type">{{ item.type }}</div>
                  </div>
                </template>
              </a-select>
            </a-form-item>

            <!-- 备注 -->
            <a-form-item label="备注" name="remarks">
              <a-textarea
                  v-model:value="formState.remarks"
                  placeholder="请输入备注信息"
                  :rows="3"
                  :maxlength="500"
                  show-count
                  allow-clear
              />
            </a-form-item>
          </a-form>
        </div>

        <!-- 底部操作按钮 -->
        <div class="action-buttons">
          <a-button
              type="primary"
              size="large"
              :loading="submitting"
              @click="handleSubmit"
              :disabled="!isFormValid"
              block
          >
            {{ submitting ? '提交中...' : '创建产品批号' }}
          </a-button>
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
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message, type FormInstance } from 'ant-design-vue'
import type { Rule } from 'ant-design-vue/es/form'
import {
  ArrowLeftOutlined,
  HomeOutlined,
  UserOutlined
} from '@ant-design/icons-vue'
import { useAuthStore } from '@/stores/auth'
import {
  createRetaBatch,
  getWholesalersByArea,
  getWholBatchesByWholesaler
} from '@/api/retaBatchController'
import type {
  CreateRetaBatchRequestDto,
  BaseResponseInteger,
  WholesalerDto,
  WholBatchSimpleDto
} from '@/types/api.ts'
import { listProvinceAll } from '@/api/provinceController'
import { listCityByProvId } from '@/api/cityController'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref<FormInstance>()

// 表单状态
const formState = reactive({
  batchId: '',
  type: undefined as string | undefined,
  provId: undefined as number | undefined,
  cityId: undefined as number | undefined,
  wholId: undefined as number | undefined,
  wbId: undefined as number | undefined,
  remarks: ''
})

// 状态管理
const submitting = ref(false)
const loadingProvinces = ref(false)
const loadingCities = ref(false)
const loadingWholesalers = ref(false)
const loadingBatches = ref(false)

const provinceOptions = ref<{ value: number; label: string }[]>([])
const cityOptions = ref<{ value: number; label: string }[]>([])
const wholesalerOptions = ref<{ value: number; label: string }[]>([])
const wholBatchOptions = ref<{ value: number; label: string; batchId: string; type: string }[]>([])

// 产品品种选项 - 零售商主要产品
const baseProductTypes = [
  '白条猪',
  '分割猪肉',
  '冷鲜肉',
  '冷冻肉',
  '猪副产品',
  '牛肉',
  '羊肉',
  '禽肉',
  '其他肉类'
]

const productTypeOptions = ref(baseProductTypes.map(type => ({
  value: type,
  label: type,
  isCustom: false
})))

// 计算属性：表单是否有效
const isFormValid = computed(() => {
  return formState.batchId &&
      formState.type &&
      formState.wbId
})

// 表单验证规则
const rules: Record<string, Rule[]> = {
  batchId: [
    { required: true, message: '请输入产品批号', trigger: 'blur' },
    { min: 1, max: 50, message: '产品批号长度应在1-50个字符之间', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择或输入产品品种', trigger: 'change' }
  ],
  wholId: [
    { required: true, message: '请选择批发商', trigger: 'change' }
  ],
  wbId: [
    { required: true, message: '请选择批发商产品批号', trigger: 'change' }
  ],
  remarks: [
    { max: 500, message: '备注不能超过500个字符', trigger: 'blur' }
  ]
}

// 产品品种搜索处理
const onProductTypeSearch = (searchText: string) => {
  if (!searchText) {
    productTypeOptions.value = baseProductTypes.map(type => ({
      value: type,
      label: type,
      isCustom: false
    }))
    return
  }

  const filteredOptions = baseProductTypes.filter(type =>
      type.includes(searchText)
  ).map(type => ({
    value: type,
    label: type,
    isCustom: false
  }))

  if (searchText && !baseProductTypes.includes(searchText)) {
    filteredOptions.push({
      value: searchText,
      label: searchText,
      isCustom: true
    })
  }

  productTypeOptions.value = filteredOptions
}

// 产品品种选择处理
const onProductTypeSelect = (value: string, option: any) => {
  console.log('选择了产品品种:', value, option)
}

// 省份选择变化
const handleProvinceChange = async (value: number) => {
  formState.cityId = undefined
  formState.wholId = undefined
  formState.wbId = undefined
  cityOptions.value = []
  wholesalerOptions.value = []
  wholBatchOptions.value = []

  if (value) {
    await loadCitiesByProvince(value)
  }
}

// 加载省份数据
const loadProvinces = async () => {
  try {
    loadingProvinces.value = true
    const response = await listProvinceAll()

    if (response.code === 0 && response.data) {
      provinceOptions.value = response.data.map(province => ({
        value: province.provId!,
        label: province.provName!
      }))
    } else {
      message.error(response.message || '加载省份数据失败')
    }
  } catch (error) {
    console.error('加载省份数据失败:', error)
    message.error('加载省份数据失败')
  } finally {
    loadingProvinces.value = false
  }
}

// 根据省份加载城市数据
const loadCitiesByProvince = async (provId: number) => {
  try {
    loadingCities.value = true
    const response = await listCityByProvId({ provId })

    if (response.code === 0 && response.data) {
      cityOptions.value = response.data.map(city => ({
        value: city.cityId!,
        label: city.cityName!
      }))
    } else {
      message.error(response.message || '加载城市数据失败')
    }
  } catch (error) {
    console.error('加载城市数据失败:', error)
    message.error('加载城市数据失败')
  } finally {
    loadingCities.value = false
  }
}

// 城市选择变化
const handleCityChange = async (value: number) => {
  formState.wholId = undefined
  formState.wbId = undefined
  wholesalerOptions.value = []
  wholBatchOptions.value = []

  if (value && formState.provId) {
    await loadWholesalersByArea()
  }
}

// 根据区域加载批发商
const loadWholesalersByArea = async () => {
  if (!formState.provId || !formState.cityId) return

  try {
    loadingWholesalers.value = true
    const response = await getWholesalersByArea({
      provId: formState.provId,
      cityId: formState.cityId
    })

    if (response.code === 0 && response.data) {
      wholesalerOptions.value = response.data.map(wholesaler => ({
        value: wholesaler.nodeId!,
        label: wholesaler.name!
      }))
    } else {
      message.error(response.message || '加载批发商失败')
    }
  } catch (error) {
    console.error('加载批发商失败:', error)
    message.error('加载批发商失败')
  } finally {
    loadingWholesalers.value = false
  }
}

// 批发商选择变化
const handleWholesalerChange = async (value: number) => {
  formState.wbId = undefined
  wholBatchOptions.value = []

  if (value) {
    await loadWholBatches(value)
  }
}

// 加载批发商产品批号
const loadWholBatches = async (wholId: number) => {
  try {
    loadingBatches.value = true
    const response = await getWholBatchesByWholesaler({ wholId })

    if (response.code === 0 && response.data) {
      wholBatchOptions.value = response.data.map(batch => ({
        value: batch.wbId!,
        label: `${batch.batchId} - ${batch.type}`,
        batchId: batch.batchId!,
        type: batch.type!
      }))
    } else {
      message.error(response.message || '加载批发商产品批号失败')
    }
  } catch (error) {
    console.error('加载批发商产品批号失败:', error)
    message.error('加载批发商产品批号失败')
  } finally {
    loadingBatches.value = false
  }
}

// 批发商搜索过滤
const filterWholesalerOption = (input: string, option: any) => {
  return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
}

// 产品批号搜索过滤
const filterBatchOption = (input: string, option: any) => {
  return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
}

// 处理表单提交
const handleSubmit = async () => {
  try {
    await formRef.value?.validate()

    submitting.value = true

    const submitData: CreateRetaBatchRequestDto = {
      batchId: formState.batchId.trim(),
      retaId: authStore.userInfo?.userId!, // 使用当前登录用户的ID作为零售商ID
      wbId: formState.wbId!,
      type: formState.type!,
      remarks: formState.remarks || undefined
    }

    const response: BaseResponseInteger = await createRetaBatch(submitData)

    if (response.code === 0 && response.data && response.data > 0) {
      message.success('产品批号创建成功')
      formRef.value?.resetFields()
      setTimeout(() => {
        router.push('/node/reta/home')
      }, 1500)
    } else {
      message.error(response.message || '创建失败')
    }
  } catch (error: any) {
    if (error.errorFields) {
      message.error('请完善表单信息')
    } else {
      console.error('创建产品批号失败:', error)
      message.error(error.message || '创建失败，请重试')
    }
  } finally {
    submitting.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.push('/node/reta/home')
}

// 跳转到个人页面
const navigateToProfile = () => {
  router.push('/profile')
}

// 在 onMounted 中调用初始化
onMounted(() => {
  loadProvinces()
})
</script>

<style scoped>
/* 复用之前的样式，添加区域选择器样式 */
.area-selector {
  display: flex;
  gap: 12px;
}

.area-item {
  flex: 1;
  margin-bottom: 0;
}

.batch-option {
  display: flex;
  flex-direction: column;
}

.batch-id {
  font-weight: 500;
}

.batch-type {
  font-size: 12px;
  color: #666;
}

/* 其他样式与屠宰企业创建页面相同 */
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

.create-batch-container {
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
  margin-right: 40px; /* 平衡返回按钮的宽度 */
}

.form-content {
  flex: 1;
  padding: 24px 20px;
  overflow-y: auto;
}

:deep(.ant-form-item-label > label) {
  font-weight: 500;
  color: #333;
}

:deep(.ant-input),
:deep(.ant-select .ant-select-selector),
:deep(.ant-picker) {
  border-radius: 8px;
  border: 1px solid #d9d9d9;
}

:deep(.ant-input:focus),
:deep(.ant-select-focused .ant-select-selector),
:deep(.ant-picker-focused) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

:deep(.ant-form-item) {
  margin-bottom: 20px;
}

.form-tip {
  color: #999;
  font-size: 12px;
  margin-top: 4px;
}

.product-type-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.custom-tag {
  background: #f0f2f5;
  color: #666;
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 4px;
}

.action-buttons {
  padding: 16px 20px;
  background: white;
  border-top: 1px solid #f0f0f0;
  flex-shrink: 0;
}

:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
}

:deep(.ant-btn-primary:disabled) {
  background: #f5f5f5;
  color: #bfbfbf;
  border-color: #d9d9d9;
}

:deep(.ant-btn-primary:not(:disabled):hover) {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
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

.nav-item.active {
  color: #667eea;
}

.nav-item:hover {
  color: #667eea;
}

.nav-item span {
  font-size: 12px;
  margin-top: 4px;
}

/* 响应式调整 */
@media (max-width: 320px) {
  .mobile-container {
    height: 568px;
  }

  .form-content {
    padding: 20px 16px;
  }

  .header-section {
    padding: 14px 16px;
  }

  .page-title {
    font-size: 16px;
  }
}

/* 滚动条样式 */
.form-content::-webkit-scrollbar {
  width: 4px;
}

.form-content::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.form-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.form-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 添加加载状态样式 */
:deep(.ant-select .ant-select-selection-placeholder) {
  color: #999;
}

:deep(.ant-select.ant-select-loading .ant-select-selection-placeholder) {
  color: #1890ff;
}
</style>
