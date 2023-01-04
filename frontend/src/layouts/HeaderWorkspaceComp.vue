<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { NDropdown, SelectOption, DropdownOption } from 'naive-ui'
import { useAppStore } from '/@/store/modules/app'

import { useI18n } from 'vue-i18n'
import { getUserWorkspaceList, IWorkspace, switchWorkspace } from '../apis/modules/workspace'

const { t } = useI18n()
const appStore = useAppStore()
// const userStore = useUserInfoStore()
const workspaceId = computed(() => {
  return appStore.getWorkspaceId()
})
// const currentUserId = ref(userStore.getSessionUser().id)
const workspaceList = ref<IWorkspace[]>([])
// const workspaceIds = ref<string[]>([])
const currentWorkspaceName = ref(appStore.getWorkspaceName())
const wsListCopy = ref([{ name: t('workspace.none') }])
const options: SelectOption[] = []

const initMenuData = () => {
  getUserWorkspaceList().then((resp) => {
    workspaceList.value = resp.data
    wsListCopy.value = resp.data
    const workspace = resp.data.filter((r) => r.id === workspaceId.value)
    if (workspace.length > 0) {
      currentWorkspaceName.value = workspace[0].name
      workspaceList.value = resp.data.filter((r) => r.id !== workspaceId.value)
      workspaceList.value.unshift(workspace[0])
    }
    workspaceList.value.forEach((ele) => {
      const option: SelectOption = {}
      option.label = ele.name
      option.key = ele.id
      options.push(option)
    })
  })
}
const handleSelect = (key: string | number, option: DropdownOption) => {
  const _workspaceId = key
  if (!_workspaceId || workspaceId.value === _workspaceId) {
    return false
  }
  if (_workspaceId) {
    currentWorkspaceName.value = option.label as string
    switchWorkspace(_workspaceId as string).then((resp) => {
      appStore.setProjectId(resp.data.lastProjectId as string)
      appStore.setWorkspaceId(resp.data.lastWorkspaceId as string)
    })
  }
}
onMounted(() => {
  initMenuData()
})
watch(
  () => currentWorkspaceName.value,
  (newValue) => {
    appStore.setWorkspaceName(newValue)
  }
)
</script>
<template>
  <n-dropdown trigger="hover" :options="options" @select="handleSelect">
    <!-- <n-button>找个地方休息</n-button> -->
    <span class="dropdown-link">{{ currentWorkspaceName }}</span>
  </n-dropdown>
</template>

<style scoped>
.dropdown-link {
  cursor: pointer;
  font-size: 12px;
  line-height: 40px;
  padding-right: 10px;
  padding-left: 5px;
}
</style>
