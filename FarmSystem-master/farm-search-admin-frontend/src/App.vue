<template>
  <a-layout style="height: 100vh">
    <the-header v-if="showLayout" />

    <router-view />

    <the-footer v-if="showLayout" />
  </a-layout>
</template>

<script lang="ts" setup>
import TheHeader from "@/components/the-header.vue";
import TheFooter from "@/components/the-footer.vue";
import {useRoute} from "vue-router";
import {computed} from "vue";

//获取路由信息对象
const route = useRoute();

//计算是否显示header和footer
const showLayout = computed(() => {
  //如果当前路由的meta.hideLayout不是true，就返回true，即显示布局
  return route.meta.hideLayout !== true;
})

</script>

<style scoped>
.logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>
