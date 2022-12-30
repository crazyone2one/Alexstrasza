<script setup lang="ts">
import { MenuOption, NLayoutSider, NMenu } from 'naive-ui'
import { computed, ref, h, watchEffect } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { useAppStore } from '../store/modules/app'
import IconComp from '../components/IconComponent.vue'
import { useI18n } from 'vue-i18n'

interface IMenu {
  id: string
  label: string
  icon?: string
  name?: string
  type?: string
  key?: string
  params?: { [key: string]: string }
  children?: IMenu[]
}
const { t } = useI18n()
const appStore = useAppStore()
const currentKey = ref<string>('')
const expandedKeys = ref<string[]>([])
const route = useRoute()
const collapsed = computed(() => {
  return appStore.app.sidebarCollapsed
})

const menus = ref([
  {
    id: '610159148224d543ae4e05d1',
    label: 'Dashboard',
    icon: 'dashboard',
    name: 'dashboard',
  },
  {
    id: '610159148224d543ae4e05d3',
    label: 'System Settings',
    icon: 'settings',
    // name: 'users',
    children: [
      {
        id: 'setting-1',
        type: 'group',
        label: t('commons.system'),
        key: 'setting-key',
        children: [
          {
            id: '610159148224d543ae4e05d4',
            label: t('commons.user'),
            name: 'users',
          },
          {
            id: '610159148224d543ae4e05d5',
            label: t('commons.workspace'),
            name: 'workspaces',
          },
          {
            id: '610159148224d543ae4e05d6',
            label: t('group.group_permission'),
            name: 'usergroup',
          },
        ],
      },
      {
        id: 'setting-2',
        type: 'group',
        label: t('commons.workspace'),
        key: 'setting-workspace',
        children: [
          {
            id: '610159148224d543ae4e05d7',
            label: t('commons.member'),
            name: 'member',
          },
          {
            id: '610159148224d543ae4e05d8',
            label: t('project.manager'),
            name: 'project',
          },
        ],
      },
    ],
  },
])
/**
 * 构建菜单数据
 * @param items 菜单参数
 */
const mapping = (items: IMenu[]): MenuOption[] =>
  items.map((item) => ({
    ...item,
    key: item.id,
    label:
      item.name != null ? () => h(RouterLink, { to: { name: item.name } }, { default: () => item.label }) : item.label,
    icon: item.icon != null ? () => h(IconComp, { type: item.icon }) : undefined,
    children: item.children && mapping(item.children),
  }))

const options = computed(() => (menus.value ? mapping(menus.value) : []))
const routeMatched = (menu: IMenu): boolean => {
  return (
    route.name === menu.name && (menu.params == null || JSON.stringify(route.params) === JSON.stringify(menu.params))
  )
}
const matchExpanded = (items: IMenu[]): boolean => {
  let matched = false
  for (const item of items) {
    if (item.children != null) {
      matchExpanded(item.children) && expandedKeys.value.push(item.id)
    }
    if (routeMatched(item)) {
      currentKey.value = item.id
      matched = true
    }
  }
  return matched
}
const toggle = (): void => {
  appStore.setCollapsed()
}
watchEffect(() => menus.value && matchExpanded(menus.value))
</script>
<template>
  <n-layout-sider
    :width="220"
    :native-scrollbar="false"
    :collapsed="collapsed"
    collapse-mode="width"
    show-trigger="arrow-circle"
    bordered
    @update:collapsed="toggle"
  >
    <n-menu
      :value="currentKey"
      :default-expanded-keys="expandedKeys"
      :options="options"
      :root-indent="18"
      @update:value="
        (k) => {
          currentKey = k
        }
      "
    />
  </n-layout-sider>
</template>

<style scoped></style>
