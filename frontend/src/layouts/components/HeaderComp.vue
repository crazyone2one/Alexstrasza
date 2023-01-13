<script setup lang="ts">
import { ElHeader } from 'element-plus'
import { useConfigStore } from '/@/store/modules/config'
import HeaderMenu from './HeaderMenu.vue'

const config = useConfigStore()
</script>
<template>
  <el-header class="layout-header">
    <div class="nav-bar">
      <!-- <NavTabs />
      <NavMenus /> -->
      <header-menu />
    </div>
  </el-header>
</template>

<style scoped lang="scss">
.layout-header {
  height: auto;
  margin: 16px 0 16px 16px;
}
.nav-bar {
  display: flex;
  height: 50px;
  margin: 20px var(--ba-main-space) 0 var(--ba-main-space);
  :deep(.nav-tabs) {
    display: flex;
    height: 100%;
    position: relative;
    .ba-nav-tab {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 20px;
      cursor: pointer;
      z-index: 1;
      user-select: none;
      opacity: 0.7;
      color: v-bind('config.getColorVal("headerBarTabColor")');
      .close-icon {
        padding: 2px;
        margin: 2px 0 0 4px;
      }
      .close-icon:hover {
        background: var(--ba-color-primary-light);
        color: var(--el-border-color) !important;
        border-radius: 50%;
      }
      &.active {
        color: v-bind('config.getColorVal("headerBarTabActiveColor")');
      }
      &:hover {
        opacity: 1;
      }
    }
    .nav-tabs-active-box {
      position: absolute;
      height: 40px;
      border-radius: var(--el-border-radius-base);
      background-color: v-bind('config.getColorVal("headerBarTabActiveBackground")');
      box-shadow: var(--el-box-shadow-light);
      transition: all 0.2s;
      -webkit-transition: all 0.2s;
    }
  }
}
</style>
