import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface IUserState {
  id: string
  token: string
  username: string
  permissions: any[]
}

export const useUserInfoStore = defineStore(
  'userInfoStore',
  () => {
    const user = ref<IUserState>({
      id: '',
      username: '',
      token: '',
      permissions: [],
    })
    /**
     * 获取token值
     * @returns token
     */
    const getToken = (): string => {
      return user.value.token
    }
    const getSessionUser = (): IUserState => {
      return user.value
    }
    /**
     * 重置用户信息
     */
    const resetAuthStore = (): void => {
      user.value.id = ''
      user.value.username = ''
      user.value.token = ''
      user.value.permissions = []
    }
    const logout = () => {
      resetAuthStore()
      return Promise.resolve('')
    }
    return { user, getToken, getSessionUser, resetAuthStore, logout }
  },
  {
    persist: true,
  }
)
