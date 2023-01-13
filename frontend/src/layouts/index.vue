<script setup lang="ts">
import { ElContainer, ElMain } from 'element-plus'
import SideComp from './components/SideComp.vue'
import HeaderComp from './components/HeaderComp.vue'
import MainComp from './components/MainComp.vue'
</script>
<template>
  <div class="common-layout">
    <el-container class="layout-container">
      <side-comp />
      <el-container class="content-wrapper">
        <header-comp />
        <main-comp />
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.layout-container {
  height: 100%;
  width: 100%;
}
.content-wrapper {
  flex-direction: column;
  width: 100%;
  height: 100%;
}
</style>
