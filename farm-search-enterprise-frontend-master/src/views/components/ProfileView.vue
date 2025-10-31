<template>
  <div class="mobile-wrapper">
    <div class="mobile-container">
      <div class="profile-container">
        <!-- 头部信息 -->
        <div class="header-section">
          <a-button class="back-btn" type="text" @click="goBack">
            <arrow-left-outlined />
          </a-button>
          <h1 class="page-title">我的</h1>
        </div>

        <!-- 功能菜单 -->
        <div class="profile-section">
          <a-card class="profile-card" @click="navigateTo('enterprise-info')">
            <template #title>
              <bank-outlined class="card-icon" />
              <span>企业信息</span>
            </template>
          </a-card>

          <a-card class="profile-card" @click="navigateTo('update-password')">
            <template #title>
              <lock-outlined class="card-icon" />
              <span>更新密码</span>
            </template>
          </a-card>

          <a-card class="profile-card logout-card" @click="handleLogout">
            <template #title>
              <logout-outlined class="card-icon" />
              <span>退出登录</span>
            </template>
          </a-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import {
  ArrowLeftOutlined,
  BankOutlined,
  LockOutlined,
  LogoutOutlined
} from '@ant-design/icons-vue'
import { useAuthStore } from '@/stores/auth.ts'

const router = useRouter()
const authStore = useAuthStore()

const goBack = () => {
  router.back()
}

const navigateTo = (path: string) => {
  switch (path) {
    case 'enterprise-info':
      router.push('/enterprise-info')
      break
    case 'update-password':
      router.push('/update-password')
      break
    default:
      break
  }
}

const handleLogout = () => {
  Modal.confirm({
    title: '确认退出登录',
    content: '您确定要退出登录吗？',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      authStore.logout()
      message.success('退出登录成功')
      router.push('/node/login')
    }
  })
}
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

.profile-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 16px 20px;
  color: white;
  text-align: center;
  position: relative;
  flex-shrink: 0;
}

.back-btn {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: white;
  border: none;
  background: transparent;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: white;
}

.profile-section {
  flex: 1;
  padding: 24px 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.profile-card {
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.profile-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  border-color: #667eea;
}

.logout-card:hover {
  border-color: #ff4d4f;
}

.logout-card:hover .card-icon {
  color: #ff4d4f;
}

:deep(.profile-card .ant-card-head) {
  border-bottom: none;
  padding: 16px;
  min-height: auto;
}

:deep(.profile-card .ant-card-body) {
  display: none;
}

.card-icon {
  color: #667eea;
  margin-right: 8px;
  transition: color 0.3s ease;
}

.logout-card .card-icon {
  color: #ff4d4f;
}

:deep(.profile-card .ant-card-head-title) {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

/* 响应式调整 */
@media (max-width: 320px) {
  .mobile-container {
    height: 568px;
  }

  .profile-section {
    padding: 20px 16px;
    gap: 12px;
  }

  .header-section {
    padding: 14px 16px;
  }
}
</style>
