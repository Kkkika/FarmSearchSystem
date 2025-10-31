
<template>
  <a-layout class="layout-container">
    <!-- 侧边栏 -->
    <a-layout-sider width="220" theme="dark">
      <a-menu
        theme="dark"
        mode="inline"
        :selectedKeys="[activeView]"
        @click="onMenuSelect"
      >
        <a-menu-item key="month-stats">
          <template #icon><LineChartOutlined /></template>
          <span>月份注册人数</span>
        </a-menu-item>
        <a-menu-item key="province-stats">
          <template #icon><BarChartOutlined /></template>
          <span>省份注册人数</span>
        </a-menu-item>
        <a-menu-item key="type-stats">
          <template #icon><PieChartOutlined /></template>
          <span>企业类型注册人数</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>

    <!-- 主内容区 -->
    <a-layout-content class="layout-content">

      <!-- 页面头部 -->
      <div class="page-header" v-if="activeView !== 'welcome'">
        <h2>{{ currentViewTitle }}</h2>
        <a-button type="primary" @click="handleRefresh">
          <template #icon><ReloadOutlined /></template>
          刷新
        </a-button>
      </div>

      <a-spin :spinning="loading" tip="图表加载中...">
        <!-- 欢迎页 -->
        <div v-if="activeView === 'welcome'" class="welcome-container">
          <DashboardOutlined style="font-size: 64px; color: #1890ff;" />
          <h1>欢迎使用数据统计看板</h1>
          <p>请从左侧菜单中选择一个统计维度进行查看。</p>
        </div>

        <!-- 月度注册趋势 -->
        <div v-if="activeView === 'month-stats'">
          <a-card :bordered="false">
            <div ref="monthlyTrendChartRef" class="chart-item"></div>
          </a-card>
        </div>

        <!-- 省份注册人数 -->
        <div v-if="activeView === 'province-stats'">
          <a-row :gutter="[24, 24]">
            <a-col :xs="24" :md="12">
              <a-card :bordered="false">
                <div ref="provinceBarChartRef" class="chart-item"></div>
              </a-card>
            </a-col>
            <a-col :xs="24" :md="12">
              <a-card :bordered="false">
                <div ref="provincePieChartRef" class="chart-item"></div>
              </a-card>
            </a-col>
          </a-row>
        </div>

        <!-- 企业类型注册人数 -->
        <div v-if="activeView === 'type-stats'">
          <a-card :bordered="false">
            <div ref="typePieChartRef" class="chart-item-pie"></div>
          </a-card>
        </div>
      </a-spin>
    </a-layout-content>
  </a-layout>
</template>


<script setup lang="ts">
// 节点企业注册信息统计看板 (最终修正版 - 确保月份为折线图)
import { ref, onMounted, onBeforeUnmount, nextTick, computed } from 'vue';
import api from '@/api/index';
import * as echarts from 'echarts';
import type { ECharts } from 'echarts';
import { message } from 'ant-design-vue';
import {
  LineChartOutlined,
  PieChartOutlined,
  BarChartOutlined,
  DashboardOutlined,
  ReloadOutlined,
} from '@ant-design/icons-vue';

//响应式状态定义
const activeView = ref<string>('welcome');
const loading = ref(false);

const monthlyTrendChartRef = ref<HTMLElement | null>(null);
const typePieChartRef = ref<HTMLElement | null>(null);
const provinceBarChartRef = ref<HTMLElement | null>(null);
const provincePieChartRef = ref<HTMLElement | null>(null);

const chartInstances: { [key: string]: ECharts | null } = {};

const typeMap: { [key: number]: string } = {
  1: '养殖企业',
  2: '屠宰企业',
  3: '批发商',
  4: '零售商',
};

//动态计算当前视图的标题
const currentViewTitle = computed(() => {
  switch (activeView.value) {
    case 'month-stats': return '月份注册人数统计';
    case 'province-stats': return '省份注册人数统计';
    case 'type-stats': return '企业类型注册人数统计';
    default: return '数据统计看板';
  }
});

//ECharts图表渲染方法
const initMonthlyTrendChart = async () => {
  if (!monthlyTrendChartRef.value) return;
  try {
    const res = await api.get('/node/stats/month');
    if (res.code === 0) {
      const chart = echarts.init(monthlyTrendChartRef.value);
      chart.setOption({
        title: { text: '近十二个月注册趋势' },
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: res.data.map((item: any) => item.month), boundaryGap: false },
        yAxis: { type: 'value', name: '数量' },
        series: [{
          name: '注册数量',
          type: 'line', //确保是折线图
          data: res.data.map((item: any) => item.total),
          smooth: true,
          areaStyle: {}
        }]
      });
      chartInstances.monthly = chart;
    } else { message.error(res.message || '月度趋势数据加载失败'); }
  } catch (error) { console.error('月度趋势图表加载失败:', error); }
};

const initTypePieChart = async () => {
  if (!typePieChartRef.value) return;
  try {
    const res = await api.get('/node/stats/type');
    if (res.code === 0) {
      const chart = echarts.init(typePieChartRef.value);
      chart.setOption({
        title: { text: '企业类型分布' },
        tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
        legend: { top: 'bottom', type: 'scroll' },
        series: [{
          name: '企业类型',
          type: 'pie', //确保是饼图
          radius: ['45%', '70%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: false,
          data: res.data.map((item: any) => ({ name: typeMap[item.type] || '未知类型', value: item.total })),
        }]
      });
      chartInstances.type = chart;
    } else { message.error(res.message || '企业类型数据加载失败'); }
  } catch (error) { console.error('企业类型图表加载失败:', error); }
};

const initProvinceCharts = async () => {
  if (!provinceBarChartRef.value || !provincePieChartRef.value) return;
  try {
    const res = await api.get('/node/stats/prov');
    if (res.code === 0) {
      const barChart = echarts.init(provinceBarChartRef.value);
      barChart.setOption({
        title: { text: '各省份注册数量' },
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
        xAxis: { type: 'category', data: res.data.map((item: any) => item.provName), axisLabel: { interval: 0, rotate: 30 } },
        yAxis: { type: 'value' },
        dataZoom: [{ type: 'inside', start: 0, end: res.data.length > 10 ? 50 : 100 }, { type: 'slider' }],
        series: [{ name: '注册数量', type: 'bar', data: res.data.map((item: any) => item.total) }],
      });
      chartInstances.provBar = barChart;

      const pieChart = echarts.init(provincePieChartRef.value);
      pieChart.setOption({
        title: { text: '省份注册占比' },
        tooltip: { trigger: 'item', formatter: '{a} <br/>{b} : {c} ({d}%)' },
        legend: { orient: 'vertical', left: 'left', top: '10%', type: 'scroll' },
        series: [{
          name: '企业数量', type: 'pie', radius: '65%', center: ['65%', '55%'],
          data: res.data.map((item: any) => ({ name: item.provName, value: item.total })),
        }],
      });
      chartInstances.provPie = pieChart;
    } else { message.error(res.message || '省份统计数据加载失败'); }
  } catch (error) { console.error('省份图表加载失败:', error); }
};

//=========== 4. 核心交互逻辑 ===========
const onMenuSelect = async ({ key }: { key: string }) => {
  activeView.value = key;
  loading.value = true;
  await nextTick();
  try {
    switch (key) {
      case 'month-stats': if (!chartInstances.monthly) await initMonthlyTrendChart(); break;
      case 'province-stats': if (!chartInstances.provBar && !chartInstances.provPie) await initProvinceCharts(); break;
      case 'type-stats': if (!chartInstances.type) await initTypePieChart(); break;
    }
  } finally {
    loading.value = false;
    handleResize();
  }
};

const handleRefresh = async () => {
  const currentView = activeView.value;
  if (currentView === 'welcome') return;
  loading.value = true;
  message.loading('正在刷新数据...', 0.5);
  await nextTick();
  try {
    switch (currentView) {
      case 'month-stats':
        if (chartInstances.monthly) { chartInstances.monthly.dispose(); chartInstances.monthly = null; }
        await initMonthlyTrendChart();
        break;
      case 'province-stats':
        if (chartInstances.provBar) { chartInstances.provBar.dispose(); chartInstances.provBar = null; }
        if (chartInstances.provPie) { chartInstances.provPie.dispose(); chartInstances.provPie = null; }
        await initProvinceCharts();
        break;
      case 'type-stats':
        if (chartInstances.type) { chartInstances.type.dispose(); chartInstances.type = null; }
        await initTypePieChart();
        break;
    }
    message.success('刷新成功！');
  } catch (error) {
    message.error('刷新失败');
    console.error("Refresh failed:", error);
  } finally {
    loading.value = false;
  }
};

//图表自适应与生命周期管理
const handleResize = () => { nextTick(() => { for (const key in chartInstances) { chartInstances[key]?.resize(); } }); };
onMounted(() => { window.addEventListener('resize', handleResize); });
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
  for (const key in chartInstances) { chartInstances[key]?.dispose(); }
});
</script>

<style scoped>
.layout-container { min-height: 100vh; }
.layout-content { padding: 24px; background-color: #f0f2f5; overflow-y: auto; height: 100vh; }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 16px 24px; background-color: #fff; border-radius: 4px; margin-bottom: 24px; }
.page-header h2 { margin: 0; font-size: 20px; font-weight: 500; }
.welcome-container { display: flex; flex-direction: column; align-items: center; justify-content: center; height: calc(100vh - 48px); text-align: center; background-color: #fff; border-radius: 4px; }
.welcome-container h1 { font-size: 24px; margin-top: 24px; color: #333; }
.welcome-container p { font-size: 16px; color: #888; }
.ant-card { border-radius: 4px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
:deep(.ant-card-body) { padding: 24px !important; }
.chart-item { width: 100%; height: 450px; }
.chart-item-pie { width: 100%; height: 500px; }
</style>
