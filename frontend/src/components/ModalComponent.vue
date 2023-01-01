<script setup lang="ts">
import { ref } from 'vue'
import { NScrollbar, NModal, NSpace, NButton } from 'naive-ui'

withDefaults(
  defineProps<{
    title?: string
    subClass?: string
    autoFocus?: boolean
  }>(),
  {
    title: '操作',
    subClass: '',
    autoFocus: false,
  }
)
const showModal = ref(false)
const emits = defineEmits(['confirm', 'cancel'])

const toggleModal = () => {
  showModal.value = !showModal.value
  return Promise.resolve(showModal.value)
}
const show = () => {
  showModal.value = true
  return Promise.resolve(true)
}
const closeModal = () => {
  showModal.value = false
  return Promise.resolve(false)
}
const onConfirm = (): void => {
  emits('confirm')
}
const onCancel = (): void => {
  showModal.value = false
  emits('cancel')
}
defineExpose({ show, toggleModal, closeModal })
</script>
<template>
  <n-modal
    v-model:show="showModal"
    preset="dialog"
    :title="title"
    :show-icon="false"
    :mask-closable="false"
    :style="subClass"
    :auto-focus="autoFocus"
  >
    <n-scrollbar>
      <slot name="content"></slot>
    </n-scrollbar>
    <template #action>
      <n-space justify="end">
        <n-button type="default" size="small" @click="onCancel">
          {{ $t('commons.cancel') }}
        </n-button>
        <n-button type="primary" size="small" @click="onConfirm">
          {{ $t('commons.confirm') }}
        </n-button>
      </n-space>
    </template>
  </n-modal>
</template>

<style scoped></style>
