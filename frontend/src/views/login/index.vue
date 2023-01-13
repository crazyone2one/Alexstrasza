<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElForm, ElFormItem, ElInput, ElButton, FormInstance, FormRules, ElNotification } from 'element-plus'
import { useI18n } from 'vue-i18n'
import CryptoJS from 'crypto-js'
import { formatAxis } from '/@/utils/format-time'
import { useRoute, useRouter } from 'vue-router'

const { t } = useI18n()
const loginFormRef = ref<FormInstance>()
const loading = reactive({
  signIn: false,
})
const form = reactive({
  name: '',
  password: '',
})
const rules = reactive<FormRules>({
  name: [{ required: true, message: t('commons.input_login_username'), trigger: 'blur' }],
  password: [
    { required: true, message: t('commons.input_password'), trigger: 'blur' },
    { min: 6, max: 30, message: t('commons.input_limit', [6, 30]), trigger: 'blur' },
  ],
})
const route = useRoute()
const router = useRouter()
// 时间获取
const currentTime = computed(() => {
  return formatAxis(new Date())
})
const signInSuccess = () => {
  const currentTimeInfo = currentTime.value
  // 如果是复制粘贴的路径，非首页/登录页，那么登录成功后重定向到对应的路径中
  if (route.query?.redirect) {
    router.push({
      path: route.query?.redirect as string,
      query: { ...route.query },
    })
  } else {
    router.push('/')
  }
  // 登录成功提示
  const signInText = t('login.signInText')
  ElNotification.success({
    message: `${currentTimeInfo}，${signInText}`,
    showClose: false,
  })
}
// * 登录
const handleSubmit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      loading.signIn = true
      const params = {
        name: form.name,
        password: CryptoJS.MD5(form.password).toString(),
      }
      console.log(params)
      signInSuccess()
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>
<template>
  <div class="login-container flx-center">
    <div class="login-box">
      <div class="login-form">
        <div class="login-logo">
          <img class="login-icon" src="../../assets/title-logo.svg" alt="" />
          <h2 class="logo-text">alexstrasza</h2>
        </div>
        <el-form ref="loginFormRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item prop="name">
            <el-input v-model="form.name" clearable :placeholder="$t('commons.login_username')" autofocus>
              <template #prefix>
                <Icon name="fa-solid fa-user" class="form-item-icon" size="16" color="var(--el-input-icon-color)" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              show-password
              clearable
              :placeholder="$t('commons.password')"
              maxlength="30"
              show-word-limit
            >
              <template #prefix>
                <Icon name="fa fa-unlock-alt" class="form-item-icon" size="16" color="var(--el-input-icon-color)" />
              </template>
            </el-input>
          </el-form-item>
        </el-form>
        <div class="login-btn">
          <el-button type="primary" round :loading="loading.signIn" @click="handleSubmit(loginFormRef)">
            {{ $t('commons.login') }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import './index.scss';
</style>
