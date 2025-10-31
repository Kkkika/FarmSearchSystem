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

            <!-- 动物检疫合格证编号 -->
            <a-form-item label="动物检疫合格证编号" name="agcId">
              <a-input
                  v-model:value="formState.agcId"
                  placeholder="请输入动物检疫合格证编号"
                  :maxlength="100"
                  allow-clear
              />
            </a-form-item>

            <!-- 官方检疫员名称 -->
            <a-form-item label="官方检疫员名称" name="testName">
              <a-input
                  v-model:value="formState.testName"
                  placeholder="请输入官方检疫员名称"
                  :maxlength="50"
                  allow-clear
              />
            </a-form-item>

            <!-- 批号录入日期 -->
            <a-form-item label="批号录入日期" name="batchDate">
              <a-date-picker
                  v-model:value="formState.batchDate"
                  placeholder="请选择批号录入日期"
                  style="width: 100%"
                  value-format="YYYY-MM-DD"
              />
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
              :disabled="!formState.batchId || !formState.type"
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message, type FormInstance } from 'ant-design-vue'
import type { Rule } from 'ant-design-vue/es/form'
import {
  ArrowLeftOutlined,
  HomeOutlined,
  UserOutlined
} from '@ant-design/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { saveFarmBatch } from '@/api/farmBatchController'
import type { FarmBatch, BaseResponseInteger } from '@/types/api.ts'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref<FormInstance>()
const currentNav = ref('create-batch')

// 表单状态
const formState = reactive({
  batchId: '',
  type: undefined as string | undefined,
  agcId: '',
  testName: '',
  batchDate: '',
  remarks: ''
})

// 状态管理
const submitting = ref(false)

// 产品品种选项 - 养殖企业主要畜禽品种
const baseProductTypes = [
  '生猪',
  '肉牛',
  '奶牛',
  '肉羊',
  '绵羊',
  '山羊',
  '肉鸡',
  '蛋鸡',
  '肉鸭',
  '蛋鸭',
  '肉鹅',
  '火鸡',
  '鸽子',
  '鹌鹑',
  '兔子',
  '马',
  '驴',
  '骆驼'
]

const productTypeOptions = ref(baseProductTypes.map(type => ({
  value: type,
  label: type,
  isCustom: false
})))

// 表单验证规则
const rules: Record<string, Rule[]> = {
  batchId: [
    { required: true, message: '请输入产品批号', trigger: 'blur' },
    { min: 1, max: 50, message: '产品批号长度应在1-50个字符之间', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择或输入产品品种', trigger: 'change' },
    { min: 1, max: 20, message: '产品品种长度应在1-20个字符之间', trigger: 'blur' }
  ],
  agcId: [
    { max: 100, message: '动物检疫合格证编号不能超过100个字符', trigger: 'blur' }
  ],
  testName: [
    { max: 50, message: '官方检疫员名称不能超过50个字符', trigger: 'blur' }
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

  // 如果搜索文本不在基础选项中，添加自定义选项
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

// 处理表单提交
const handleSubmit = async () => {
  try {
    // 表单验证
    await formRef.value?.validate()

    submitting.value = true

    // 构建提交数据
    const submitData: FarmBatch = {
      batchId: formState.batchId.trim(),
      farmId: authStore.userInfo?.userId, // 使用当前登录用户的ID作为养殖企业ID
      type: formState.type,
      agcId: formState.agcId || undefined,
      testName: formState.testName || undefined,
      batchDate: formState.batchDate ? new Date(formState.batchDate).toISOString() : undefined,
      remarks: formState.remarks || undefined,
      state: 1 // 默认状态为待发布
    }

    // 调用API
    const response: BaseResponseInteger = await saveFarmBatch(submitData)

    if (response.code === 0 && response.data && response.data > 0) {
      message.success('产品批号创建成功')

      // 重置表单
      formRef.value?.resetFields()

      // 延迟跳转回首页
      setTimeout(() => {
        router.push('/node/farm/home')
      }, 1500)
    } else {
      // 后端会返回批号已存在的错误信息
      message.error(response.message || '创建失败')
    }
  } catch (error: any) {
    if (error.errorFields) {
      // 表单验证错误
      message.error('请完善表单信息')
    } else {
      // API调用错误
      console.error('创建产品批号失败:', error)
      message.error(error.message || '创建失败，请重试')
    }
  } finally {
    submitting.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.push('/node/farm/home')
}

// 跳转到个人页面
const navigateToProfile = () => {
  router.push('/profile')
}

onMounted(() => {
  // 页面加载时的初始化操作
  console.log('创建产品批号页面加载完成')
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
</style>
