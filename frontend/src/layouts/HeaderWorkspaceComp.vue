<script setup lang="ts">
import { ref, computed, onMounted, watch, getCurrentInstance } from 'vue'
import { NDropdown, SelectOption, DropdownOption, NPopselect } from 'naive-ui'
import { useAppStore } from '/@/store/modules/app'
import IconComponent from '../components/IconComponent.vue'
import { useI18n } from 'vue-i18n'
import { getUserWorkspaceList, IWorkspace, switchWorkspace } from '../apis/modules/workspace'

const { t } = useI18n()
const appStore = useAppStore()
const workspaceId = computed(() => {
  return appStore.getWorkspaceId()
})
const instance = getCurrentInstance()

const currentWorkspaceId = ref(workspaceId.value)
const workspaceList = ref<IWorkspace[]>([])
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
      option.value = ele.id
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
      // 工作空间变了之后项目一定会变
      instance?.proxy?.$Bus.emit('projectChange', 'abc')
    })
  }
}
onMounted(() => {
  initMenuData()
  if (!currentWorkspaceName.value) {
    window.$notification?.warning({
      title: '',
      description: '还未选择工作空间',
      duration: 2500,
    })
  }
})
watch(
  () => currentWorkspaceName.value,
  (newValue) => {
    appStore.setWorkspaceName(newValue)
  }
)
</script>
<template>
  <n-popselect
    v-if="currentWorkspaceName"
    v-model:value="currentWorkspaceId"
    :options="options"
    trigger="click"
    @update:value="handleSelect"
  >
    <span class="dropdown-link">{{ currentWorkspaceName }}</span>
  </n-popselect>
  <!-- 用户未选择工作空间的情况下使用红色帮助图标，以突出显示 -->
  <n-dropdown v-else trigger="hover" :options="options" @select="handleSelect">
    <icon-component type="help" size="25" color="red" />
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
