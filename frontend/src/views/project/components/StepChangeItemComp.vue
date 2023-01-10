<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { NFormItem, NDropdown } from 'naive-ui'
import { ITestCaseTemplate } from '/@/apis/modules/template'
import { useI18n } from 'vue-i18n'

interface IProps {
  form: ITestCaseTemplate
  disable?: boolean
}
const { t } = useI18n()
const props = withDefaults(defineProps<IProps>(), {
  disable: false,
})
const emits = defineEmits(['update:form'])
const model = computed({
  get: () => props.form,
  set: (val) => {
    emits('update:form', val)
  },
})
const options = [
  {
    label: t('test_track.case.step_describe'),
    key: 'STEP',
    disabled: props.disable,
  },
  {
    label: t('test_track.case.text_describe'),
    key: 'TEXT',
    disabled: props.disable,
  },
]
const handleSelect = (key: string) => {
  if (!props.disable) {
    return
  }
  model.value.stepModel = key
  console.log(model.value)
}
onMounted(() => {
  if (!props.form.stepModel) {
    model.value.stepModel = 'STEP'
  }
})
</script>
<template>
  <n-form-item
    :label="model.stepModel === 'STEP' ? $t('test_track.case.step_describe') : $t('test_track.case.text_describe')"
  >
    <n-dropdown v-if="!disable" trigger="hover" :options="options" @select="handleSelect">
      <span class="el-dropdown-link">
        {{ $t('test_track.case.change_type') }}
      </span>
    </n-dropdown>
  </n-form-item>
</template>

<style scoped></style>
