<!-- eslint-disable import/no-absolute-path -->
<script setup lang="ts">
import { NForm, NFormItemRow, NInput, NButton, FormInst } from 'naive-ui'
import { ref } from 'vue'
import CryptoJS from 'crypto-js'
import { loginApi } from '/@/apis/modules/user'
import { useUserInfoStore } from '/@/store/modules/user'
import { useRouter } from 'vue-router'

interface ModelType {
  name: string | null
  password: string | null
  email?: string | null
}
const userInfo = useUserInfoStore()
const formRef = ref<FormInst | null>(null)
const model = ref<ModelType>({
  name: null,
  password: null,
})
const loading = ref(false)
const router = useRouter()
/**
 * 登录功能
 * @param e
 */
const handleLogin = async (e: Event) => {
  e.preventDefault()
  loading.value = true
  formRef.value?.validate((errors) => {
    if (!errors) {
      const params = {
        name: model.value.name as string,
        password: CryptoJS.MD5(model.value.password as string).toString(),
      }
      loginApi(params).then((resp) => {
        userInfo.$patch((state) => {
          state.user.id = resp.data.userId as string
          state.user.token = resp.data.token
          state.user.username = resp.data.username
          state.user.permissions = resp.data.authorities
        })
        loading.value = false
        const route = router.currentRoute.value
        const redirect = route.query.redirect?.toString()
        router.replace(redirect ?? route.redirectedFrom?.fullPath ?? '/')
        window.$notification?.success({
          content: `欢迎回来, ${resp.data.username}`,
          duration: 2500,
          keepAliveOnHover: true,
        })
      })
    }
  })
}
</script>
<template>
  <div class="login-container flx-center">
    <div class="login-box">
      <div class="login-form">
        <div class="login-logo">
          <!-- <img class="login-icon" src="../../assets/vue.svg" alt="" /> -->
          <h2 class="logo-text">Alexstrasza</h2>
        </div>
        <n-form ref="formRef" :model="model">
          <n-form-item-row label="用户名" path="name">
            <n-input v-model:value="model.name" />
          </n-form-item-row>
          <n-form-item-row label="密码" path="password">
            <n-input v-model:value="model.password" />
          </n-form-item-row>
        </n-form>
        <n-button type="primary" block secondary strong @click="handleLogin"> 登录 </n-button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.login-container {
  position: relative;
  min-width: 550px;
  height: 100%;
  min-height: 500px;
  background-color: #eeeeee;
  background-image: url('/@/assets/login_bg.svg');
  background-position: 50%;
  background-size: 100% 100%;
  background-size: cover;
  .login-box {
    box-sizing: border-box;
    display: flex;
    align-items: center;
    justify-content: space-around;
    width: 96%;
    height: 94%;
    padding: 0 4% 0 50px;
    background-color: hsl(0deg 0% 100% / 80%);
    border-radius: 10px;
    .login-form {
      width: 420px;
      padding: 50px 40px 45px;
      border-radius: 10px;
      box-shadow: 2px 3px 7px rgb(0 0 0 / 20%);
      .login-logo {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 45px;
        .login-icon {
          width: 60px;
          height: 52px;
        }
        .logo-text {
          padding: 0 0 0 25px;
          margin: 0;
          font-size: 42px;
          font-weight: bold;
          color: #34495e;
          white-space: nowrap;
        }
      }
      .login-btn {
        display: flex;
        justify-content: space-between;
        white-space: nowrap;
        .n-button {
          width: 100%;
        }
      }
    }
  }
}
</style>
