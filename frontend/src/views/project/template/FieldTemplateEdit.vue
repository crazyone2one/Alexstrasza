<script setup lang="ts">
import { ref, computed, nextTick } from 'vue'
import ModalComponent from '/@/components/ModalComponent.vue'
import CustomFieldFormListComp from '../components/CustomFieldFormListComp.vue'
import { NDivider, NForm, NFormItem, NInput, FormInst, FormRules, NButton, NScrollbar } from 'naive-ui'
import { getCustomFieldDefault, getCustomFieldTemplates, ITestCaseTemplate } from '/@/apis/modules/template'
import { useAppStore } from '/@/store/modules/app'
import { ICustomField } from '/@/apis/modules/custom-field'
import { generateTableHeaderKey, getCustomFieldsKeys } from '/@/utils/table-utils'

interface IProps {
  visible?: boolean
  scene: string
  url: string
  rules: FormRules
  form?: ITestCaseTemplate
}
const props = withDefaults(defineProps<IProps>(), {
  visible: false,
  form: () => {
    return {} as ITestCaseTemplate
  },
})
const appStore = useAppStore()
const emits = defineEmits(['update:form'])
const model = computed({
  get: () => props.form,
  set: (val) => {
    emits('update:form', val)
  },
})
const formRef = ref<FormInst | null>(null)
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const customFieldFormListComp = ref<InstanceType<typeof CustomFieldFormListComp> | null>(null)
const relateFields = ref<ICustomField[]>([])
const templateContainIds = ref<string[]>([])
const isSystem = computed(() => {
  return props.form?.system
})
const init = (): void => {
  relateFields.value = []
  templateContainIds.value = []
}
//* 获取关联字段
const getRelateFields = () => {
  const condition = { templateId: '' }
  condition.templateId = model.value.id as string
  if (model.value.id) {
    getCustomFieldTemplates(condition).then((res) => {
      console.log(res)
    })
  } else {
    appendDefaultFiled()
  }
}
// * 默认关联字段
const appendDefaultFiled = () => {
  const condition = {
    projectId: appStore.getProjectId(),
    scene: props.scene,
  }
  getCustomFieldDefault(condition).then((res) => {
    const data = res.data
    data.forEach((item) => {
      if (item.id) {
        templateContainIds.value.push(item.id)
      }
      item.fieldId = item.id
      item.id = ''
      item.options = JSON.parse(item.options as string)
      if (item.type === 'checkbox') {
        item.defaultValue = []
      }
    })
    relateFields.value.push(...data)
  })
}
const openModal = (data?: ITestCaseTemplate): void => {
  modalDialog.value?.toggleModal()
  nextTick(() => {
    init()
    getRelateFields()
  })
}
const handleSubmit = (): void => {
  formRef.value?.validate(async (errors) => {
    if (!errors) {
      const param: ITestCaseTemplate = { steps: '', options: '', projectId: '' } as ITestCaseTemplate
      Object.assign(param, model.value)
      if (model.value.steps) {
        param.steps = JSON.stringify(model.value.steps)
      }
      param.options = JSON.stringify(model.value.options)
      param.projectId = appStore.getProjectId()
      const customFields = relateFields.value
      if (customFields) {
        const keys = getCustomFieldsKeys(customFields)
        customFields.forEach((item) => {
          if (!item.key) {
            item.key = generateTableHeaderKey(keys, customFields)
          }
          item.defaultValue = JSON.stringify(item.defaultValue)
        })
      }
      param.customFields = customFields
      console.log(param)
    }
  })
}
defineExpose({ openModal })
</script>
<template>
  <modal-component ref="modalDialog" title="title" sub-class="width:50%" @confirm="handleSubmit">
    <template #content>
      <n-scrollbar style="max-height: 700px">
        <div>
          <n-divider title-placement="left"> {{ $t('test_track.plan_view.base_info') }} </n-divider>
          <n-form
            ref="formRef"
            :model="props.form"
            :rules="props.rules"
            label-placement="left"
            label-width="auto"
            require-mark-placement="right-hanging"
          >
            <n-form-item :label="$t('commons.template_name')" path="name">
              <n-input v-model:value="model.name" :disabled="isSystem" />
            </n-form-item>
            <slot name="base"></slot>
            <n-form-item :label="$t('commons.description')" path="description">
              <n-input
                v-model:value="model.description"
                type="textarea"
                :autosize="{
                  minRows: 3,
                  maxRows: 5,
                }"
              />
            </n-form-item>
            <n-divider title-placement="left"> {{ $t('custom_field.template_setting') }} </n-divider>
            <slot name="setting"></slot>
            <n-form-item :label="$t('table.selected_fields')">
              <n-button type="primary" size="small"> {{ $t('custom_field.add_field') }} </n-button>
              <n-button type="primary" secondary size="small">{{ $t('custom_field.custom_field_setting') }} </n-button>
            </n-form-item>
            <n-form-item>
              <custom-field-form-list-comp
                ref="customFieldFormListComp"
                :table-data="relateFields"
                :scene="props.scene"
                :template-contain-ids="templateContainIds"
              />
            </n-form-item>
          </n-form>
        </div>
      </n-scrollbar>
    </template>
  </modal-component>
</template>

<style scoped></style>
