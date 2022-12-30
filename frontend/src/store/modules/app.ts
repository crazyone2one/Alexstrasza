import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface IAppState {
  sidebarCollapsed: boolean
}

export const useAppStore = defineStore(
  'appStore',
  () => {
    const app = ref<IAppState>({
      sidebarCollapsed: false,
    })
    const setCollapsed = (): void => {
      app.value.sidebarCollapsed = !app.value.sidebarCollapsed
    }
    return { app, setCollapsed }
  },
  {
    persist: true,
  }
)
