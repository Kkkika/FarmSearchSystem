<template>
  <div class="mobile-wrapper">
    <div class="mobile-container">
      <div class="login-container">
        <!-- 头部 -->
        <div class="login-header">
          <h1 class="login-title">GALA肉类食品溯源系统</h1>
          <p class="login-subtitle">—— 流通节点端 ——</p>
        </div>

        <!-- 登录表单 -->
        <div class="login-form">
          <a-form
            :model="loginForm"
            :rules="rules"
            ref="formRef"
            layout="vertical"
            @finish="handleLogin"
          >
            <a-form-item label="登录编码" name="code">
              <a-input
                v-model:value="loginForm.code"
                placeholder="请输入登录编码"
                size="large"
                class="login-input"
              >
                <template #prefix>
                  <user-outlined />
                </template>
              </a-input>
            </a-form-item>

            <a-form-item label="登录密码" name="password">
              <a-input-password
                v-model:value="loginForm.password"
                placeholder="请输入登录密码"
                size="large"
                class="login-input"
              >
                <template #prefix>
                  <lock-outlined />
                </template>
              </a-input-password>
            </a-form-item>

            <a-form-item>
              <a-button
                type="primary"
                html-type="submit"
                size="large"
                :loading="loading"
                block
                class="login-btn"
              >
                {{ loading ? '登录中...' : '登录' }}
              </a-button>
            </a-form-item>
          </a-form>
        </div>

        <!-- 底部信息 -->
        <div class="login-footer">
          <p>GALA工作室 版权所有</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message, type FormInstance } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { nodeLogin } from '@/api/nodeInfoController.ts'
import { useAuthStore } from '@/stores/auth.ts'

interface LoginForm {
  code: string
  password: string
}

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive<LoginForm>({
  code: '',
  password: ''
})

const rules = {
  code: [
    { required: true, message: '请输入登录编码', trigger: 'blur' },
    { min: 1, max: 50, message: '登录编码长度在1-50个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入登录密码', trigger: 'blur' },
    { min: 1, max: 100, message: '密码长度在1-100个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  try {
    loading.value = true

    const response = await nodeLogin({
      code: loginForm.code,
      password: loginForm.password
    })

    if (response.data) {
      // 保存登录信息
      authStore.login(response.data)
      message.success('登录成功')

      // 根据企业类型跳转到对应页面
      redirectByEnterpriseType(response.data.enterpriseType)
    } else {
      message.error('登录失败，请检查登录信息')
    }
  } catch (error: any) {
    console.error('登录失败:', error)

    if (error.response?.data?.message) {
      message.error(error.response.data.message)
    } else {
      message.error('登录失败，请重试')
    }
  } finally {
    loading.value = false
  }
}

const redirectByEnterpriseType = (enterpriseType?: number) => {
  switch (enterpriseType) {
    case 1: // 养殖企业
      router.push('/node/farm/home')
      break
    case 2: // 屠宰企业
      router.push('/node/slau/home')
      break
    case 3: // 批发商
      router.push('/node/whol/home')
      break
    case 4: // 零售商
      router.push('/node/reta/home')
      break
    default:
      message.error('未知的企业类型')
      router.push('/node/login')
  }
}

onMounted(() => {
  // 如果已登录，直接跳转到对应页面
  if (authStore.isLoggedIn && authStore.userInfo?.enterpriseType) {
    redirectByEnterpriseType(authStore.userInfo.enterpriseType)
  }
})
</script>

<style scoped>
.mobile-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

.login-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 40px 30px;
}

.login-header {
  text-align: center;
  margin-bottom: 60px;
  margin-top: 40px;
}

.login-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0 0 12px 0;
}

.login-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.login-form {
  flex: 1;
}

.login-input {
  border-radius: 12px;
  height: 48px;
}

.login-btn {
  border-radius: 12px;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  margin-top: 20px;
}

.login-footer {
  text-align: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.login-footer p {
  color: #999;
  font-size: 12px;
  margin: 0;
}

/* 响应式调整 */
@media (max-width: 320px) {
  .mobile-container {
    min-height: 568px;
  }

  .login-container {
    padding: 30px 20px;
  }

  .login-title {
    font-size: 18px;
  }
}

/* 防止iOS输入框缩放 */
:deep(input) {
  font-size: 16px !important;
}
</style>
