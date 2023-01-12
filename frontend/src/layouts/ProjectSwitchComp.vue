<script setup lang="ts">
import { ref, onMounted, computed, getCurrentInstance } from 'vue'
import { SelectOption, DropdownOption, NPopselect } from 'naive-ui'
import { useAppStore } from '/@/store/modules/app'
import { useUserInfoStore } from '../store/modules/user'
import { getUserProjectList, IProject, switchProject } from '../apis/modules/project'

const instance = getCurrentInstance()
const currentProject = ref('/(ㄒoㄒ)/~~暂未选择项目')
const appStore = useAppStore()
const userStore = useUserInfoStore()
const options: SelectOption[] = []
const projectList = ref<IProject[]>([])
const projectId = computed(() => {
  return appStore.getProjectId()
})
const currentProjectId = ref(projectId.value)
const handleSelect = (key: string, option: DropdownOption) => {
  const _projectId = key
  if (!_projectId || projectId.value === _projectId) {
    return false
  }
  change(_projectId)
}
const change = (projectId: string) => {
  const condition = {
    id: userStore.getSessionUser().id,
    lastProjectId: projectId as string,
  }
  switchProject(condition).then((res) => {
    appStore.setProjectId(res.data.lastProjectId as string)
    // instance?.proxy?.$Bus.emit('projectChange')
    changeProjectName(projectId)
  })
}
const changeProjectName = (projectId: string) => {
  if (projectId) {
    const project = projectList.value.filter((p) => p.id === appStore.getProjectId())
    if (project.length > 0) {
      currentProject.value = project[0].name
    }
  } else {
    currentProject.value = 'no project data'
  }
}
// * 初始化数据
const initData = () => {
  projectList.value = []
  const condition = {
    userId: userStore.getSessionUser().id,
    workspaceId: appStore.getWorkspaceId(),
  }
  getUserProjectList(condition).then((res) => {
    projectList.value = res.data
    const _projectId = appStore.getProjectId()
    if (_projectId) {
      if (projectList.value.length > 0 && projectList.value.map((p) => p.id).indexOf(_projectId) > -1) {
        change(projectList.value[0].id as string)
      }
    } else {
      if (projectList.value.length > 0) {
        change(projectList.value[0].id as string)
      }
    }
    projectList.value.forEach((ele) => {
      const option: SelectOption = {}
      option.label = ele.name
      option.value = ele.id
      options.push(option)
    })
    changeProjectName(_projectId)
  })
}

instance?.proxy?.$Bus.on('projectChange', () => {
  initData()
})
onMounted(() => {
  initData()
})
</script>
<template>
  <n-popselect v-model:value="currentProjectId" :options="options" trigger="click" @update:value="handleSelect">
    <span class="dropdown-link">{{ $t('commons.project') }}:{{ currentProject }}</span>
  </n-popselect>
</template>

<style scoped>
.project-name {
  display: inline-block;
  width: 160px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
