<script setup lang="ts">
import { NPagination } from 'naive-ui'
interface Props {
  itemCount: number
  showSizePicker?: boolean
  disabled?: boolean
  size?: string
}
withDefaults(defineProps<Props>(), {
  showSizePicker: true,
  disabled: false,
  size: 'medium',
})
const emits = defineEmits(['handlePage', 'handlePageSize'])
const handlePage = (page: number) => {
  emits('handlePage', page)
}
const handlePageSize = (pageSize: number) => {
  emits('handlePageSize', pageSize)
}
</script>
<template>
  <div class="mt-3">
    <n-pagination
      :item-count="itemCount"
      :page-sizes="[10, 20, 30, 40]"
      :show-size-picker="showSizePicker"
      :disabled="disabled"
      @update:page="handlePage"
      @update:page-size="handlePageSize"
    />
  </div>
</template>
<style scoped></style>
