<script setup lang="ts">
import { ref, nextTick, getCurrentInstance } from 'vue'
import ModalComponent from '/@/components/ModalComponent.vue'
import { useI18n } from 'vue-i18n'
import { NForm, NFormItem, NInput, FormInst, NSelect } from 'naive-ui'
import { IProject, modifyProject, saveProject } from '/@/apis/modules/project'
import TemplateSelectComp from '/@/components/TemplateSelectComp.vue'
import { useAppStore } from '/@/store/modules/app'
import { useUserInfoStore } from '/@/store/modules/user'
import { getCurrentUser } from '/@/apis/modules/user'

const { t } = useI18n()
const instance = getCurrentInstance()
const appStore = useAppStore()
const userStore = useUserInfoStore()
const title = ref<string>(t('project.create'))
const formRef = ref<FormInst | null>(null)
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const caseTemplate = ref<InstanceType<typeof TemplateSelectComp> | null>(null)

const model = ref<IProject>({
  name: '',
  description: '',
  caseTemplateId: '',
  issueTemplateId: '',
})
const rules = {
  name: [
    { required: true, trigger: ['blur'], message: t('project.input_name') },
    { min: 2, max: 60, message: t('commons.input_limit', [2, 60]), trigger: 'blur' },
  ],
  description: {
    max: 250,
    trigger: ['blur'],
    message: t('commons.input_limit', [0, 250]),
  },
}
const platformOptions = ref([])
const emits = defineEmits(['refresh'])

// * 重置表单
const resetModel = (): void => {
  model.value.id = ''
  model.value.name = ''
  model.value.description = ''
  model.value.caseTemplateId = ''
  model.value.issueTemplateId = ''
  title.value = t('project.create')
}
// * 打开编辑框
const openEditModal = (params?: IProject) => {
  modalDialog.value?.toggleModal()
  resetModel()
  nextTick(() => {
    if (params) {
      title.value = t('project.edit')
      model.value = Object.assign({}, params)
      formRef.value?.restoreValidation()
    }
  })
}
// * 提交数据
const handleSubmit = (): void => {
  formRef.value?.validate((errors) => {
    if (!errors) {
      model.value.workspaceId = appStore.getWorkspaceId()
      model.value.createUser = userStore.getSessionUser().id
      if (model.value.id) {
        modifyProject(model.value).then(() => {
          window.$message?.success(t('commons.save_success'))
          modalDialog.value?.closeModal()
          emits('refresh')
        })
      } else {
        saveProject(model.value).then(() => {
          window.$message?.success(t('commons.save_success'))
          modalDialog.value?.closeModal()
          // 添加项目后重新加载一次用户信息
          getCurrentUser(userStore.getSessionUser().id).then((resp) => {
            // userStore.$patch((state) => {
            //   state.user.id = resp.data.id as string
            //   state.user.token = resp.data.token as string
            //   state.user.username = resp.data.name
            //   state.user.permissions = resp.data.authorities as []
            // })
            appStore.$patch((state) => {
              state.app.currentProjectId = resp.data.lastProjectId as string
              state.app.currentWorkspaceId = resp.data.lastWorkspaceId as string
            })
          })
          emits('refresh')
        })
      }
      instance?.proxy?.$Bus.emit('projectChange', 'abc')
    } else {
      return false
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
          <n-input v-model:value="model.name" />
        </n-form-item>
        <n-form-item
          v-if="platformOptions.length > 1"
          :label="$t('test_track.issue.third_party_integrated')"
          path="platform"
        >
          <n-select v-model:value="model.platform" :options="platformOptions" />
        </n-form-item>
        <n-form-item v-if="model.caseTemplateId" :label="$t('workspace.case_template_manage')" path="caseTemplateId">
          <template-select-comp
            ref="caseTemplate"
            prop="caseTemplateId"
            scene="API_CASE"
            :data="model"
            :project-id="(model.id as string)"
          />
        </n-form-item>
        <!-- <n-form-item :label="$t('workspace.issue_template_manage')" path="issueTemplateId">
          <template-select-comp
            ref="issueTemplate"
            prop="issueTemplateId"
            scene="ISSUE"
            :platform="model.platform"
            :data="model"
            :project-id="(model.id as string)"
            :disabled="thirdPartTemplateSupport && model.thirdPartTemplate"
          /> 
            <n-checkbox v-model:checked="model.thirdPartTemplate">
            {{ $t('test_track.issue.use_third_party', [model.platform]) }}
          </n-checkbox>
        </n-form-item> -->
        <n-form-item :label="$t('commons.description')" path="description">
          <n-input v-model:value="model.description" type="textarea" />
        </n-form-item>
      </n-form>
    </template>
  </modal-component>
</template>

<style scoped></style>
