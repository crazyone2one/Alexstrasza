<script setup lang="ts">
import { ref, computed, reactive, nextTick, onMounted } from 'vue'
import { ElScrollbar, ElMenu } from 'element-plus'
import { useConfigStore } from '/@/store/modules/config'
import { onBeforeRouteUpdate, RouteLocationNormalizedLoaded, useRoute } from 'vue-router'
import MenuTreeComp from './MenuTreeComp.vue'
import { useNavTabsStore } from '/@/store/modules/nav-tabs'

const verticalMenusRef = ref<InstanceType<typeof ElScrollbar>>()
const config = useConfigStore()
const route = useRoute()
const navTabs = useNavTabsStore()
const state = reactive({
  defaultActive: '',
})
// 激活当前路由的菜单
const currentRouteActive = (currentRoute: RouteLocationNormalizedLoaded) => {
  state.defaultActive = currentRoute.path
}
// 滚动条滚动到激活菜单所在位置
const verticalMenusScroll = () => {
  nextTick(() => {
    const activeMenu: HTMLElement | null = document.querySelector('.el-menu.layouts-menu-vertical li.is-active')
    if (!activeMenu) return false
    verticalMenusRef.value?.setScrollTop(activeMenu.offsetTop)
  })
}
const verticalMenusScrollbarHeight = computed(() => {
  let menuTopBarHeight = 0
  if (config.layout.menuShowTopBar) {
    menuTopBarHeight = 50
  }
  return 'calc(100vh - ' + (32 + menuTopBarHeight) + 'px)'
})

onBeforeRouteUpdate((to) => {
  currentRouteActive(to)
})

onMounted(() => {
  currentRouteActive(route)
  verticalMenusScroll()
})
</script>
<template>
  <el-scrollbar ref="verticalMenusRef" class="vertical-menus-scrollbar">
    <el-menu
      class="layouts-menu-vertical"
      :collapse-transition="false"
      :unique-opened="config.layout.menuUniqueOpened"
      :default-active="state.defaultActive"
      :collapse="config.layout.menuCollapse"
    >
      <menu-tree-comp :menus="navTabs.state.tabsViewRoutes" />
    </el-menu>
  </el-scrollbar>
</template>

<style scoped>
.vertical-menus-scrollbar {
  height: v-bind(verticalMenusScrollbarHeight);
  background-color: v-bind('config.getColorVal("menuBackground")');
}
.layouts-menu-vertical {
  border: 0;
  --el-menu-bg-color: v-bind('config.getColorVal("menuBackground")');
  --el-menu-text-color: v-bind('config.getColorVal("menuColor")');
  --el-menu-active-color: v-bind('config.getColorVal("menuActiveColor")');
}
</style>
