<template>
  <div class="page-container">
    <!--查询表单 -->
    <a-card :bordered="false" style="margin-bottom: 16px">
      <a-form :model="searchParams" layout="inline">
        <a-form-item label="企业名称">
          <a-input v-model:value="searchParams.name" placeholder="请输入企业名称" />
        </a-form-item>
        <a-form-item label="登录编码">
          <a-input v-model:value="searchParams.code" placeholder="请输入登录编码" />
        </a-form-item>
        <a-form-item label="企业类型">
          <a-select v-model:value="searchParams.type" placeholder="请选择企业类型" style="width: 150px" allow-clear>
            <a-select-option :value="1">养殖企业</a-select-option>
            <a-select-option :value="2">屠宰企业</a-select-option>
            <a-select-option :value="3">批发商</a-select-option>
            <a-select-option :value="4">零售商</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="searchParams.status" placeholder="请选择状态" style="width: 120px" allow-clear>
            <a-select-option :value="0">启用</a-select-option>
            <a-select-option :value="1">禁用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button style="margin-left: 8px" @click="handleReset">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!--操作栏和企业列表 -->
    <a-card :bordered="false">
      <div style="margin-bottom: 16px; display: flex; justify-content: space-between;">
        <a-button type="primary" @click="openAddModal">添加节点企业</a-button>
        <a-button @click="openRefreshConfirm">
          <template #icon><ReloadOutlined /></template>
          刷新
        </a-button>
      </div>

      <a-table
        :columns="columns"
        :dataSource="tableData"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
        rowKey="nodeId"
      >
        <template #bodyCell="{ column, record }">
          <!-- 状态列的自定义渲染 -->
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 0 ? 'green' : 'red'">
              {{ record.status === 0 ? '启用' : '禁用' }}
            </a-tag>
          </template>
          <!-- 操作列的按钮 -->
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="openDetailsModal(record)">详情</a-button>
              <!-- 启用状态下的操作 -->
              <template v-if="record.status === 0">
                <a-button type="link" size="small" @click="openEditModal(record)">更新</a-button>
                <a-button type="link" danger size="small" @click="handleDisable(record)">禁用</a-button>
              </template>
              <!-- 禁用状态下的操作 -->
              <template v-if="record.status === 1">
                <a-button type="link" size="small" @click="handleEnable(record)">启用</a-button>
                <a-button type="link" danger size="small" @click="handlePermanentDelete(record)">永久删除</a-button>
              </template>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!--更新弹窗 -->
    <a-modal
      v-model:open="editModalState.visible"
      :title="editModalState.title"
      @ok="handleEditModalOk"
      :confirm-loading="editModalState.confirmLoading"
      width="600px"
    >
      <a-form :model="editFormState" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="登录编码" required>
          <a-input v-model:value="editFormState.code" :disabled="true" placeholder="请输入登录编码" />
        </a-form-item>
        <a-form-item label="企业名称" required>
          <a-input v-model:value="editFormState.name" placeholder="请输入企业名称" />
        </a-form-item>
        <a-form-item label="企业类型" required>
          <a-select v-model:value="editFormState.type" placeholder="请选择企业类型" :disabled="true">
            <a-select-option :value="1">养殖企业</a-select-option>
            <a-select-option :value="2">屠宰企业</a-select-option>
            <a-select-option :value="3">批发商</a-select-option>
            <a-select-option :value="4">零售商</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 动态表单项 -->
        <a-form-item label="营业执照编号" required>
          <a-input v-model:value="editFormState.businessId" placeholder="请输入营业执照编号" />
        </a-form-item>
        <a-form-item v-if="editFormState.type === 1" label="动物防疫合格证" required>
          <a-input v-model:value="editFormState.epId" placeholder="请输入动物防疫条件合格证编号" />
        </a-form-item>
        <a-form-item v-if="editFormState.type === 1 || editFormState.type === 2" label="环评资质证书" required>
          <a-input v-model:value="editFormState.eiaId" placeholder="请输入环境影响评价资质证书编号" />
        </a-form-item>
        <a-form-item v-if="editFormState.type === 3" label="食品流通许可证" required>
          <a-input v-model:value="editFormState.cirId" placeholder="请输入食品流通许可证编号" />
        </a-form-item>
        <a-form-item v-if="editFormState.type === 3 || editFormState.type === 4" label="食品经营许可证" required>
          <a-input v-model:value="editFormState.fbId" placeholder="请输入食品经营许可证编号" />
        </a-form-item>

        <a-form-item label="法人">
          <a-input v-model:value="editFormState.corporation" placeholder="请输入法人姓名" />
        </a-form-item>
        <a-form-item label="联系电话">
          <a-input v-model:value="editFormState.telephone" placeholder="请输入联系电话" />
        </a-form-item>

        <a-form-item label="所在省份" required>
          <a-select
            v-model:value="editFormState.provId"
            placeholder="请选择省份"
            @change="handleEditProvinceChange"
          >
            <a-select-option v-for="p in provinceList" :key="p.id" :value="p.id">
              {{ p.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="所在城市" required>
          <a-select
            v-model:value="editFormState.cityId"
            placeholder="请先选择省份"
            :disabled="!editFormState.provId"
          >
            <a-select-option v-for="c in editCityList" :key="c.id" :value="c.id">
              {{ c.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="企业地址">
          <a-input v-model:value="editFormState.address" placeholder="请输入企业地址" />
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea v-model:value="editFormState.remarks" placeholder="请输入备注信息" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 添加表单 -->
    <a-modal
      v-model:open="addModalState.visible"
      :title="addModalState.title"
      @ok="handleAddModalOk"
      :confirm-loading="addModalState.confirmLoading"
      width="600px"
    >
      <a-form :model="addFormState" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="登录编码" required>
          <a-input v-model:value="addFormState.code" placeholder="请输入登录编码" />
        </a-form-item>

        <a-form-item label="登录密码" required>
          <a-input-password v-model:value="addFormState.password" placeholder="请输入登录密码" />
        </a-form-item>

        <a-form-item label="企业名称" required>
          <a-input v-model:value="addFormState.name" placeholder="请输入企业名称" />
        </a-form-item>
        <a-form-item label="企业类型" required>
          <a-select v-model:value="addFormState.type" placeholder="请选择企业类型">
            <a-select-option :value="1">养殖企业</a-select-option>
            <a-select-option :value="2">屠宰企业</a-select-option>
            <a-select-option :value="3">批发商</a-select-option>
            <a-select-option :value="4">零售商</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="营业执照编号" required>
          <a-input v-model:value="addFormState.businessId" placeholder="请输入营业执照编号" />
        </a-form-item>
        <a-form-item v-if="addFormState.type === 1" label="动物防疫合格证" required>
          <a-input v-model:value="addFormState.epId" placeholder="请输入动物防疫条件合格证编号" />
        </a-form-item>
        <a-form-item v-if="addFormState.type === 1 || addFormState.type === 2" label="环评资质证书" required>
          <a-input v-model:value="addFormState.eiaId" placeholder="请输入环境影响评价资质证书编号" />
        </a-form-item>
        <a-form-item v-if="addFormState.type === 3" label="食品流通许可证" required>
          <a-input v-model:value="addFormState.cirId" placeholder="请输入食品流通许可证编号" />
        </a-form-item>
        <a-form-item v-if="addFormState.type === 3 || addFormState.type === 4" label="食品经营许可证" required>
          <a-input v-model:value="addFormState.fbId" placeholder="请输入食品经营许可证编号" />
        </a-form-item>

        <a-form-item label="法人">
          <a-input v-model:value="addFormState.corporation" placeholder="请输入法人姓名" />
        </a-form-item>
        <a-form-item label="联系电话">
          <a-input v-model:value="addFormState.telephone" placeholder="请输入联系电话" />
        </a-form-item>

        <a-form-item label="所在省份" required>
          <a-select
            v-model:value="addFormState.provId"
            placeholder="请选择省份"
            @change="handleProvinceChange"
          >
            <a-select-option v-for="p in provinceList" :key="p.id" :value="p.id">
              {{ p.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="所在城市" required>
          <a-select
            v-model:value="addFormState.cityId"
            placeholder="请先选择省份"
            :disabled="!addFormState.provId"
          >
            <a-select-option v-for="c in addCityList" :key="c.id" :value="c.id">
              {{ c.name }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="企业地址">
          <a-input v-model:value="addFormState.address" placeholder="请输入企业地址" />
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea v-model:value="addFormState.remarks" placeholder="请输入备注信息" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 4. 详情弹窗 -->
    <a-modal v-model:open="detailsModal.visible" title="企业详情" :footer="null" width="700px">
      <a-descriptions bordered :column="2">
        <a-descriptions-item label="企业ID">{{ detailsModal.data.nodeId }}</a-descriptions-item>
        <a-descriptions-item label="登录编码">{{ detailsModal.data.code }}</a-descriptions-item>
        <a-descriptions-item label="企业名称">{{ detailsModal.data.name }}</a-descriptions-item>
        <a-descriptions-item label="企业类型">{{ detailsModal.data.type }}</a-descriptions-item>
        <a-descriptions-item label="省份">{{ detailsModal.data.province }}</a-descriptions-item>
        <a-descriptions-item label="城市">{{ detailsModal.data.city }}</a-descriptions-item>
        <a-descriptions-item label="详细地址" :span="2">{{ detailsModal.data.address }}</a-descriptions-item>
        <a-descriptions-item label="营业执照编号">{{ detailsModal.data.businessId }}</a-descriptions-item>
        <a-descriptions-item label="动物防疫合格证">{{ detailsModal.data.epId || '无' }}</a-descriptions-item>
        <a-descriptions-item label="环评资质证书">{{ detailsModal.data.eiaId || '无' }}</a-descriptions-item>
        <a-descriptions-item label="食品流通许可证">{{ detailsModal.data.cirId || '无' }}</a-descriptions-item>
        <a-descriptions-item label="食品经营许可证">{{ detailsModal.data.fbId || '无' }}</a-descriptions-item>
        <a-descriptions-item label="法人">{{ detailsModal.data.corporation }}</a-descriptions-item>
        <a-descriptions-item label="联系电话">{{ detailsModal.data.telephone }}</a-descriptions-item>
        <a-descriptions-item label="注册日期">{{ detailsModal.data.regDate }}</a-descriptions-item>
      </a-descriptions>
      <div style="text-align: right; margin-top: 20px;">
        <a-space>
          <a-button @click="detailsModal.visible = false">关闭</a-button>
        </a-space>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import { Modal, message } from 'ant-design-vue';
import { ReloadOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { createVNode } from 'vue';
import api from '@/api/index'

// 表格列定义
const columns = [
  { title: '企业名称', dataIndex: 'name', key: 'name' },
  { title: '登录编码', dataIndex: 'code', key: 'code' },
  { title: '企业类型', dataIndex: 'type', key: 'type' },
  { title: '状态', key: 'status' },
  { title: '注册日期', dataIndex: 'regDate', key: 'regDate' },
  { title: '上次更新日期', dataIndex: 'updateTime', key: 'updateTime' },
  { title: '操作', key: 'action', width: 220 },
];

//省份列表
const provinceList = ref([
  { id: 1, name: '首都' },
  { id: 2, name: '经济中心' },
  { id: 3, name: '广东省' },
  { id: 4, name: '江苏省' },
  { id: 5, name: '四川省' },
]);

//城市总列表
const allCitiesList = ref([
  { id: 1, name: '北京市', provId: 1 },
  { id: 2, name: '上海市', provId: 2 },
  { id: 3, name: '广州市', provId: 3 },
  { id: 4, name: '深圳市', provId: 3 },
  { id: 5, name: '南京市', provId: 4 },
  { id: 6, name: '成都市', provId: 5 },
]);

// 用于存放“添加”根据省份筛选后的城市列表
const addCityList = ref([]);
// 用于存放“更新”弹窗的城市列表
const editCityList = ref([]);

//处理 添加 省份选择城市变化逻辑
const handleProvinceChange = (selectedProvId) => {
  // 当省份改变时，清空已选择的城市
  addFormState.value.cityId = null;
  // 根据选择的省份ID，从总列表里筛选出对应的城市
  addCityList.value = allCitiesList.value.filter(city => city.provId === selectedProvId);
};

//处理 更新 省份选择城市变化逻辑
const handleEditProvinceChange = (selectedProvId) => {
  editFormState.value.cityId = null;
  editCityList.value = allCitiesList.value.filter(city => city.provId === selectedProvId);
};

//分页查询的搜索参数
const searchParams = reactive({
  name: '',
  type: undefined,
  provId: undefined,
  cityId: undefined,
  code: '',
  status: undefined,
});

const tableData = ref([]);
const loading = ref(false);
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
});

// 更新弹窗的状态
const editModalState = reactive({
  visible: false,
  title: '更新节点企业信息',
  confirmLoading: false,
});
const editFormState = ref({}); // 更新表单的数据

//完整的添加弹窗初始化表单对象
const getInitialFormState = () => ({
  nodeId: null,
  code: '',
  password: '',
  name: '',
  type: 1,
  provId: null,
  cityId: null,
  address: '',
  businessId: '',
  epId: '',
  eiaId: '',
  cirId: '',
  fbId: '',
  corporation: '',
  telephone: '',
  regDate: null,
  remarks: '',
});

//添加弹窗的状态
const addModalState = reactive({
  visible: false,
  title: '添加节点企业',
  confirmLoading: false,
});
const addFormState = ref(getInitialFormState()); // 添加表单的数据，使用完整结构进行初始化

//添加日期格式化辅助函数，接受日期格式化为YYYY-MM-DD
const getFormattedDate = (date) => {
  if (!date) return null;
  const year = date.getFullYear();
  // getMonth() 返回 0-11，所以需要 +1
  // padStart(2, '0') 用于确保月份和日期是两位数 (例如 09)
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

// 详情弹窗的状态
const detailsModal = reactive({
  visible: false,
  data: {},
});

//辅助函数，作用为将中文的企业类型转回数字
const convertTypeToNumber = (type) => {
  if (typeof type !== 'string') return type; // 如果已经是数字，直接返回
  switch(type) {
    case '养殖企业': return 1;
    case '屠宰企业': return 2;
    case '批发商': return 3;
    case '零售商': return 4;
    default: return undefined; // 或其他默认值
  }
}

//辅助函数，将城市和省份转回数字
//城市
const convertCityToNumber = (city) => {
  if (typeof city !== 'string') return city; // 如果已经是数字，直接返回
  switch (city) {
    case '北京市':
      return 1;
    case '上海市':
      return 2;
    case '广州市':
      return 3;
    case '深圳市':
      return 4;
    case '南京市':
      return 5;
    case '成都市':
      return 6;
    default:
      return undefined; // 或其他默认值
  }
}
//省份
const convertProvinceToNumber = (province) => {
  if (typeof province !== 'string') return province; // 如果已经是数字，直接返回
  switch (province) {
    case '首都':
      return 1;
    case '经济中心':
      return 2;
    case '广东省':
      return 3;
    case '江苏省':
      return 4;
    case '四川省':
      return 5;
    default:
      return undefined; // 或其他默认值
  }
}


//获取列表数据函数
//从后端API分页获取企业节点数据
const fetchData = async () => {
  loading.value = true;
  try {
    const params = {
      ...searchParams,
      pageNum: pagination.current,
      maxPageNum: pagination.pageSize,
    };
    const response = await api.post('/node/page', params);
    if (response.data && response.code === 0) {
      tableData.value = response.data.data;
      pagination.total = response.data.totalRow;
    } else {
      message.error(response.message || '获取列表失败');
    }
  } catch (error) {
    message.error('请求列表数据失败，请重新尝试。');
  } finally {
    loading.value = false;
  }
};

// 页面加载时获取初始数据
onMounted(fetchData);

// 查询和重置
const handleSearch = () => {
  pagination.current = 1;
  fetchData();
};
const handleReset = () => {
  searchParams.name = '';
  searchParams.code = '';
  searchParams.type = undefined;
  searchParams.status = undefined;
  handleSearch();
};

// 表格分页变化
const handleTableChange = (pager) => {
  pagination.current = pager.current;
  pagination.pageSize = pager.pageSize;
  fetchData();
};

// 打开添加弹窗
const openAddModal = () => {
  addFormState.value = getInitialFormState(); //重置
  addCityList.value = []; // 同时清空上一次的城市列表
  addModalState.visible = true;
};

// 打开编辑弹窗
const openEditModal = async (record) => {
  loading.value = true;
  editModalState.title = '正在加载数据...';

  try {
    //根据 record 中的 nodeId，调用详情接口获取完整、最新的数据
    const response = await api.get(`/node/detail/${record.nodeId}`);
    console.log("现在读取编辑列表的response：",response.data);

    if (response.data && response.code === 0) {
      //从响应中获取完整的企业信息对象
      const fullRecordData = response.data;

      //将后端返回的中文字段，转换为前端需要的数字ID，并添加到对象中
      fullRecordData.type = convertTypeToNumber(fullRecordData.type);
      fullRecordData.provId = convertProvinceToNumber(fullRecordData.province);
      fullRecordData.cityId = convertCityToNumber(fullRecordData.city);


      //处理城市列表的级联逻辑
      if (fullRecordData.provId) {
        editCityList.value = allCitiesList.value.filter(city => city.provId === fullRecordData.provId);
      } else {
        editCityList.value = [];
      }

      //将完整数据赋值给表单状态
      editFormState.value = fullRecordData;

      //数据加载成功后，恢复标题并显示弹窗
      editModalState.title = '更新节点企业信息';
      editModalState.visible = true;

    } else {
      message.error(response.message || '获取企业详细信息失败');
    }
  } catch (error) {
    message.error('请求企业详情失败，请重试。');
  } finally {
    loading.value = false;
  }
};

//添加watch监听，监控添加表单中的企业类型变化。
watch(
  () => addFormState.value.type, // 监听的源
  (newType, oldType) => {
    // 当类型确实发生改变时才执行，防止不必要的重置
    if (newType !== oldType) {
      console.log(`企业类型从 ${oldType} 变为 ${newType}，正在清理无关数据...`);

      // 为了安全，我们先保留用户已填写的基础信息和营业执照
      const basicInfo = {
        businessId: addFormState.value.businessId,
        corporation: addFormState.value.corporation,
        telephone: addFormState.value.telephone,
        address: addFormState.value.address,
        remarks: addFormState.value.remarks,
      };

      // 清理所有特定于类型的证书ID
      addFormState.value.epId = '';
      addFormState.value.eiaId = '';
      addFormState.value.cirId = '';
      addFormState.value.fbId = '';

      // 将保留的基础信息重新赋值回去
      Object.assign(addFormState.value, basicInfo);
    }
  }
);

//处理添加提交
const handleAddModalOk = async () => {
  // 可以在这里添加一些前端校验，比如密码不能为空
  if (!addFormState.value.code || !addFormState.value.password || !addFormState.value.name) {
    message.warn('登录编码、密码和企业名称是必填项！');
    return;
  }
  addModalState.confirmLoading = true;
  try {
    //在提交前，先读取现在日期作为注册日期
    addFormState.value.regDate = getFormattedDate(new Date());
    console.log('即将提交的表单数据:', addFormState.value);

    const response = await api.post('/node/save', addFormState.value);
    if (response.data && response.code === 0) {
      message.success('添加成功');
      addModalState.visible = false;
      handleReset(); // 刷新列表
    } else {
      message.error(response.message || '添加失败');
    }
  } catch (error) {
    message.error('请求异常');
  } finally {
    addModalState.confirmLoading = false;
  }
};

//处理更新提交
const handleEditModalOk = async () => {
  editModalState.confirmLoading = true;
  try {
    // 更新操作，发送 editFormState
    const response = await api.put('/node/edit', editFormState.value);
    if (response.data && response.code === 0) {
      message.success('更新成功');
      editModalState.visible = false;
      fetchData(); // 刷新列表
    } else {
      message.error(response.message || '更新失败');
    }
  } catch (error) {
    message.error('请求异常');
  } finally {
    editModalState.confirmLoading = false;
  }
};

// 打开详情弹窗
const openDetailsModal = async (record) => {
  try {
    const response = await api.get(`/node/detail/${record.nodeId}`);
    if (response.data && response.code === 0) {
      detailsModal.data = { ...response.data, status: record.status }; // 补充状态信息
      detailsModal.visible = true;
    } else {
      message.error(response.message || '获取详情失败');
    }
  } catch (error) {
    message.error('请求详情失败');
  }
};


// 禁用操作
const handleDisable = (record) => {
  Modal.confirm({
    title: '是否确定禁用?',
    icon: createVNode(ExclamationCircleOutlined),
    content: '若禁用企业，将下架其所关联的所有产品批号，包括关联的下游企业的产品批号，是否确定禁用？',
    onOk: async () => {
      try {
        const response = await api.put(`/node/disable/${record.nodeId}`);
        if (response && response.code === 0) {
          message.success('禁用成功');
          fetchData(); // 刷新列表
          if (detailsModal.visible) detailsModal.visible = false;
        } else {
          message.error(response.message || '禁用失败');
        }
      } catch (error) {
        message.error('请求禁用接口失败');
      }
    },
    onCancel() {},
  });
};

// 启用操作
const handleEnable = (record) => {
  Modal.confirm({
    title: '是否确定启用?',
    icon: createVNode(ExclamationCircleOutlined),
    content: '启用企业后，下架的产品批号会保持下架状态，是否确定启用？',
    onOk: async () => {
      try {
        const response = await api.put(`/node/enable/${record.nodeId}`);
        if (response && response.code === 0) {
          message.success('启用成功');
          fetchData(); // 刷新列表
          if (detailsModal.visible) detailsModal.visible = false;
        } else {
          message.error(response.message || '启用失败');
        }
      } catch (error) {
        message.error('请求启用接口失败');
      }
    },
    onCancel() {},
  });
};

// 永久删除操作
const handlePermanentDelete = (record) => {
  Modal.confirm({
    title: '是否确定永久删除?',
    icon: createVNode(ExclamationCircleOutlined),
    content: '删除后，企业所关联的下架状态产品批号将一并删除，是否确定永久删除？',
    okText: '是',
    okType: 'danger',
    cancelText: '否',
    onOk: async () => {
      try {
        const response = await api.delete(`/node/permanent/${record.nodeId}`);
        if (response && response.code === 0) {
          message.success('永久删除成功');
          fetchData(); // 刷新列表
        } else {
          message.error(response.message || '永久删除失败');
        }
      } catch (error) {
        message.error('请求永久删除接口失败');
      }
    },
  });
};

// 刷新确认
const openRefreshConfirm = () => {
  Modal.confirm({
    title: '刷新',
    content: '确定要刷新列表吗？',
    okText: '刷新',
    cancelText: '退出',
    onOk: () => {
      message.loading('正在刷新...', 0.5);
      fetchData();
    },
  });
};

</script>

<style scoped>
.page-container {
  padding: 16px;
}
</style>
