<template>
    <div class="mobile-wrapper">
        <div class="mobile-container">
            <div class="enterprise-container">
                <!-- 头部信息 -->
                <div class="header-section">
                    <a-button class="back-btn" type="text" @click="goBack">
                        <arrow-left-outlined />
                    </a-button>
                    <h1 class="page-title">企业信息</h1>
                </div>

                <!-- 企业信息内容 -->
                <div class="content-section">
                    <a-spin :spinning="loading">
                        <a-card class="info-card" v-if="enterpriseInfo.nodeId">
                            <!-- 基本信息 -->
                            <div class="section-title">基本信息</div>
                            <div class="info-item">
                                <span class="label">企业名称：</span>
                                <span class="value">{{ enterpriseInfo.name || '--' }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">登录编码：</span>
                                <span class="value">{{ enterpriseInfo.code || '--' }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">企业类型：</span>
                                <span class="value">{{ enterpriseInfo.type || '--' }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">所在地区：</span>
                                <span class="value">{{ getFullAddress() }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">详细地址：</span>
                                <span class="value">{{ enterpriseInfo.address || '--' }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">法定代表人：</span>
                                <span class="value">{{ enterpriseInfo.corporation || '--' }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">联系电话：</span>
                                <span class="value">{{ enterpriseInfo.telephone || '--' }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">注册日期：</span>
                                <span class="value">{{ enterpriseInfo.regDate || '--' }}</span>
                            </div>

                            <!-- 营业执照信息 -->
                            <div class="section-title">营业执照信息</div>
                            <div class="info-item">
                                <span class="label">营业执照代码：</span>
                                <span class="value">{{ enterpriseInfo.businessId || '--' }}</span>
                            </div>

                            <!-- 根据企业类型显示不同的资质信息 -->
                            <template v-if="enterpriseInfo.type && enterpriseInfo.type.includes('养殖')">
                                <div class="section-title">养殖企业资质</div>
                                <div class="info-item">
                                    <span class="label">动物防疫条件合格证：</span>
                                    <span class="value">{{ enterpriseInfo.epId || '--' }}</span>
                                </div>
                                <div class="info-item">
                                    <span class="label">环境影响评价资质：</span>
                                    <span class="value">{{ enterpriseInfo.eiaId || '--' }}</span>
                                </div>
                            </template>

                            <template v-if="enterpriseInfo.type && enterpriseInfo.type.includes('屠宰')">
                                <div class="section-title">屠宰企业资质</div>
                                <div class="info-item">
                                    <span class="label">环境影响评价资质：</span>
                                    <span class="value">{{ enterpriseInfo.eiaId || '--' }}</span>
                                </div>
                            </template>

                            <template v-if="enterpriseInfo.type && enterpriseInfo.type.includes('批发')">
                                <div class="section-title">批发企业资质</div>
                                <div class="info-item">
                                    <span class="label">食品流通许可证：</span>
                                    <span class="value">{{ enterpriseInfo.cirId || '--' }}</span>
                                </div>
                                <div class="info-item">
                                    <span class="label">食品经营许可证：</span>
                                    <span class="value">{{ enterpriseInfo.fbId || '--' }}</span>
                                </div>
                            </template>

                            <template v-if="enterpriseInfo.type && enterpriseInfo.type.includes('零售')">
                                <div class="section-title">零售企业资质</div>
                                <div class="info-item">
                                    <span class="label">食品经营许可证：</span>
                                    <span class="value">{{ enterpriseInfo.fbId || '--' }}</span>
                                </div>
                            </template>
                        </a-card>

                        <!-- 空状态 -->
                        <a-card class="empty-card" v-else>
                            <div class="empty-text">暂无企业信息</div>
                        </a-card>
                    </a-spin>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { useAuthStore } from '@/stores/auth.ts'
import { getFarmInfoById } from '@/api/farmBatchController.ts'
import { getSlauInfoById } from '@/api/slauBatchController.ts'
import { getWholInfoById } from '@/api/wholBatchController.ts'
import { getRetaInfoById } from '@/api/retaBatchController.ts'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const enterpriseInfo = ref<any>({})

const goBack = () => {
    router.back()
}

const getFullAddress = () => {
    const { province, city } = enterpriseInfo.value
    if (province && city) {
        return `${province}${city}`
    } else if (province) {
        return province
    } else if (city) {
        return city
    }
    return '--'
}

const loadEnterpriseInfo = async () => {
    try {
        loading.value = true
        console.log('开始加载企业信息...')

        const nodeId = authStore.getNodeId()
        console.log('当前节点ID:', nodeId)

        if (!nodeId) {
            message.error('无法获取企业ID')
            return
        }

        // 根据当前企业类型调用不同的 API
        let response
        const enterpriseType = authStore.getCurrentEnterpriseType()
        console.log('企业类型:', enterpriseType)

        switch (enterpriseType) {
            case 1: // 养殖企业
                console.log('调用养殖企业API...')
                response = await getFarmInfoById({ farmId: nodeId })
                break
            case 2: // 屠宰企业
                console.log('调用屠宰企业API...')
                response = await getSlauInfoById({ slauId: nodeId })
                break
            case 3: // 批发企业
                console.log('调用批发企业API...')
                response = await getWholInfoById({ wholId: nodeId })
                break
            case 4: // 零售企业
                console.log('调用零售企业API...')
                response = await getRetaInfoById({ retaId: nodeId })
                break
            default:
                message.error('未知的企业类型')
                return
        }

        console.log('API响应:', response)

        if (response.code === 0 && response.data) {
            enterpriseInfo.value = response.data
            console.log('企业信息设置成功:', enterpriseInfo.value)
        } else {
            message.error(response.message || '获取企业信息失败')
        }
    } catch (error) {
        console.error('获取企业信息失败:', error)
        message.error('获取企业信息失败')
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    console.log('企业信息页面挂载')
    loadEnterpriseInfo()
})
</script>

<style scoped>
.mobile-wrapper {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    min-height: 100vh;
    background: #f0f2f5;
    padding: 20px;
}

.mobile-container {
    width: 100%;
    max-width: 414px;
    height: 100vh;
    background: white;
    border-radius: 16px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
    overflow: hidden;
    position: relative;
}

.enterprise-container {
    height: 100%;
    display: flex;
    flex-direction: column;
}

.header-section {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 16px 20px;
    color: white;
    text-align: center;
    position: relative;
    flex-shrink: 0;
}

.back-btn {
    position: absolute;
    left: 16px;
    top: 50%;
    transform: translateY(-50%);
    color: white;
    border: none;
    background: transparent;
}

.back-btn:hover {
    background: rgba(255, 255, 255, 0.1);
}

.page-title {
    font-size: 18px;
    font-weight: 600;
    margin: 0;
    color: white;
}

.content-section {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
}

.info-card {
    border-radius: 12px;
    border: 1px solid #f0f0f0;
}

:deep(.info-card .ant-card-body) {
    padding: 20px;
}

.empty-card {
    border-radius: 12px;
    border: 1px solid #f0f0f0;
    text-align: center;
    padding: 40px 20px;
}

.empty-text {
    color: #999;
    font-size: 16px;
}

.section-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin: 20px 0 12px 0;
    padding-bottom: 8px;
    border-bottom: 2px solid #667eea;
}

.section-title:first-child {
    margin-top: 0;
}

.info-item {
    display: flex;
    margin-bottom: 16px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f5f5f5;
}

.info-item:last-child {
    margin-bottom: 0;
    padding-bottom: 0;
    border-bottom: none;
}

.label {
    font-weight: 500;
    color: #666;
    min-width: 140px;
    flex-shrink: 0;
}

.value {
    color: #333;
    flex: 1;
    word-break: break-all;
}

/* 响应式调整 */
@media (max-width: 320px) {
    .mobile-container {
        height: 568px;
    }

    .content-section {
        padding: 16px;
    }

    .header-section {
        padding: 14px 16px;
    }

    .label {
        min-width: 120px;
    }
}
</style>
