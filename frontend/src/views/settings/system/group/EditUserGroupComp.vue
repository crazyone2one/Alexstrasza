<script setup lang="ts">
import { ref } from 'vue'
import ModalComponent from '/@/components/ModalComponent.vue'
import { useI18n } from 'vue-i18n'
import { NForm, NFormItem, NInput, FormInst, NSelect, NSwitch, SelectOption } from 'naive-ui'
import { createUserGroup, ISystemGroup } from '/@/apis/modules/user-group'
import { GROUP_SYSTEM } from '/@/utils/constants'
import { getWorkspaces } from '/@/apis/modules/workspace'

const { t } = useI18n()

const dialogTitle = ref()
const formRef = ref<FormInst | null>(null)
const dialogType = ref('')
const show = ref(true)
const isSystem = ref(false)
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const model = ref<ISystemGroup>({
  name: '',
  description: '',
  global: false,
  system: false,
  type: '',
})
const workspaces = ref<Array<SelectOption>>([])
const rules = {
  name: [
    { required: true, trigger: ['blur'], message: t('commons.input_name') },
    { min: 2, max: 50, message: t('commons.input_limit', [2, 50]), trigger: 'blur' },
  ],
  description: { min: 2, max: 90, message: t('commons.input_limit', [2, 90]), trigger: 'blur' },
  scopeId: [{ required: true, message: t('group.select_belong_organization'), trigger: 'blur' }],
  type: [{ required: true, message: t('group.select_type'), trigger: 'blur' }],
}
const typeOptions = [
  {
    label: t('group.system'),
    value: 'SYSTEM',
  },
  {
    label: t('group.workspace'),
    value: 'WORKSPACE',
  },
  {
    label: t('group.project'),
    value: 'PROJECT',
  },
]
const handleSubmit = (): void => {
  formRef.value?.validate((errors) => {
    if (!errors) {
      if (dialogType.value === 'create') {
        createUserGroup(model.value).then(() => {
          window.$message?.success(t('commons.save_success'))
        })
        console.log(model.value)
      }
      if (dialogType.value === 'edit') {
        console.log(2)
      }
    }
  })
}
// * 打开编辑框
const openEditModal = (params?: ISystemGroup, type?: string, title?: string) => {
  modalDialog.value?.toggleModal()
  isSystem.value = false
  show.value = true
  dialogType.value = type as string
  dialogTitle.value = title
  model.value = Object.assign({}, params)
  if (type !== 'create') {
    if (model.value.type !== GROUP_SYSTEM) {
      model.value.global = true
      show.value = false
    } else {
      model.value.global = model.value.scopeId === 'global'
      show.value = !model.value.global
    }
  }
  getWorkspace()
}
const getWorkspace = () => {
  getWorkspaces().then((resp) => {
    const { data } = resp
    data.forEach((element) => {
      const workspaceItem: SelectOption = {}
      workspaceItem.label = element.name
      workspaceItem.value = element.id
      workspaces.value?.push(workspaceItem)
    })
  })
}
const handleChangeGlobal = (globalValue: boolean) => {
  show.value = isSystem.value ? false : !globalValue
}
const handleUpdateValue = (value: string) => {
  if (value === GROUP_SYSTEM) {
    isSystem.value = true
    model.value.global = true
    handleChangeGlobal(true)
  } else {
    isSystem.value = false
  }
}
defineExpose({ openEditModal })
</script>

<template>
  <modal-component ref="modalDialog" :title="dialogTitle" @confirm="handleSubmit">
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
          <n-input v-model:value="model.name" />
        </n-form-item>
        <n-form-item :label="$t('group.type')" path="type">
          <n-select v-model:value="model.type" :options="typeOptions" @update:value="handleUpdateValue" />
        </n-form-item>
        <n-form-item :label="$t('group.description')" path="description">
          <n-input v-model:value="model.description" type="textarea" />
        </n-form-item>
        <n-form-item :label="$t('group.global_group')">
          <n-switch
            v-model:value="model.global"
            :disabled="dialogType === 'edit' || model.type === 'SYSTEM'"
            @update:value="handleChangeGlobal(model.global)"
          />
        </n-form-item>
        <n-form-item v-if="show" :label="$t('project.owning_workspace')" path="scopeId">
          <n-select
            v-model:value="model.scopeId"
            :options="workspaces"
            :placeholder="$t('project.please_choose_workspace')"
            :disabled="dialogType === 'edit'"
          />
        </n-form-item>
      </n-form>
    </template>
  </modal-component>
</template>

<style scoped></style>
