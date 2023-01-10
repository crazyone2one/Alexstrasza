<script setup lang="ts">
import { computed, h, onMounted } from 'vue'
import { NFormItem, NDataTable, DataTableColumns, NSpace, NButton, NInput, NInputNumber } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import { ISteps, ITestCaseTemplate } from '/@/apis/modules/template'

interface IProps {
  form: ITestCaseTemplate
  readOnly?: boolean
}

const { t } = useI18n()
const props = withDefaults(defineProps<IProps>(), {
  readOnly: false,
})

// const defaultRows = ref(2)
const emits = defineEmits(['update:form'])
const model = computed({
  get: () => props.form,
  set: (val) => {
    emits('update:form', val)
  },
})
const rowKey = (row: ISteps) => {
  return row.num
}
//* add step
const handleAddStep = (index: number, data: ISteps) => {
  const step: ISteps = {
    num: 0,
    desc: '',
    result: '',
  }
  step.num = data.num + 1
  step.desc = ''
  step.result = ''
  const _steps = model.value.steps as ISteps[]
  _steps.forEach((step) => {
    if (step.num > data.num) {
      step.num++
    }
  })
  _steps.splice(index + 1, 0, step)
}
const handleCopyStep = (index: number, data: ISteps) => {
  const step: ISteps = {
    num: 0,
    desc: '',
    result: '',
  }
  step.num = data.num + 1
  step.desc = data.desc
  step.result = data.result
  const _steps = model.value.steps as ISteps[]
  _steps.forEach((step) => {
    if (step.num > data.num) {
      step.num++
    }
  })
  _steps.splice(index + 1, 0, step)
}
const handleDeleteStep = (index: number, data: ISteps) => {
  const _steps = model.value.steps as ISteps[]
  _steps.splice(index, 1)
  _steps.forEach((step) => {
    if (step.num > data.num) {
      step.num--
    }
  })
}
const createColumns = (): DataTableColumns<ISteps> => {
  return [
    {
      title: t('test_track.case.number'),
      key: 'num',
      width: 80,
      align: 'center',
      render(row, index) {
        return h(
          NInputNumber,
          {
            value: row.num,
            showButton: false,
            disabled: true,
            bordered: false,
          },
          {}
        )
      },
    },
    {
      title: t('test_track.case.step_desc'),
      key: 'desc',
      render(row, index) {
        return h(
          NInput,
          {
            value: row.desc,
            type: 'textarea',
            // autofocus: { minRows: 1, maxRows: 3 },
            onUpdateValue: (v: string) => {
              const _steps = model.value.steps as ISteps[]
              _steps[index].desc = v
            },
          },
          {}
        )
      },
    },
    {
      title: t('test_track.case.expected_results'),
      key: 'result',
      render(row, index) {
        return h(
          NInput,
          {
            value: row.result,
            type: 'textarea',
            // autofocus: { minRows: 1, maxRows: 3 },
            onUpdateValue: (v: string) => {
              const _steps = model.value.steps as ISteps[]
              _steps[index].result = v
            },
          },
          {}
        )
      },
    },
    {
      title: t('commons.operating'),
      align: 'center',
      key: 'actions',
      fixed: 'right',
      render(row, index) {
        return h(
          NSpace,
          { justify: 'center' },
          {
            default: () => [
              h(
                NButton,
                {
                  size: 'small',
                  type: 'warning',
                  text: true,
                  onClick: () => handleAddStep(index, row),
                },
                { default: () => t('commons.add') }
              ),
              h(
                NButton,
                {
                  size: 'small',
                  type: 'info',
                  text: true,
                  onClick: () => handleCopyStep(index, row),
                },
                {
                  default: () => {
                    return '复制'
                  },
                }
              ),
              h(
                NButton,
                {
                  size: 'small',
                  type: 'error',
                  text: true,
                  onClick: () => handleDeleteStep(index, row),
                },
                {
                  default: () => {
                    return '删除'
                  },
                }
              ),
            ],
          }
        )
      },
    },
  ]
}
const columns = createColumns()
onMounted(() => {
  if (model.value.steps.length === 0) {
    model.value.steps = [
      {
        num: 1,
        desc: '11',
        result: '111',
      },
    ]
  }
})
</script>
<template>
  <n-form-item path="steps">
    <n-data-table :bordered="false" :columns="columns" :data="(model.steps as ISteps[])" :row-key="rowKey" />
  </n-form-item>
</template>

<style scoped></style>
