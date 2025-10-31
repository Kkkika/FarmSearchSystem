<template>
  <div class="mobile-wrapper">
    <div class="mobile-container">
      <div class="home-container">
        <!-- 头部信息 -->
        <div class="header-section">
          <h1 class="enterprise-title">{{ authStore.getEnterpriseDisplayName() }}</h1>
          <p class="node-type">当前溯源节点类型：{{ authStore.getCurrentEnterpriseTypeName() }}</p>
        </div>

        <!-- 主要内容区域 -->
        <div class="main-content">
          <!-- 功能菜单 -->
          <div class="function-section">
            <a-card class="function-card" @click="navigateTo('create-batch')">
              <template #title>
                <plus-circle-outlined class="card-icon" />
                <span>新建产品批号</span>
              </template>
            </a-card>

            <a-card class="function-card" @click="navigateTo('batch-management')">
              <template #title>
                <unordered-list-outlined class="card-icon" />
                <span>产品批号管理</span>
              </template>
            </a-card>

            <a-card class="function-card" @click="navigateTo('downstream-confirm')">
              <template #title>
                <check-circle-outlined class="card-icon" />
                <span>下游企业进场确认</span>
              </template>
            </a-card>
          </div>

          <!-- 分割线 -->
          <a-divider style="margin: 0;" />
        </div>

        <!-- 底部导航 -->
        <div class="bottom-nav">
          <div class="nav-item" :class="{ active: currentNav === 'home' }" @click="currentNav = 'home'">
            <home-outlined />
            <span>首页</span>
          </div>
          <div class="nav-item" :class="{ active: currentNav === 'profile' }" @click="navigateToProfile">
            <user-outlined />
            <span>我的</span>
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
  HomeOutlined,
  UserOutlined,
  PlusCircleOutlined,
  UnorderedListOutlined,
  CheckCircleOutlined
} from '@ant-design/icons-vue'
import { useAuthStore } from '@/stores/auth.ts'
import { getNodeProfile } from '@/api/nodeInfoController.ts'
import router from "@/router";

const authStore = useAuthStore()
const currentNav = ref('home')

const navigateTo = (path: string) => {
  switch (path) {
    case 'create-batch':
      router.push('/slau/create-batch')
      break
    case 'batch-management':
      message.info('跳转到产品批号管理页面')
      break
    case 'downstream-confirm':
      message.info('跳转到下游企业进场确认页面')
      break
    default:
      break
  }
}

const navigateToProfile = () => {
  router.push('/profile')
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
  height: 100vh; /* 改为固定高度 */
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  position: relative;
}

.home-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px 20px;
  color: white;
  text-align: center;
  flex-shrink: 0; /* 防止头部被压缩 */
}

.main-content {
  flex: 1; /* 主要内容区域填充剩余空间 */
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止溢出 */
}

.function-section {
  flex: 1; /* 功能区域填充主要内容区域 */
  padding: 24px 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  overflow-y: auto; /* 内容过多时可滚动 */
}

.bottom-nav {
  display: flex;
  background: #fafafa;
  border-top: 1px solid #f0f0f0;
  padding: 12px 0;
  flex-shrink: 0; /* 防止底部导航被压缩 */
}

/* 其他样式保持不变 */
.enterprise-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: white;
}

.node-type {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
  color: white;
}

.function-card {
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.function-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  border-color: #667eea;
}

:deep(.function-card .ant-card-head) {
  border-bottom: none;
  padding: 16px;
  min-height: auto;
}

:deep(.function-card .ant-card-body) {
  display: none;
}

.card-icon {
  color: #667eea;
  margin-right: 8px;
}

:deep(.function-card .ant-card-head-title) {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
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

.nav-item span {
  font-size: 12px;
  margin-top: 4px;
}

/* 响应式调整 */
@media (max-width: 320px) {
  .mobile-container {
    height: 568px; /* 小屏高度 */
  }

  .function-section {
    padding: 20px 16px;
    gap: 12px;
  }

  .header-section {
    padding: 20px 16px;
  }

  .enterprise-title {
    font-size: 18px;
  }
}
</style>
