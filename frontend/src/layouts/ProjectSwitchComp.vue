<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { NDropdown, SelectOption, DropdownOption } from 'naive-ui'
import { useAppStore } from '/@/store/modules/app'
import { useUserInfoStore } from '../store/modules/user'
import { getUserProjectList, IProject, switchProject } from '../apis/modules/project'

const currentProject = ref('/(ㄒoㄒ)/~~暂未选择项目')
const appStore = useAppStore()
const userStore = useUserInfoStore()
const options: SelectOption[] = []
const projectList = ref<IProject[]>([])
const currentProjectId = computed(() => {
  return appStore.getProjectId()
})
const handleSelect = (key: string | number, option: DropdownOption) => {
  const _projectId = key
  if (!_projectId || currentProjectId.value === _projectId) {
    return false
  }
  const condition = {
    id: userStore.getSessionUser().id,
    lastProjectId: _projectId as string,
  }
  switchProject(condition).then((res) => {
    appStore.setProjectId(res.data.lastProjectId as string)
    changeProjectName(_projectId)
  })
}
const changeProjectName = (projectId: string) => {
  if (projectId) {
    const project = projectList.value.filter((p) => p.id === appStore.getProjectId())
    if (project.length > 0) {
      currentProject.value = project[0].name
    }
  }
}
// * 初始化数据
const initData = () => {
  const condition = {
    userId: userStore.getSessionUser().id,
    workspaceId: appStore.getWorkspaceId(),
  }
  getUserProjectList(condition).then((res) => {
    console.log(res)
    projectList.value = res.data
    const _projectId = appStore.getProjectId()
    // if (_projectId) {

    // }
    projectList.value.forEach((ele) => {
      const option: SelectOption = {}
      option.label = ele.name
      option.key = ele.id
      options.push(option)
    })
    changeProjectName(_projectId)
  })
}
onMounted(() => {
  initData()
})
</script>
<template>
  <n-dropdown trigger="hover" :options="options" @select="handleSelect">
    <!-- <n-button>找个地方休息</n-button> -->
    <span class="dropdown-link">{{ currentProject }}</span>
  </n-dropdown>
</template>

<style scoped></style>
