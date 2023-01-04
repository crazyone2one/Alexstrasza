import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface IAppState {
  sidebarCollapsed: boolean
  currentWorkspaceId: string
  currentProjectId: string
  workspaceName: string
}

export const useAppStore = defineStore(
  'appStore',
  () => {
    const app = ref<IAppState>({
      sidebarCollapsed: false,
      currentWorkspaceId: '',
      currentProjectId: '',
      workspaceName: '',
    })
    const setCollapsed = (): void => {
      app.value.sidebarCollapsed = !app.value.sidebarCollapsed
    }
    const setProjectId = (projectId: string): void => {
      app.value.currentProjectId = projectId
    }
    const getProjectId = (): string => {
      return app.value.currentProjectId
    }
    const setWorkspaceId = (workspaceId: string): void => {
      app.value.currentWorkspaceId = workspaceId
    }

    const getWorkspaceId = (): string => {
      return app.value.currentWorkspaceId
    }
    const setWorkspaceName = (workspaceName: string): void => {
      app.value.workspaceName = workspaceName
    }
    const getWorkspaceName = (): string => {
      return app.value.workspaceName
    }
    return {
      app,
      setCollapsed,
      setProjectId,
      setWorkspaceId,
      getProjectId,
      getWorkspaceId,
      setWorkspaceName,
      getWorkspaceName,
    }
  },
  {
    persist: true,
  }
)
