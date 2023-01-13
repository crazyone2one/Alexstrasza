import { RouteLocationNormalized, RouteRecordRaw } from 'vue-router'

export interface Layout {
  shrink: boolean
  mainAnimation: string
  menuWidth: number
  menuDefaultIcon: string
  menuCollapse: boolean
  menuUniqueOpened: boolean
  menuShowTopBar: boolean
  menuBackground: string[]
  menuColor: string[]
  menuActiveBackground: string[]
  menuActiveColor: string[]
  menuTopBarBackground: string[]
  headerBarTabColor: string[]
  headerBarBackground: string[]
  headerBarHoverBackground: string[]
  headerBarTabActiveBackground: string[]
  headerBarTabActiveColor: string[]
}
export interface NavTabs {
  activeIndex: number
  activeRoute: RouteLocationNormalized | null
  tabsView: RouteLocationNormalized[]
  tabFullScreen: boolean
  tabsViewRoutes: RouteRecordRaw[]
  authNode: Map<string, string[]>
}
