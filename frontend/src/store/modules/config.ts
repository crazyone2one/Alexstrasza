import { defineStore } from 'pinia'
import { reactive } from 'vue'
import { Layout } from '../interface'

export const useConfigStore = defineStore(
  'config',
  () => {
    const layout: Layout = reactive({
      // 是否收缩布局(小屏设备)
      shrink: false,
      // 后台主页面切换动画，可选值<slide-right|slide-left|el-fade-in-linear|el-fade-in|el-zoom-in-center|el-zoom-in-top|el-zoom-in-bottom>
      mainAnimation: 'slide-right',

      /* 侧边菜单 */
      // 侧边菜单背景色
      menuBackground: ['#ffffff', '#1d1e1f'],
      // 侧边菜单文字颜色
      menuColor: ['#303133', '#CFD3DC'],
      // 侧边菜单激活项背景色
      menuActiveBackground: ['#ffffff', '#1d1e1f'],
      // 侧边菜单激活项文字色
      menuActiveColor: ['#409eff', '#3375b9'],
      // 侧边菜单顶栏背景色
      menuTopBarBackground: ['#fcfcfc', '#1d1e1f'],
      // 侧边菜单宽度(展开时)，单位px
      menuWidth: 260,
      // 侧边菜单项默认图标
      menuDefaultIcon: 'el-icon-Minus',
      // 是否水平折叠收起菜单
      menuCollapse: false,
      // 是否只保持一个子菜单的展开(手风琴)
      menuUniqueOpened: false,
      // 显示菜单栏顶栏(LOGO)
      menuShowTopBar: true,

      /* 顶栏 */
      // 顶栏文字色
      headerBarTabColor: ['#000000', '#CFD3DC'],
      // 顶栏激活项背景色
      headerBarTabActiveBackground: ['#ffffff', '#1d1e1f'],
      // 顶栏激活项文字色
      headerBarTabActiveColor: ['#000000', '#409EFF'],
      // 顶栏背景色
      headerBarBackground: ['#ffffff', '#1d1e1f'],
      // 顶栏悬停时背景色
      headerBarHoverBackground: ['#f5f5f5', '#18222c'],
    })
    const menuWidth = () => {
      if (layout.shrink) {
        return layout.menuCollapse ? '0px' : layout.menuWidth + 'px'
      }
      // 菜单是否折叠
      return layout.menuCollapse ? '64px' : layout.menuWidth + 'px'
    }

    const getColorVal = (name: keyof Layout) => {
      const colors = layout[name] as string[]
      return colors[0]
    }
    const setLayout = (name: keyof Layout, value: any) => {
      layout[name] = value as never
    }
    return { layout, menuWidth, getColorVal, setLayout }
  },
  {
    persist: true,
  }
)
