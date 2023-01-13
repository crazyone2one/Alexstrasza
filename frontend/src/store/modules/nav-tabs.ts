import { reactive } from 'vue'
import { defineStore } from 'pinia'
import { NavTabs } from '/@/store/interface/index'
import { RouteLocationNormalized, RouteRecordRaw } from 'vue-router'
import { isEmpty } from 'lodash-es'
import { i18n } from '/@/i18n'

export const useNavTabsStore = defineStore(
  'navTabs',
  () => {
    const state: NavTabs = reactive({
      // 激活tab的index
      activeIndex: 0,
      // 激活的tab
      activeRoute: null,
      // tab列表
      tabsView: [],
      // 当前tab是否全屏
      tabFullScreen: false,
      // 从后台加载到的菜单路由列表
      tabsViewRoutes: [],
      // 按钮权限节点
      authNode: new Map(),
    })
    const addTab = (route: RouteLocationNormalized) => {
      if (!route.meta.addtab) return
      for (const key in state.tabsView) {
        if (state.tabsView[key].path === route.path) {
          state.tabsView[key].params = !isEmpty(route.params) ? route.params : state.tabsView[key].params
          state.tabsView[key].query = !isEmpty(route.query) ? route.query : state.tabsView[key].query
          return
        }
      }
      if (typeof route.meta.title == 'string') {
        route.meta.title =
          route.meta.title.indexOf('pagesTitle.') === -1 ? route.meta.title : i18n.global.t(route.meta.title)
      }
      state.tabsView.push(route)
    }

    const closeTab = (route: RouteLocationNormalized) => {
      state.tabsView.map((v, k) => {
        if (v.path == route.path) {
          state.tabsView.splice(k, 1)
          return
        }
      })
    }

    const closeTabs = (retainMenu: RouteLocationNormalized | false = false) => {
      if (retainMenu) {
        state.tabsView = [retainMenu]
      } else {
        state.tabsView = []
      }
    }
    const setActiveRoute = (route: RouteLocationNormalized) => {
      const currentRouteIndex: number = state.tabsView.findIndex((item: RouteLocationNormalized) => {
        return item.path === route.path
      })
      if (currentRouteIndex === -1) return
      state.activeRoute = route
      state.activeIndex = currentRouteIndex
    }
    const setTabsViewRoutes = (data: RouteRecordRaw[]): void => {
      state.tabsViewRoutes = encodeRoutesURI(data)
    }

    const setAuthNode = (key: string, data: string[]) => {
      state.authNode.set(key, data)
    }

    return { state, addTab, closeTab, closeTabs, setActiveRoute, setAuthNode, setTabsViewRoutes }
  },
  {
    persist: {
      key: 'storeTabViewConfig',
      paths: ['tabFullScreen'],
    },
  }
)
function encodeRoutesURI(data: RouteRecordRaw[]) {
  data.forEach((item) => {
    if (item.meta?.type == 'iframe') {
      item.path = '/admin/iframe/' + encodeURIComponent(item.path)
    }

    if (item.children && item.children.length) {
      item.children = encodeRoutesURI(item.children)
    }
  })
  return data
}
