<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { addWorkspaceSpecial, IWorkspace, updateWorkspaceSpecial } from '/@/apis/modules/workspace'
import ModalComponent from '/@/components/ModalComponent.vue'
import { useI18n } from 'vue-i18n'
import { NForm, NFormItem, NInput, FormInst } from 'naive-ui'

const { t } = useI18n()
const title = ref<string>(t('workspace.create'))
const formRef = ref<FormInst | null>(null)
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const model = ref<IWorkspace>({
  name: '',
  description: '',
})
const rules = {
  name: [{ required: true, trigger: ['blur'], message: t('workspace.input_name') }],
  description: {
    max: 50,
    trigger: ['blur'],
    message: t('commons.input_limit', [0, 50]),
  },
}
const emits = defineEmits(['refresh'])

// * 重置表单
const resetModel = (): void => {
  model.value.id = ''
  model.value.name = ''
  model.value.description = ''
  title.value = t('workspace.create')
}
// * 打开编辑框
const openEditModal = (params?: IWorkspace) => {
  modalDialog.value?.toggleModal()
  resetModel()
  nextTick(() => {
    if (params) {
      console.log(params)
      title.value = t('workspace.update')
      model.value.id = params.id
      model.value.name = params ? params.name : ''
      model.value.description = params ? params.description : ''
      formRef.value?.restoreValidation()
    }
  })
}
// * 提交数据
const handleSubmit = (): void => {
  formRef.value?.validate((errors) => {
    if (!errors) {
      if (model.value.id) {
        updateWorkspaceSpecial(model.value).then(() => {
          modalDialog.value?.closeModal()
          emits('refresh')
          window.$message?.success(t('commons.modify_success'))
        })
      } else {
        addWorkspaceSpecial(model.value).then(() => {
          modalDialog.value?.closeModal()
          emits('refresh')
          window.$message?.success(t('commons.save_success'))
        })
      }
    }
  })
}
defineExpose({ openEditModal })
</script>
<template>
  <modal-component ref="modalDialog" :title="title" @confirm="handleSubmit">
    <template #content>
      <n-form
        ref="formRef"
        :model="model"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-form-item :label="$t('commons.name')" path="name">
          <n-input v-model:value="model.name" maxlength="100" show-count />
        </n-form-item>
        <n-form-item :label="$t('commons.description')" path="description">
          <n-input v-model:value="model.description" type="textarea" />
        </n-form-item>
      </n-form>
    </template>
  </modal-component>
</template>

<style scoped></style>
