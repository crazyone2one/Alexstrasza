<script setup lang="ts">
import { computed, h } from 'vue'
import { NDataTable, DataTableColumns, NButton, NCheckbox } from 'naive-ui'
import { ICustomField } from '/@/apis/modules/custom-field'
import { SYSTEM_FIELD_NAME_MAP } from '/@/utils/table-constants'
import { useI18n } from 'vue-i18n'
import CustomFiledComp from './CustomFiledComp.vue'

interface IProps {
  tableData?: ICustomField[]
  scene: string
  platform?: string
  templateContainIds: string[]
}
const props = withDefaults(defineProps<IProps>(), {
  tableData: () => [],
  platform: '',
})
const emits = defineEmits(['update:tableData'])
const model = computed({
  get: () => props.tableData,
  set: (val) => {
    emits('update:tableData', val)
  },
})
const { t } = useI18n()
const systemNameMap = computed(() => {
  return SYSTEM_FIELD_NAME_MAP
})
const handleDelete = (param: ICustomField) => {
  console.log(param)
}
const createColumns = (): DataTableColumns<ICustomField> => {
  return [
    {
      title: t('commons.name'),
      key: 'name',
      align: 'center',
      render(row) {
        return h('span', [
          row.system
            ? h('span', {}, { default: () => t(systemNameMap.value[row.name]) })
            : h('span', {}, { default: () => row.name }),
        ])
      },
    },
    {
      title: t('commons.default'),
      key: 'type',
      align: 'center',
      render(row) {
        return h(CustomFiledComp, { templateData: row, prop: 'defaultValue', isEdit: true })
      },
    },
    {
      title: t('api_test.definition.document.table_coloum.is_required'),
      key: 'required',
      align: 'center',
      render(row) {
        return h(NCheckbox, {
          checked: row.required,
          onUpdateChecked: (v: boolean) => {
            row.required = v
          },
        })
      },
    },
    {
      title: t('custom_field.system_field'),
      key: 'system',
      align: 'center',
      render(row) {
        return h('span', [
          row.system
            ? h('span', {}, { default: () => t('commons.yes') })
            : h('span', {}, { default: () => t('commons.no') }),
        ])
      },
    },
    {
      title: t('commons.remark'),
      key: 'remark',
      align: 'center',
    },
    {
      title: t('commons.operating'),
      align: 'center',
      key: 'actions',
      fixed: 'right',
      render(row) {
        return h(
          NButton,
          {
            size: 'small',
            type: 'error',
            text: true,
            disabled: row.name === '用例等级',
            onClick: () => handleDelete(row),
          },
          { default: () => t('commons.delete') }
        )
      },
    },
  ]
}
const columns = createColumns()
const rowKey = (row: ICustomField) => {
  return row.id as string
}
</script>
<template>
  <n-data-table :bordered="false" :columns="columns" :data="model" :row-key="rowKey" />
</template>

<style scoped></style>
