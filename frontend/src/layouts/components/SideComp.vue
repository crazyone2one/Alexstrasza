<script setup lang="ts">
import { ElAside } from 'element-plus'
import { computed } from 'vue'
import { useConfigStore } from '../../store/modules/config'
import LogoComp from './LogoComp.vue'
import MenuComp from './MenuComp.vue'

const config = useConfigStore()
const menuWidth = computed(() => config.menuWidth())
</script>
<template>
  <el-aside class="layout-aside">
    <logo-comp />
    <menu-comp />
  </el-aside>
</template>

<style scoped lang="scss">
.layout-aside {
  background: var(bg-color-overlay);
  margin: 16px 0 16px 16px;
  height: calc(100vh - 32px);
  box-shadow: var(--el-box-shadow-light);
  border-radius: var(--el-border-radius-base);
  overflow: hidden;
  transition: width 0.3s ease;
  width: v-bind(menuWidth);
}
</style>
