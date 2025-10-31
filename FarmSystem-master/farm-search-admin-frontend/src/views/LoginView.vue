<template>
  <div class="login-container">
    <h1 class="main-title">欢迎使用肉类食品溯源系统管理平台</h1>
    <a-card class="login-card" :bordered="false">
      <a-form
        ref="formRef"
        :model="LoginData"
        :rules="rules"
        @finish="handleLogin"
      >
        <!-- (已移除) 登录角色切换功能已被移除，因为角色应由后端决定 -->

        <a-form-item name="adminName">
          <a-input
            v-model:value="LoginData.adminName"
            placeholder="请输入用户名"
            size="large"
          >
            <template #prefix>
              <UserOutlined class="site-form-item-icon" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="adminPassword">
          <a-input-password
            v-model:value="LoginData.adminPassword"
            placeholder="请输入密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined class="site-form-item-icon" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="captcha">
          <div class="captcha-wrapper">
            <a-input
              v-model:value="LoginData.captcha"
              placeholder="请输入验证码"
              size="large"
            >
              <template #prefix>
                <SafetyCertificateOutlined class="site-form-item-icon" />
              </template>
            </a-input>
            <div class="captcha-code" @click="refreshCaptcha" title="点击刷新">
              {{ captchaCode }}
            </div>
          </div>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            :loading="loading"
            size="large"
            block
          >
            登 录
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; // (修正) 移除了不再需要的 computed
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import {
  UserOutlined,
  LockOutlined,
  SafetyCertificateOutlined
} from '@ant-design/icons-vue';
import api from '@/api/index';
import store from '@/store';

// -- 响应式数据 --
const router = useRouter();
const formRef = ref();
const loading = ref(false);
const captchaCode = ref('');
// const user = computed(()=>store.state.user); // (修正) 这个页面通常不需要实时计算 user
// const loginModalLoading = ref(false); // (修正) 这个变量没有被使用

// (已修改) 表单数据，移除了 role 字段
const LoginData = reactive({
  adminName: '',
  adminPassword: '',
  captcha: '',
});

// (已修改) 表单验证规则，移除了 role 规则
const rules = {
  adminName: [{ required: true, message: '请输入用户名!' }],
  adminPassword: [{ required: true, message: '请输入密码!' }],
  captcha: [{ required: true, message: '请输入验证码!'}],
};

// --- 验证码逻辑 (无修改) ---
const generateCaptcha = () => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  let result = '';
  for (let i = 0; i < 4; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
};

const refreshCaptcha = () => {
  captchaCode.value = generateCaptcha();
};

/**
 * (已修改) 登录逻辑
 */
const handleLogin = () => { // (修正) @finish 事件不传递 values，直接从 reactive 对象取值
  loading.value = true;

  // 校验验证码 (不区分大小写)
  if (LoginData.captcha.toLowerCase() !== captchaCode.value.toLowerCase()) {
    message.error('验证码不正确！');
    loading.value = false;
    refreshCaptcha();
    LoginData.captcha = '';
    return;
  }

  // (核心修改) 创建一个只包含后端所需字段的载荷(payload)对象
  const payload = {
    adminName: LoginData.adminName,
    adminPassword: LoginData.adminPassword,
  };

  // (修复) 使用正确的变量名 LoginData 或 payload 进行日志记录和 API 调用
  console.log("发送到后端的登录数据:", payload);

  api.post('/admin/login', payload).then((response) => {
    loading.value = false;
    if (response.code === 0) {
      message.success('登录成功！');
      // 后端返回的数据中应该包含了用户信息和 token，将其存入 store
      store.commit('setUser', response.data);
      // 跳转到后台主页
      router.push('/RegisStats');
    } else {
      message.error(response.message || '登录失败，请检查用户名或密码');
      refreshCaptcha();
      LoginData.captcha = '';
    }
  }).catch(err => {
    // 捕获网络错误等
    loading.value = false;
    message.error('登录请求失败，请检查网络连接');
    console.error('Login API Error:', err);
    refreshCaptcha();
  });
};

// 组件挂载时生成初始验证码
onMounted(() => {
  refreshCaptcha();
});
</script>

<style scoped>
/* 样式部分无修改，保持原样 */
.login-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: url('https://gw.alipayobjects.com/zos/rmsportal/TVYTbAXWheQpRcWDaDMu.svg');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
}

.login-card {
  width: 400px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px; /* 圆角 */
}

.main-title {
  font-size: 32px;
  color: #333;
  font-weight: 600;
  margin-bottom: 40px; /* 标题和卡片之间的间距 */
}

.captcha-wrapper {
  display: flex;
  align-items: center;
}

.captcha-code {
  width: 120px;
  height: 40px; /* 与 antd large size input 高度一致 */
  margin-left: 12px;
  font-size: 22px;
  font-family: 'Courier New', Courier, monospace;
  font-weight: bold;
  letter-spacing: 4px;
  text-align: center;
  line-height: 40px;
  cursor: pointer;
  background-color: #fafafa;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  user-select: none;
  transition: all 0.3s;
}
.captcha-code:hover {
  border-color: #40a9ff;
}
</style>
