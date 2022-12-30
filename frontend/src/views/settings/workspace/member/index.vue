<script setup lang="ts">
import { NResult, NButton } from 'naive-ui'

const handleClick = (): void => {
  window.$message?.info('Just So So')
}
</script>
<template>
  <n-result status="success" title="Member" description="Not yet!">
    <template #footer>
      <n-button @click="handleClick">not yet</n-button>
    </template>
  </n-result>
</template>

<style scoped></style>
