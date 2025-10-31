<template>
  <a-layout-header class="header">
    <div class="logo" >肉类食品溯源系统管理平台</div>
    <a-menu
      theme="dark"
      mode="horizontal"
      :style="{ lineHeight: '64px' , display:'block'}">

      <a-menu-item key="/RegisStats">
        <router-link to="/RegisStats" class="RegisMenulink">
          节点企业注册信息统计
        </router-link>
      </a-menu-item>

      <a-menu-item key="/RegisInfo">
        <router-link to="/RegisInfo" class="RegisMenulink">
          节点企业注册信息管理
        </router-link>
      </a-menu-item>

      <a-menu-item key="/adminUser" v-if="user && user.userType == 'ADMIN'">
        <router-link to="/adminUser">
          <setting-outlined />
          <span>管理员管理</span>
        </router-link>
      </a-menu-item>

      <a-menu-item key="userDropdown" :style="{ float: 'right' }" v-if="user.userId">
        <a-dropdown>
          <a class="ant-dropdown-link" @click.prevent>
            您好：
            <span v-if="user.userType === 'ADMIN'">高级管理员</span>
            <span v-if="user.userType === 'NORMAL'">普通管理员</span>
            {{ user.username }}
            <DownOutlined />
          </a>
          <!-- 下拉菜单的内容-->
          <template #overlay>
            <a-menu @click="handleMenuClick">
              <a-menu-item key="updatePassword">
                <UserOutlined />
                更新密码
              </a-menu-item>
              <a-menu-item key="logout">
                <LogoutOutlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </a-menu-item>

      <!-- 更新密码弹窗 -->
      <a-modal
        v-model:open="updatePasswordModalopen"
        title="更新密码"
        ok-text="确认修改"
        cancel-text="取消"
        :confirm-loading="modalLoading"
        @ok="handleUpdatePassword"
        @cancel="handleCancel">
        <a-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          layout="vertical">
          <a-form-item label="原密码" name="oldPassword">
            <a-input-password v-model:value="passwordForm.oldPassword" placeholder="请输入原密码" />
          </a-form-item>
          <a-form-item label="新密码" name="newPassword">
            <a-input-password v-model:value="passwordForm.newPassword" placeholder="请输入新密码" />
          </a-form-item>
          <a-form-item label="确认新密码" name="confirmPassword">
            <a-input-password v-model:value="passwordForm.confirmPassword" placeholder="请再次输入新密码" />
          </a-form-item>
        </a-form>
      </a-modal>

    </a-menu>
  </a-layout-header>
</template>
<script lang="ts" setup>

import { message,Modal } from "ant-design-vue";
import { SettingOutlined, DownOutlined, UserOutlined, LogoutOutlined } from '@ant-design/icons-vue';
import api from '@/api/index';
import { computed, reactive,defineComponent, ref,watchEffect } from 'vue';
import forge from 'node-forge';
import store from '@/store';
import {useRouter} from 'vue-router';
import type {Rule}from 'ant-design-vue/es/form';

const router = useRouter();
const user =computed(()=>store.state.user);
// 使用 watchEffect 可以在 user 变化时自动打印
watchEffect(() => {
  console.log("当前用户信息:", user.value);
});
//更新密码弹窗相关
// 控制弹窗显示/隐藏
const updatePasswordModalopen = ref(false);
// 弹窗确认按钮的加载状态
const modalLoading = ref(false);
// 表单的引用，用于校验等操作
const passwordFormRef = ref();
// 表单的数据模型
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
});

// 自定义校验规则：确认密码必须和新密码一致
const validateConfirmPassword = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('请再次输入新密码');
  } else if (value !== passwordForm.newPassword) {
    return Promise.reject("两次输入的新密码不一致!");
  } else {
    return Promise.resolve();
  }
};

// 表单的校验规则
const passwordRules: Record<string, Rule[]> = {
  oldPassword: [{ required: true, message: '请输入原密码!', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码!', trigger: 'blur' }],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ],
};

// 点击 Modal 的 "确认修改" 按钮时触发
const handleUpdatePassword = () => {
  passwordFormRef.value.validate().then(() => {
    modalLoading.value = true;
    //调用 API 接口
    api.post('/admin/updatePassword', {
      adminId: user.value.userId, // 传递当前用户ID
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
    }).then((response) => {
      console.log("修改密码返回的response code："+response.code);
      if (response.code === 0) {
        message.success('密码修改成功！');
        updatePasswordModalopen.value = false; // 关闭弹窗
        passwordFormRef.value.resetFields(); // 重置表单

      } else {
        message.error(response.message || '密码修改失败');
      }
    }).catch(error => {
      console.error("密码修改失败:", error);
      message.error(error.response?.data?.message || '操作失败，请重试');
    }).finally(() => {
      modalLoading.value = false; // 结束加载状态
    });
  }).catch(info => {
    console.log('表单校验失败:', info);
  });
};

// 点击 Modal 的 "取消" 按钮或遮罩层时触发
const handleCancel = () => {
  passwordFormRef.value.resetFields(); // 重置表单
  updatePasswordModalopen.value = false; // 关闭弹窗
};

// 退出登录
const logout = () => {
  try {
    console.log("执行前端退出登录...");

    //从 Vuex Store 中移除用户信息
    //这会立即更新 UI，比如隐藏用户头像和名称
    store.commit("setUser", {});

    //从sessionStorage中删除 accessToken 和 refreshToken
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('refreshToken');
    console.log("前端 Token 和用户状态已成功清除");

    //给出成功的用户反馈
    message.success("您已成功退出登录！", 2); // 2秒后自动关闭

    //将用户重定向到登录页面
    // 使用 replace 可以防止用户通过浏览器的“后退”按钮回到之前的页面
    router.replace('/LoginView');
  } catch (error) {
    // 通常情况下，本地删除操作不会失败，但为了代码健壮性可以加上
    console.error("前端退出登录时发生错误:", error);
    message.error("退出操作时发生未知错误，请刷新页面重试。");
  }

};

//处理下拉菜单点击事件的函数
const handleMenuClick = ({key}:{key:string}) => {
  if (key === 'updatePassword') {
    updatePasswordModalopen.value = true;
  } else if (key === 'logout') {
    //当点击退出登录时，弹出确认框
    Modal.confirm({
      title: '确认退出登录?',
      okText: '是',
      cancelText: '否',
      onOk: () => {
        logout();
      },
    });
  }
};



</script>

<style scoped>
.logo {
  width: 250px;
  height: 31px;
  float: left;
  color: white;
  font-size: 18px;
}

.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}

.RegisMenulink {
  display:flex;
  justify-content: center;

}
</style>
