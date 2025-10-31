<template>
    <div class="mobile-wrapper">
        <div class="mobile-container">
            <div class="password-container">
                <!-- 头部信息 -->
                <div class="header-section">
                    <a-button class="back-btn" type="text" @click="goBack">
                        <arrow-left-outlined />
                    </a-button>
                    <h1 class="page-title">更新密码</h1>
                </div>

                <!-- 密码更新表单 -->
                <div class="form-section">
                    <a-card class="form-card">
                        <a-form
                                :model="formState"
                                :rules="rules"
                                @finish="onSubmit"
                                layout="vertical"
                        >
                            <a-form-item label="原密码" name="oldPassword">
                                <a-input-password
                                        v-model:value="formState.oldPassword"
                                        placeholder="请输入原密码"
                                        size="large"
                                />
                            </a-form-item>

                            <a-form-item label="新密码" name="newPassword">
                                <a-input-password
                                        v-model:value="formState.newPassword"
                                        placeholder="请输入新密码"
                                        size="large"
                                />
                            </a-form-item>

                            <a-form-item label="确认新密码" name="confirmPassword">
                                <a-input-password
                                        v-model:value="formState.confirmPassword"
                                        placeholder="请再次输入新密码"
                                        size="large"
                                />
                            </a-form-item>

                            <a-form-item>
                                <a-button
                                        type="primary"
                                        html-type="submit"
                                        size="large"
                                        :loading="loading"
                                        block
                                >
                                    更新密码
                                </a-button>
                            </a-form-item>
                        </a-form>
                    </a-card>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message, type FormRule } from 'ant-design-vue'
import { ArrowLeftOutlined } from '@ant-design/icons-vue'
import { updatePassword } from '@/api/nodeInfoController.ts'
import { useAuthStore } from '@/stores/auth.ts'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)

interface FormState {
    oldPassword: string
    newPassword: string
    confirmPassword: string
}

const formState = reactive<FormState>({
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
})

const validateConfirmPassword = async (_rule: FormRule, value: string) => {
    if (value === '') {
        return Promise.reject('请确认新密码')
    } else if (value !== formState.newPassword) {
        return Promise.reject('两次输入的密码不一致')
    } else {
        return Promise.resolve()
    }
}

const rules = {
    oldPassword: [
        { required: true, message: '请输入原密码', trigger: 'blur' },
        { min: 6, message: '密码至少6位字符', trigger: 'blur' }
    ],
    newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, message: '密码至少6位字符', trigger: 'blur' },
        {
            validator: (_rule: FormRule, value: string) => {
                if (value && value === formState.oldPassword) {
                    return Promise.reject('新密码不能与原密码相同')
                }
                return Promise.resolve()
            },
            trigger: 'blur'
        }
    ],
    confirmPassword: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' }
    ]
}

const goBack = () => {
    router.back()
}

const onSubmit = async () => {
    try {
        loading.value = true

        // 获取当前用户的 nodeId
        const nodeId = authStore.getNodeId()
        if (!nodeId) {
            message.error('无法获取企业信息，请重新登录')
            return
        }

        console.log('提交密码更新请求:', {
            nodeId,
            oldPassword: formState.oldPassword,
            newPassword: formState.newPassword
        })

        // 调用更新密码 API
        const response = await updatePassword({
            nodeId: nodeId,
            oldPassword: formState.oldPassword,
            newPassword: formState.newPassword
        })

        console.log('密码更新响应:', response)

        if (response.code === 200 || response.code === 0) {
            message.success('密码更新成功')
            // 清空表单
            formState.oldPassword = ''
            formState.newPassword = ''
            formState.confirmPassword = ''
            // 返回上一页
            setTimeout(() => {
                router.back()
            }, 1000)
        } else {
            message.error(response.message || '密码更新失败')
        }
    } catch (error: any) {
        console.error('密码更新失败:', error)

        // 更详细的错误处理
        if (error.response) {
            // 服务器响应了错误状态码
            const errorData = error.response.data
            if (errorData && errorData.message) {
                message.error(`密码更新失败: ${errorData.message}`)
            } else {
                message.error('密码更新失败，请检查原密码是否正确')
            }
        } else if (error.request) {
            // 请求发送了但没有收到响应
            message.error('网络错误，请检查网络连接')
        } else {
            // 其他错误
            message.error('密码更新失败，请重试')
        }
    } finally {
        loading.value = false
    }
}
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

.password-container {
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

.form-section {
    flex: 1;
    padding: 24px 20px;
    display: flex;
    align-items: flex-start;
}

.form-card {
    width: 100%;
    border-radius: 12px;
    border: 1px solid #f0f0f0;
}

:deep(.form-card .ant-card-body) {
    padding: 24px;
}

:deep(.ant-form-item-label > label) {
    font-weight: 500;
    color: #333;
}

:deep(.ant-input-affix-wrapper) {
    border-radius: 8px;
}

:deep(.ant-btn) {
    border-radius: 8px;
    height: 44px;
    font-weight: 500;
}

/* 响应式调整 */
@media (max-width: 320px) {
    .mobile-container {
        height: 568px;
    }

    .form-section {
        padding: 20px 16px;
    }

    .header-section {
        padding: 14px 16px;
    }

    :deep(.form-card .ant-card-body) {
        padding: 20px;
    }
}
</style>
