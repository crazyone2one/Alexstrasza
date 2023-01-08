<script setup lang="ts">
import { ref } from 'vue'
import ModalComponent from './ModalComponent.vue'
import { NSpace, NInput } from 'naive-ui'
import { useI18n } from 'vue-i18n'

interface Props {
  title: string
  withTip?: boolean
}

withDefaults(defineProps<Props>(), {
  withTip: false,
})
const { t } = useI18n()
const deleteValue = ref('')
const record = ref({ name: '' })
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const emits = defineEmits(['delete'])
// * 提交数据
const handleSubmit = () => {
  if (deleteValue.value.trim() !== 'DELETE-' + record.value.name.trim()) {
    window.$message?.warning(t('commons.incorrect_input'))
    return
  }
  emits('delete', record.value)
  modalDialog.value?.closeModal()
}
// * 打开编辑框
const openDeleteModal = (param: { name: string }): void => {
  modalDialog.value?.toggleModal()
  deleteValue.value = ''
  record.value = param
}
defineExpose({ openDeleteModal })
</script>
<template>
  <modal-component ref="modalDialog" :title="title" @confirm="handleSubmit">
    <template #content>
      <n-space vertical>
        <span>{{ $t('commons.delete_confirm') }}</span>
        <span class="font-bold italic"> DELETE-{{ record.name }}</span>
        <n-input v-model:value="deleteValue" type="text" :placeholder="$t('commons.input_content')" />
      </n-space>
    </template>
  </modal-component>
</template>

<style scoped></style>
