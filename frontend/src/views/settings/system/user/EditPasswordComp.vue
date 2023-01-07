<script setup lang="ts">
import { ref } from 'vue'
import ModalComponent from '/@/components/ModalComponent.vue'
import { FormInst, NForm, NFormItem, NInput } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import CryptoJS from 'crypto-js'
import { specialModifyPassword } from '/@/apis/modules/user'

const { t } = useI18n()
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const model = ref({
  id: '',
  newPassword: '',
  confirmPassword: '',
})
const formRef = ref<FormInst | null>(null)
const rules = {
  newPassword: [
    { required: true, trigger: ['blur'], message: t('user.input_password') },
    {
      required: true,
      trigger: ['blur'],
      message: t('member.password_format_is_incorrect'),
      pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,30}$/,
    },
  ],
  confirmPassword: { min: 2, max: 90, message: t('commons.input_limit', [2, 90]), trigger: 'blur' },
}
const emits = defineEmits(['refresh'])
const handleSubmit = (): void => {
  formRef.value?.validate(async (errors) => {
    if (!errors) {
      const param = {
        id: model.value.id,
        newPassword: CryptoJS.MD5(model.value.newPassword as string).toString(),
        confirmPassword: CryptoJS.MD5(model.value.confirmPassword as string).toString(),
      }
      model.value.newPassword = CryptoJS.MD5(model.value.newPassword as string).toString()
      specialModifyPassword(param).then(() => {
        window.$message?.success(t('commons.modify_success'))
        // TODO: 若更新的是当前的用户，则退出重新登录
        emits('refresh')
        modalDialog.value?.closeModal()
      })
    }
  })
}
const openModal = (userId: string): void => {
  model.value.id = userId
  model.value.newPassword = ''
  model.value.confirmPassword = ''
  formRef.value?.restoreValidation()
  modalDialog.value?.toggleModal()
}
defineExpose({ openModal })
</script>
<template>
  <modal-component ref="modalDialog" :title="$t('member.edit_password')" @confirm="handleSubmit">
    <template #content>
      <n-form
        ref="formRef"
        :model="model"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-form-item :label="$t('member.new_password')" path="newPassword">
          <n-input v-model:value="model.newPassword" type="password" show-password-on="mousedown" />
        </n-form-item>
        <n-form-item :label="$t('member.repeat_password')" path="confirmPassword">
          <n-input v-model:value="model.confirmPassword" type="password" show-password-on="mousedown" />
        </n-form-item>
      </n-form>
    </template>
  </modal-component>
</template>

<style scoped></style>
