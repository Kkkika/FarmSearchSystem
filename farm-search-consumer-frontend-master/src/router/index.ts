import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'mobile-trace',
      component: () => import('@/views/MobileTraceView.vue')
    },
    {
      path: '/trace',
      name: 'mobile-trace-page',
      component: () => import('@/views/MobileTraceView.vue')
    }
  ]
})

export default router
