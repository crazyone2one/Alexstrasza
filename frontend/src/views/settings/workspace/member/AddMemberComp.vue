<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { IUserInfo, getUserListByResourceUrl, addWorkspaceMember } from '/@/apis/modules/user'
import { FormInst, NForm, NFormItem, NSelect, SelectOption } from 'naive-ui'
import ModalComponent from '/@/components/ModalComponent.vue'
import { GROUP_PROJECT } from '/@/utils/constants'
import { useAppStore } from '/@/store/modules/app'
import { getUserGroupList } from '/@/apis/modules/user-group'

interface IProps {
  groupType?: string
  groupScopeId?: string
  projectId?: string
  userResourceUrl?: string
}
const props = withDefaults(defineProps<IProps>(), {
  groupType: '',
  groupScopeId: '',
  projectId: '',
  userResourceUrl: '/user/list/',
})
const { t } = useI18n()
const appStore = useAppStore()
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const model = ref<{
  userIds: string[]
  groupIds: string[]
  groups: SelectOption[]
  workspaceId: string
}>({
  userIds: [],
  groupIds: [],
  groups: [],
  workspaceId: '',
})
// const rules = {
//   userIds: { required: true, message: t('member.please_choose_member'), trigger: ['blur'] },
//   groupIds: { required: true, message: t('group.please_select_group'), trigger: ['blur'] },
// }
const emit = defineEmits(['refresh'])
const formRef = ref<FormInst | null>(null)
const userListOptions = ref<SelectOption[]>([])
// * 提交数据
const handleSubmit = (): void => {
  formRef.value?.validate((errors) => {
    if (!errors) {
      model.value.workspaceId = appStore.getWorkspaceId()
      addWorkspaceMember(model.value).then(() => {
        window.$message?.success(t('commons.save_success'))
        modalDialog.value?.closeModal()
        emit('refresh')
      })
    }
  })
}
const resetModel = (): void => {
  model.value.groupIds = []
  model.value.userIds = []
  model.value.groups = []
}
// * open modal
const handleOpenModal = (val?: IUserInfo): void => {
  modalDialog.value?.toggleModal()
  resetModel()
  // 初始化用户列表信息
  getUserListByResourceUrl(props.userResourceUrl).then((res) => {
    const userList = res.data
    userList.forEach((user) => {
      const option: SelectOption = {}
      option.value = user.id
      option.label = user.name
      userListOptions.value.push(option)
    })
  })
  const param = { type: props.groupType, resourceId: props.groupScopeId, projectId: '' }
  if (props.groupType === GROUP_PROJECT) {
    param.projectId = props.projectId || appStore.getProjectId()
  }
  getUserGroupList(param).then((res) => {
    const result = res.data
    result.forEach((item) => {
      const option: SelectOption = {}
      option.value = item.id
      option.label = item.name
      model.value.groups.push(option)
    })
  })
}

defineExpose({ handleOpenModal })
</script>
<template>
  <modal-component ref="modalDialog" :title="$t('member.create')" sub-class="width:45%" @confirm="handleSubmit">
    <template #content>
      <n-form
        ref="formRef"
        :model="model"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-form-item :label="$t('commons.member')" path="userIds">
          <n-select
            v-model:value="model.userIds"
            :options="userListOptions"
            multiple
            filterable
            :placeholder="$t('member.please_choose_member')"
          />
        </n-form-item>
        <n-form-item :label="$t('commons.group')" path="groupIds">
          <n-select
            v-model:value="model.groupIds"
            :options="model.groups"
            multiple
            filterable
            :placeholder="$t('group.please_select_group')"
          />
        </n-form-item>
      </n-form>
    </template>
  </modal-component>
</template>

<style scoped></style>
