import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// 懒加载组件
const NodeLoginView = () => import('@/views/node/NodeLoginView.vue')
const FarmHomeView = () => import('@/views/node/FarmHomeView.vue')
const SlauHomeView = () => import('@/views/node/SlauHomeView.vue')
const WholHomeView = () => import('@/views/node/WholHomeView.vue')
const RetaHomeView = () => import('@/views/node/RetaHomeView.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 节点企业登录
    {
      path: '/node/login',
      name: 'node-login',
      component: NodeLoginView,
      meta: { requiresAuth: false }
    },

    // 养殖企业主页
    {
      path: '/node/farm/home',
      name: 'farm-home',
      component: FarmHomeView,
      meta: { requiresAuth: true, enterpriseType: 1 }
    },

    // 屠宰企业主页
    {
      path: '/node/slau/home',
      name: 'slau-home',
      component: SlauHomeView,
      meta: { requiresAuth: true, enterpriseType: 2 }
    },

    // 批发商主页
    {
      path: '/node/whol/home',
      name: 'whol-home',
      component: WholHomeView,
      meta: { requiresAuth: true, enterpriseType: 3 }
    },

    // 零售商主页
    {
      path: '/node/reta/home',
      name: 'reta-home',
      component: RetaHomeView,
      meta: { requiresAuth: true, enterpriseType: 4 }
    },

    // 默认重定向
    {
      path: '/',
      redirect: '/node/login'
    },
    {
      path: '/node',
      redirect: '/node/login'
    },
    {
      path: '/profile',
      name: 'Profile',
      component: () => import('@/views/components/ProfileView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/enterprise-info',
      name: 'EnterpriseInfo',
      component: () => import('@/views/components/EnterpriseInfoView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/update-password',
      name: 'UpdatePassword',
      component: () => import('@/views/components/UpdatePasswordView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/farm/create-batch',
      name: 'FarmCreateBatch',
      component: () => import('@/views/farmView/FarmCreateBatch.vue')
    },
    {
      path: '/slau/create-batch',
      name: 'SlauCreateBatch',
      component: () => import('@/views/slauView/SlauCreateBatch.vue')
    },
    {
      path: '/whol/create-batch',
      name: 'WholCreateBatch',
      component: () => import('@/views/wholView/WholCreateBatch.vue'),
      meta: { title: '新建产品批号', requiresAuth: true }
    },
    {
      path: '/reta/create-batch',
      name: 'RetaCreateBatch',
      component: () => import('@/views/retaView/RetaCreateBatch.vue'),
      meta: { title: '新建产品批号', requiresAuth: true }
    },
    {
      path: '/farm/batch-management',
      name: 'FarmBatchManagement',
      component: () => import('@/views/farmView/FarmBatchManagement.vue'),
      meta: { title: '产品批号管理', requiresAuth: true }
    },
    // 在路由配置中添加
    {
      path: '/farm/batch-update/:fbId',
      name: 'FarmBatchUpdate',
      component: () => import('@/views/farmView/FarmBatchUpdate.vue'),
      meta: { requiresAuth: true }
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // 初始化认证状态
  if (!authStore.isLoggedIn) {
    authStore.initAuth()
  }

  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    if (!authStore.isLoggedIn) {
      // 未登录，跳转到登录页
      next('/node/login')
    } else if (to.meta.enterpriseType && authStore.userInfo?.enterpriseType !== to.meta.enterpriseType) {
      // 企业类型不匹配，跳转到对应类型的主页
      redirectToEnterpriseHome(authStore.userInfo?.enterpriseType, next)
    } else {
      next()
    }
  } else if (to.path === '/node/login' && authStore.isLoggedIn) {
    // 已登录但访问登录页，跳转到对应主页
    redirectToEnterpriseHome(authStore.userInfo?.enterpriseType, next)
  } else {
    next()
  }
})

function redirectToEnterpriseHome(enterpriseType: number | undefined, next: any) {
  switch (enterpriseType) {
    case 1:
      next('/node/farm/home')
      break
    case 2:
      next('/node/slau/home')
      break
    case 3:
      next('/node/whol/home')
      break
    case 4:
      next('/node/reta/home')
      break
    default:
      next('/node/login')
  }
}

export default router
