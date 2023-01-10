<script setup lang="ts">
import { FormRules, NFormItem, NSelect, NInput } from 'naive-ui'
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import FieldTemplateEdit from '../FieldTemplateEdit.vue'
import { ITestCaseTemplate } from '/@/apis/modules/template'
import StepChangeItemComp from '../../components/StepChangeItemComp.vue'
import TestCaseStepItemComp from '../../components/TestCaseStepItemComp.vue'

const { t } = useI18n()
const fieldTemplateEdit = ref<InstanceType<typeof FieldTemplateEdit> | null>(null)
const url = ref('')
const showDialog = ref(false)
const rules: FormRules = {
  name: [
    { required: true, trigger: ['blur'], message: t('test_track.case.input_name') },
    { max: 64, message: t('test_track.length_less_than') + '64', trigger: 'blur' },
  ],
  type: [{ required: true, trigger: 'change' }],
}
const model = ref<ITestCaseTemplate>({
  name: '',
  description: '',
  type: 'functional',
  caseName: '',
  system: false,
  global: false,
  prerequisite: '',
  stepDescription: '',
  expectedResult: '',
  actualResult: '',
  stepModel: 'STEP',
  steps: [],
  projectId: '',
})
const caseTypeOption = computed(() => {
  return [{ value: 'functional', label: t('api_test.home_page.failed_case_list.table_value.case_type.functional') }]
})
const isSystem = computed(() => {
  return model.value.system
})
const openModal = (param?: ITestCaseTemplate, isCopy?: boolean) => {
  showDialog.value = true
  if (param) {
    Object.assign(model.value, param)
    model.value.steps = param.steps ? JSON.parse(param.steps as string) : []
    if (!(param.options instanceof Array)) {
      model.value.options = param.options ? JSON.parse(param.options) : []
    }
    url.value = isCopy ? 'field/template/case/add' : 'field/template/case/update'
  } else {
    model.value = {
      name: '',
      description: '',
      type: 'functional',
      caseName: '',
      system: false,
      global: false,
      prerequisite: '',
      stepDescription: '',
      expectedResult: '',
      actualResult: '',
      stepModel: 'STEP',
      steps: [],
      projectId: '',
    }
    url.value = 'field/template/case/add'
  }
  fieldTemplateEdit.value?.openModal(param)
}
defineExpose({ openModal })
</script>
<template>
  <field-template-edit
    ref="fieldTemplateEdit"
    :form="model"
    scene="TEST_CASE"
    :url="url"
    :rules="rules"
    :visible="showDialog"
  >
    <template #base>
      <n-form-item :label="$t('api_test.home_page.failed_case_list.table_coloum.case_type')" path="type">
        <n-select
          v-model:value="model.type"
          :options="caseTypeOption"
          :placeholder="$t('api_test.home_page.failed_case_list.table_coloum.case_type')"
          :disabled="isSystem"
          filterable
        />
      </n-form-item>
    </template>
    <template #setting>
      <n-form-item :label="$t('test_track.case.name')" path="caseName">
        <n-input v-model:value="model.caseName" />
      </n-form-item>
      <n-form-item :label="$t('test_track.case.prerequisite')" path="prerequisite">
        <n-input v-model:value="model.prerequisite" />
      </n-form-item>
      <step-change-item-comp :form="model" />
      <test-case-step-item-comp v-if="model.stepModel === 'STEP'" :form="model" />
    </template>
  </field-template-edit>
</template>

<style scoped></style>
