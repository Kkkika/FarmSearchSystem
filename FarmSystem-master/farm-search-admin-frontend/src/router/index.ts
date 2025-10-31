import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/LoginView.vue'
import store from "@/store";
import {message} from "ant-design-vue";
import api from '@/api/index';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    alias: '/LoginView',
    component: HomeView,
    meta:{
      hideLayout:true
    },
    beforeEnter:(to,from,next)=>{
      console.log(`导航守卫：准备进入登录页，当前登录状态 (来自getter): ${store.getters.isLoggedIn}`);
      //检查用户登录状态
      if(store.getters.isLoggedIn)
      {// 用户已登录，重定向到主页
        console.log("用户已登录，重定向到主页");
        next('/RegisStats' );
      }else
      {
        console.log("用户未登录，允许进入登录页");
        next();
      }
    }
  },
  {
    path: '/RegisStats',
    name: 'EnterpriseStats',
    component: () => import( '../views/RegisStatsView.vue'),//节点企业注册信息统计
    meta:{
      loginRequire:true
    }
  },
  {
    path: '/RegisInfo',
    name: 'EnterpriseRegisInfo',
    component: () => import( '../views/RegisInfoView.vue'),//节点企业注册信息管理
    meta:{
      loginRequire:true
    }
  },
  {
    //仅高级管理员可见
    path: '/adminUser',
    name: 'adminUser',
    component: () => import( '../views/admin/adminUser.vue'),//节点企业注册信息管理
    meta:{
      loginRequire:true,
      roles:['ADMIN']
    }
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 最下方路由登录拦截,每次路由拦截
router.beforeEach((to, from, next) => {
  if (to.matched.some(function (item) {
    return item.meta.loginRequire
  })) {
    if (!store.getters.isLoggedIn) {
      message.error("请登录！")
      next("/LoginView")
    } else {
      //用户已登录，检查角色身份
      if(to.meta.roles){
        //用户是高级管理员，放行
        if(to.meta.roles.includes(store.getters.isAdmin))
        {
         next();
        }else{
          //不符合
          message.error('抱歉，您没有权限访问此页面。')
          next(from.path)
        }
      }else{
        //页面不需要特定角色，直接放行
        next()
      }
    }
  } else {
    //页面不需要登录，直接放行
    next();
  }
});

export default router
