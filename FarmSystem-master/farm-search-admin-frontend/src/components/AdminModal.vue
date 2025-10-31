<template>
  <!-- Ant Design Vue 的模态框（Modal）组件 -->
  <a-modal
    :visible="visible"
    :title="isEdit ? '更新管理员' : '新建管理员'"
    ok-text="确认"
    cancel-text="取消"
    @cancel="close"
    @ok="submit"
  >
    <!-- 表单（Form）组件 -->
    <!-- ref="formRef" 用于在 <script> 中获取表单实例，以便调用其方法（如 validate） -->
    <!-- :model="formState" 将表单数据源绑定到 formState 响应式对象 -->
    <!-- :rules="rules" 将表单验证规则绑定到 rules 对象 -->
    <!-- layout="vertical" 设置表单项标签和输入框垂直排列 -->
    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">

      <!-- 用户名输入框 -->
      <!-- name="username" 必须与 formState 和 rules 中的键名匹配，用于验证 -->
      <a-form-item label="用户名" name="adminName">
        <!-- v-model:value 双向绑定输入框的值到 formState.username -->
        <a-input v-model:value="formState.adminName" />
      </a-form-item>

      <!-- 密码输入框 -->
      <!-- v-if="!isEdit" 条件渲染：只有在“新建”模式下 (isEdit 为 false) 才显示此输入框 -->
      <a-form-item label="密码" name="adminPassword" v-if="!isEdit">
        <!-- a-input-password 是一个带“显示/隐藏密码”功能的输入框 -->
        <a-input-password v-model:value="formState.adminPassword" />
      </a-form-item>

      <!-- 备注输入框 -->
      <a-form-item label="备注" name="remarks">
        <a-input v-model:value="formState.remarks" />
      </a-form-item>

      <!-- 角色选择器 -->
      <a-form-item label="角色" name="role">
        <!-- a-select 是一个下拉选择器 -->
        <a-select v-model:value="formState.role" placeholder="请选择角色">
          <!-- a-select-option 定义下拉选项 -->
          <a-select-option value="NORMAL">普通管理员</a-select-option>
          <a-select-option value="ADMIN">高级管理员</a-select-option>
        </a-select>
      </a-form-item>

    </a-form>
  </a-modal>
</template>


<script setup lang="ts">
// 从 'vue' 中导入 ref, watch, toRefs 用于创建响应式数据、监听数据变化和转换 props
import { ref, watch, toRefs } from 'vue';
// 从 'ant-design-vue' 中导入 message 组件用于显示全局提示信息
import { message } from 'ant-design-vue';

// 使用 defineProps 定义父组件传递过来的属性
const props = defineProps({
  // visible: 控制模态框显示或隐藏的布尔值
  visible: Boolean,
  // isEdit: 标记当前是“编辑”模式还是“新建”模式的布尔值
  isEdit: Boolean,
  // adminData: 在“编辑”模式下，父组件传递过来的管理员原始数据
  adminData: Object,
});

// 使用 defineEmits 定义本组件可以向父组件触发的自定义事件
const emit = defineEmits(['close', 'submit']);

// 使用 toRefs 将 props 对象转换为一组 ref, 使得解构后的 props 保持响应性
const { visible, isEdit, adminData } = toRefs(props);

// 创建一个 ref 来引用模板中的 a-form 组件实例
const formRef = ref();

// 创建一个响应式对象，用于存储表单的各个字段值
const formState = ref({
  adminName: '',
  adminPassword: '',
  remarks: '',
  role: 'NORMAL', // 默认角色为普通管理员
});

// 定义表单的验证规则，键名需要与 formState 中的键名一一对应
const rules = {
  adminName: [{ required: true, message: '请输入用户名' }],
  adminPassword: [{ required: true, message: '请输入密码' }],
  remarks: [{ required: true, message: '请输入管理员备注' }],
  role: [{ required: true, message: '请选择角色' }],
};

// 监听 visible prop 的变化
watch(visible, (newVal) => {
  // 当 newVal 为 true 时，表示模态框被打开
  if (newVal) {
    // 如果是“编辑”模式 (isEdit 为 true) 并且父组件传递了 adminData
    if (isEdit.value && adminData.value) {
      // 编辑模式：使用传递过来的数据填充表单
      // 使用扩展运算符 (...) 创建一个 adminData 的浅拷贝，避免直接修改 props
      formState.value = { ...adminData.value };
      // 通常在编辑时不回显密码，所以从表单数据中删除 password 字段
      delete formState.value.adminPassword;
    } else {
      // 新建模式：重置表单数据为初始状态
      formState.value = {
        adminName: '',
        adminPassword: '',
        remarks: '',
        role: 'admin',
      };
    }
  }
});

// 定义关闭模态框的函数
const close = () => {
  // 触发 'close' 事件，通知父组件关闭模态框
  emit('close');
};

// 定义提交表单的函数
const submit = () => {
  // 调用表单实例的 validate 方法进行验证
  formRef.value.validate().then(() => {
    // 验证成功后，触发 'submit' 事件，并把表单数据 (formState.value) 作为参数传递给父组件
    emit('submit', formState.value);
  }).catch(error => {
    // 验证失败时的回调
    console.log('表单验证失败:', error);
    message.warning('请填写完整信息');
  });
};
</script>

<style scoped>
/* scoped 属性确保此处的 CSS 只应用于当前组件，不会影响到其他组件 */
</style>
