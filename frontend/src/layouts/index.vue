<script setup lang="ts">
import { NLayout } from 'naive-ui'
import ContentComp from './content-comp.vue'
import HeaderComp from './header-comp.vue'
import SiderComp from './sider-comp.vue'
</script>
<template>
  <n-layout position="absolute" has-sider>
    <sider-comp />
    <n-layout>
      <header-comp />
      <content-comp />
      <!-- <n-layout-footer>成府路</n-layout-footer> -->
    </n-layout>
  </n-layout>
</template>

<style scoped></style>
