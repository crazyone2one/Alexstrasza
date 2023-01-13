import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface IAppState {
  siteName: string
  currentWorkspaceId: string
  currentProjectId: string
  workspaceName: string
  projectName: string
}

export const useAppStore = defineStore(
  'appStore',
  () => {
    const app = ref<IAppState>({
      siteName: 'one-piece',
      currentWorkspaceId: '',
      currentProjectId: '',
      workspaceName: '',
      projectName: '',
    })

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
    const setProjectName = (val: string): void => {
      app.value.projectName = val
    }
    const getProjectName = (): string => {
      return app.value.projectName
    }
    // * 重置信息
    const resetAppStore = () => {
      app.value.currentWorkspaceId = ''
      app.value.currentProjectId = ''
      app.value.workspaceName = ''
      app.value.projectName = ''
    }
    return {
      app,
      setProjectId,
      setWorkspaceId,
      getProjectId,
      getWorkspaceId,
      setWorkspaceName,
      getWorkspaceName,
      resetAppStore,
      setProjectName,
      getProjectName,
    }
  },
  {
    persist: true,
  }
)
