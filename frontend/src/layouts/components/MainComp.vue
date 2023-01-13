<script setup lang="ts">
import { reactive, onMounted, watch, onBeforeMount, onUnmounted, nextTick, getCurrentInstance } from 'vue'
import { useRoute, RouteLocationNormalized } from 'vue-router'
import { ElMain, ElScrollbar } from 'element-plus'
import { useConfigStore } from '/@/store/modules/config'
import { useNavTabsStore } from '/@/store/modules/nav-tabs'
import { mainHeight as layoutMainScrollbarStyle } from '/@/utils/common'

const config = useConfigStore()
const route = useRoute()
const navTabs = useNavTabsStore()
let instance = getCurrentInstance()
const state: {
  componentKey: string
  keepAliveComponentNameList: string[]
} = reactive({
  componentKey: route.path,
  keepAliveComponentNameList: [],
})
const addKeepAliveComponentName = (keepAliveName: string | undefined) => {
  if (keepAliveName) {
    let exist = state.keepAliveComponentNameList.find((name: string) => {
      return name === keepAliveName
    })
    if (exist) return
    state.keepAliveComponentNameList.push(keepAliveName)
  }
}

onBeforeMount(() => {
  instance?.proxy?.$Bus.on('onTabViewRefresh', (menu) => {
    state.keepAliveComponentNameList = state.keepAliveComponentNameList.filter((name: string) => {
      const _menu = menu as RouteLocationNormalized
      _menu.meta.keepalive !== name
    })
    state.componentKey = ''
    nextTick(() => {
      const _menu = menu as RouteLocationNormalized
      state.componentKey = _menu.path
      addKeepAliveComponentName(_menu.meta.keepalive as string)
    })
  })
  instance?.proxy?.$Bus.on('onTabViewClose', (menu) => {
    state.keepAliveComponentNameList = state.keepAliveComponentNameList.filter((name: string) => {
      const _menu = menu as RouteLocationNormalized
      _menu.meta.keepalive !== name
    })
  })
})

onUnmounted(() => {
  instance?.proxy?.$Bus.off('onTabViewRefresh')
  instance?.proxy?.$Bus.off('onTabViewClose')
})

onMounted(() => {
  // 确保刷新页面时也能正确取得当前路由 keepalive 参数
  if (typeof navTabs.state.activeRoute?.meta.keepalive == 'string') {
    addKeepAliveComponentName(navTabs.state.activeRoute?.meta.keepalive)
  }
})

watch(
  () => route.path,
  () => {
    state.componentKey = route.path
    if (typeof navTabs.state.activeRoute?.meta.keepalive == 'string') {
      addKeepAliveComponentName(navTabs.state.activeRoute?.meta.keepalive)
    }
  }
)
</script>
<template>
  <el-main class="layout-main">
    <el-scrollbar class="layout-main-scrollbar" :style="layoutMainScrollbarStyle()" ref="mainScrollbarRef">
      <router-view v-slot="{ Component }">
        <transition :name="config.layout.mainAnimation" mode="out-in">
          <keep-alive :include="state.keepAliveComponentNameList">
            <component :is="Component" :key="state.componentKey" />
          </keep-alive>
        </transition>
      </router-view>
    </el-scrollbar>
  </el-main>
</template>

<style scoped>
.layout-container .layout-main {
  padding: 0 !important;
  overflow: hidden;
  width: 100%;
  height: 100%;
}
.layout-main-scrollbar {
  width: 100%;
  position: relative;
  overflow: hidden;
}
</style>
