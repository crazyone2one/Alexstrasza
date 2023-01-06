<script setup lang="ts">
import { ref } from 'vue'
import ModalComponent from '/@/components/ModalComponent.vue'
import { useI18n } from 'vue-i18n'
import { NForm, NFormItem, NInput, FormInst, NSelect, SelectOption, NButton } from 'naive-ui'
import { GROUP_TYPE } from '/@/utils/constants'
import { IUserInfo } from '/@/apis/modules/user'
import { getAllUserGroupByType, ISystemGroup } from '/@/apis/modules/user-group'

const { t } = useI18n()

const formRef = ref<FormInst | null>(null)
const type = ref<string>('Add')
const dialogTitle = ref<string>('创建用户')
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const model = ref<IUserInfo>({
  name: '',
  email: '',
  phone: '',
  groups: [
    {
      type: '',
      showSearchGetMore: false,
    },
  ],
})
const rules = {
  name: [
    { required: true, trigger: ['blur'], message: t('commons.input_name') },
    { min: 2, max: 50, message: t('commons.input_limit', [2, 50]), trigger: 'blur' },
  ],
  description: { min: 2, max: 90, message: t('commons.input_limit', [2, 90]), trigger: 'blur' },
  scopeId: [{ required: true, message: t('group.select_belong_organization'), trigger: 'blur' }],
  type: [{ required: true, message: t('group.select_type'), trigger: 'blur' }],
}
const userGroup = ref<ISystemGroup[]>([])
const handleSubmit = (): void => {
  formRef.value?.validate((errors) => {
    if (!errors) {
      console.log(model.value)
    }
  })
}
// * 打开编辑框
const openEditModal = (params?: IUserInfo, typeParam?: string, title?: string) => {
  modalDialog.value?.toggleModal()
  type.value = typeParam || type.value
  dialogTitle.value = title || dialogTitle.value

  getAllUserGroup()
}
const getLabel = (index: number): string => {
  const a = index + 1
  return t('commons.group') + a
}
const getAllUserGroup = (): void => {
  getAllUserGroupByType({ type: GROUP_TYPE.SYSTEM }).then((resp) => {
    userGroup.value = resp.data
  })
}
const activeGroup = (group: any) => {
  const temp = userGroup.value.filter((ug) => {
    if (!group.selects) {
      return true
    }
    let sign = true
    for (const groupSelect of group.selects) {
      if (ug.id + '+' + ug.type === groupSelect) {
        sign = false
        break
      }
    }
    return sign
  })
  const options: SelectOption[] = []
  temp.forEach((item) => {
    const option: SelectOption = {}
    option.label = item.name
    option.value = item.id + '+' + item.type
    options.push(option)
  })
  return options
}
const groupType = (idType: string) => {
  if (!idType) {
    return
  }
  return idType.split('+')[1]
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
        <n-form-item :label="$t('commons.username')" path="name">
          <n-input v-model:value="model.name" :placeholder="$t('user.input_name')" />
        </n-form-item>
        <n-form-item :label="$t('commons.email')" path="email">
          <n-input v-model:value="model.email" :placeholder="$t('user.input_email')" />
        </n-form-item>
        <n-form-item :label="$t('commons.phone')" path="phone">
          <n-input v-model:value="model.phone" :placeholder="$t('user.input_phone')" />
        </n-form-item>
        <n-form-item v-if="type === 'Add'" :label="$t('commons.password')" path="password">
          <n-input v-model:value="model.password" :placeholder="$t('user.input_password')" />
        </n-form-item>
        <div v-for="(group, index) in model.groups" :key="index">
          <n-form-item :label="getLabel(index)" :path="'groups.' + index + '.type'">
            <n-select
              v-model:value="group.type"
              :options="activeGroup(group)"
              :placeholder="$t('user.select_group')"
              :disabled="model.groups[index].type !== null && model.groups[index].type !== ''"
            />
            <n-button v-if="model.groups.length > 1" class="ml-5">{{ $t('commons.delete') }}</n-button>
          </n-form-item>
          <div v-if="groupType(group.type) === GROUP_TYPE.WORKSPACE">
            <n-form-item :label="t('commons.project')" :path="'groups.' + index + '.ids'">
              <n-select
                v-model:value="group.ids"
                :options="group.projectOptions"
                :placeholder="$t('user.select_project')"
              />
            </n-form-item>
          </div>
        </div>
      </n-form>
    </template>
  </modal-component>
</template>

<style scoped></style>
