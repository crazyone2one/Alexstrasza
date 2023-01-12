<script setup lang="ts">
import { ref } from 'vue'
import { DrawerPlacement, NDrawer, NDrawerContent } from 'naive-ui'

const active = ref(false)
const placement = ref<DrawerPlacement>('right')

const handleOpen = (projectId: string) => {
  active.value = true
  console.log(projectId)
}
defineExpose({ handleOpen })
</script>

<template>
  <n-drawer v-model:show="active" :default-width="502" :placement="placement" resizable :mask-closable="false">
    <n-drawer-content title="" closable> 《斯通纳》是美国作家约翰·威廉姆斯在 1965 年出版的小说。 </n-drawer-content>
  </n-drawer>
</template>

<style scoped></style>
