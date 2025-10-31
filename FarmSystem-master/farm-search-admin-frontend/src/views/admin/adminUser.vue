<template>
  <div>
    <!-- (新增) 搜索面板 -->
    <div class="search-panel">
      <a-form layout="inline">
        <a-form-item label="关键词">
          <a-input
            v-model:value="searchKeyword"
            placeholder="搜索ID或用户名"
            style="width: 200px"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="角色">
          <a-select
            v-model:value="selectedRole"
            style="width: 150px"
            placeholder="请选择角色"
          >
            <a-select-option value="ALL">所有角色</a-select-option>
            <a-select-option value="ADMIN">高级管理员</a-select-option>
            <a-select-option value="NORMAL">普通管理员</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button style="margin-left: 10px" @click="handleReset">重置</a-button>
        </a-form-item>
      </a-form>
    </div>

    <!-- 头部区域，包含操作按钮 -->
    <div class="header">
      <a-button type="primary" @click="showAddModal">新建管理员</a-button>
      <a-button @click="handleRefresh" title="刷新列表">
        <template #icon><ReloadOutlined /></template>刷新
      </a-button>
    </div>

    <!-- Ant Design Vue 表格组件 -->
    <a-table
      :columns="columns"
      :data-source="filteredAdminList"
      :loading="loading"
      row-key="adminId"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a @click="showEditModal(record)">更新信息</a>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定要重置该管理员的密码吗？"
            ok-text="确定"
            cancel-text="取消"
            @confirm="handleResetPassword(record.adminId)"
          >
            <a>重置密码</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a-popconfirm
            title="确定删除这位管理员吗?"
            ok-text="确定"
            cancel-text="取消"
            @confirm="handleDelete(record.adminId)"
          >
            <a style="color: red">删除</a>
          </a-popconfirm>
        </template>
      </template>
    </a-table>

    <!-- 引用子组件 AdminModal -->
    <AdminModal
      :visible="isModalVisible"
      :is-edit="isEdit"
      :admin-data="currentAdmin"
      @close="handleModalClose"
      @submit="handleModalSubmit"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { ReloadOutlined } from '@ant-design/icons-vue';
import AdminModal from '@/components/AdminModal';
import api from '@/api/index';

// ----- 响应式状态定义 -----
const loading = ref(false);
const isModalVisible = ref(false);
const isEdit = ref(false);
const currentAdmin = ref(null);

// 列表和搜索相关的状态
const allAdminsList = ref([]); // 存储从后端获取的原始完整列表
const filteredAdminList = ref([]); // 存储筛选后要在表格中显示的列表
const searchKeyword = ref(''); // 绑定搜索输入框的值
const selectedRole = ref('ALL'); // 绑定角色下拉框的值，默认为 'ALL'

// ----- 表格列定义 -----
const columns = [
  { title: '管理员ID', dataIndex: 'adminId', key: 'adminId' },
  { title: '用户名', dataIndex: 'adminName', key: 'adminName' },
  { title: '角色', dataIndex: 'role', key: 'role' },
  { title: '备注', dataIndex: 'remarks', key: 'remarks' },
  { title: '操作', key: 'action' },
];


/**
 * 异步获取管理员列表
 */
const fetchAdmins = async () => {
  loading.value = true;
  try {
    const response = await api.get('/admin/list');
    if (response && response.code === 0) {
      allAdminsList.value = response.data; // 填充原始列表
      filteredAdminList.value = response.data; // 默认情况下，显示所有数据
      handleSearch();//带着搜索条件获取
    } else {
      message.error(response.message || '获取数据失败');
    }
  } catch (error) {
    message.error('获取管理员列表失败', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchAdmins();
});

/**
 *处理搜索/查询操作
 */
const handleSearch = () => {
  let result = allAdminsList.value;

  // 根据关键词过滤 (ID 或 用户名)
  const keyword = searchKeyword.value.trim().toLowerCase();
  if (keyword) {
    result = result.filter(admin =>
      String(admin.adminId).includes(keyword) || // 将 ID 转为字符串进行匹配
      admin.adminName.toLowerCase().includes(keyword) // 用户名不区分大小写匹配
    );
  }

  // 根据角色过滤
  if (selectedRole.value !== 'ALL') {
    result = result.filter(admin => admin.role === selectedRole.value);
  }

  filteredAdminList.value = result; // 更新表格显示的数据

  if (result.length === 0 && (keyword || selectedRole.value !== 'ALL')) {
    message.info('没有找到匹配的管理员');
  }
};

/**
 * 重置搜索条件
 */
const handleReset = () => {
  searchKeyword.value = '';
  selectedRole.value = 'ALL';
  filteredAdminList.value = allAdminsList.value; // 恢复显示完整列表
  message.success('查询条件已重置');
};


const handleRefresh = () => {
  // 刷新时也应该重置搜索条件
  handleReset();
  fetchAdmins();
};



const showAddModal = () => {
  isEdit.value = false;
  currentAdmin.value = null;
  isModalVisible.value = true;
};

const showEditModal = (admin) => {
  isEdit.value = true;
  currentAdmin.value = { ...admin };
  isModalVisible.value = true;
};

const handleDelete = async (adminId) => {
  try {
    const response = await api.delete(`/admin/delete/${adminId}`);
    if (response && response.code === 0) {
      message.success('删除成功');
      fetchAdmins(); // 删除成功后，重新加载列表
    } else {
      message.error(response.message || '删除失败');
    }
  } catch (error) {
    message.error('删除失败');
  }
};

const handleModalClose = () => {
  isModalVisible.value = false;
};

const handleModalSubmit = async (formData) => {
  try {
    if (isEdit.value) {
      const payload = { ...formData, adminId: currentAdmin.value.adminId };
      const response = await api.put('/admin/update', payload);
      if (response && response.code === 0) {
        message.success('更新成功！');
      } else {
        message.error(response.message || '更新失败');
      }
    } else {
      const response = await api.post('/admin/register', formData);
      if (response && response.code === 0) {
        message.success('新建管理员成功！');
      } else {
        message.error(response.message || '新建失败');
      }
    }
    isModalVisible.value = false;
    fetchAdmins();
  } catch (error) {
    message.error(isEdit.value ? '更新失败' : '新建失败');
  }
};

const handleResetPassword = async (adminId) => {
  try {
    const payload = { adminId: adminId };
    const response = await api.post('/admin/resetPassword', payload);
    if (response && response.code === 0) {
      const { newPassword, adminName } = response.data;
      Modal.success({
        title: '密码重置成功',
        content: `管理员 "${adminName}" 的新密码是： ${newPassword} 。请妥善保管。`,
        okText: '好的',
      });
    } else {
      message.error(response.message || '重置密码失败');
    }
  } catch (error) {
    message.error('重置密码操作失败');
  }
};
</script>

<style scoped>
/* 搜索面板样式 */
.search-panel {
  background: #fafafa;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  padding: 16px;
  margin-bottom: 20px;
  margin-top: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
</style>
