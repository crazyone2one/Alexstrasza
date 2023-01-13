import axios from 'axios'
import { useUserInfoStore } from '../store/modules/user'
import { useAppStore } from '../store/modules/app'
import { ElMessage, ElMessageBox } from 'element-plus/es/components'

const config = {
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // 设置超时时间
  timeout: 50000,
  // 跨域时候允许携带凭证
  withCredentials: true,
}
// 允许跨域
axios.defaults.headers.post['Access-Control-Allow-Origin-Type'] = '*'

const axiosInstance = axios.create(config)
// Add a request interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    const appStore = useAppStore()
    const userStore = useUserInfoStore()
    const token = userStore.getToken()
    if (token) {
      config.headers = {
        ...config.headers,
        Authorization: `Bearer ${token}`,
        // 包含 工作空间 项目的标识
        WORKSPACE: appStore.getWorkspaceId(),
        PROJECT: appStore.getProjectId(),
      }
    }
    return config
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error)
  }
)
// Add a response interceptor
axiosInstance.interceptors.response.use(
  (response) => {
    const { responseType } = response.config
    // 不拦截blob
    if (responseType === 'blob') {
      return response
    }
    if (response.data.code === 200) {
      return Promise.resolve(response.data)
    } else {
      return Promise.reject(response)
    }
  },
  async (error) => {
    const { code, msg } = error.response.data
    if (error.response?.status === 500) {
      ElMessage.error('系统错误，联系管理员')
    }
    if ([400, 401].includes(error.response?.status as number)) {
      // eslint-disable-next-line @typescript-eslint/restrict-template-expressions
      ElMessage.error(`${error.response?.data.message}`)
    }
    if (error.response.status === 403) {
      const userStore = useUserInfoStore()
      ElMessageBox.confirm('当前页面已失效，请重新登录', '', {
        confirmButtonText: 'OK',
        type: 'warning',
      }).then(() => {
        userStore.resetAuthStore()
        window.location.href = '/'
      })
    }
    return await Promise.reject(error)
  }
)
export default axiosInstance
