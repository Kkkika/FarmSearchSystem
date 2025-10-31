<template>
  <a-layout-footer style="text-align: center">
    肉类食品溯源系统  <span v-show="user.id">欢迎: {{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts" setup>
import {  computed } from 'vue';
import store from '@/store';

const user = computed(()=>store.state.user);

</script>
