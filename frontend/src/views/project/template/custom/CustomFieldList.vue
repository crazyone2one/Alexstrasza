<script setup lang="ts">
import { DataTableColumns, NCard, NDataTable, NSpin, NSpace, NButton } from 'naive-ui'
import { ref, h, reactive, onMounted, computed } from 'vue'
import SearchComponent from '/@/components/SearchComponent.vue'
import PaginationComponent from '/@/components/PaginationComponent.vue'
import ButtonComp from '/@/components/ButtonComponent.vue'
import { IPageResponse } from '/@/apis/interface'
import { useI18n } from 'vue-i18n'
import DeleteComponent from '/@/components/DeleteComponent.vue'
import { FIELD_TYPE_MAP, SCENE_MAP, SYSTEM_FIELD_NAME_MAP } from '/@/utils/table-constants'
import { getCustomFieldPages, ICustomField } from '/@/apis/modules/custom-field'
import { useAppStore } from '/@/store/modules/app'

const show = ref(false)
const { t } = useI18n()
const appStore = useAppStore()
const deleteComponent = ref<InstanceType<typeof DeleteComponent> | null>(null)
const state = reactive({
  condition: { name: '', projectId: '', limit: 10, page: 1 },
})
const tableData = reactive<IPageResponse<ICustomField>>({
  records: [],
  total: 0,
})
const rowKey = (row: ICustomField) => {
  return row.id as string
}

// * 添加或者编辑
const handleEdit = (workspace?: ICustomField): void => {}
const handleDelete = (workspace: ICustomField) => {
  deleteComponent.value?.openDeleteModal(workspace)
}
const fieldTypeMap = computed(() => {
  return FIELD_TYPE_MAP
})
const sceneMap = computed(() => {
  return SCENE_MAP
})
const systemNameMap = computed(() => {
  return SYSTEM_FIELD_NAME_MAP
})
// * 创建列表表头
const createColumns = (): DataTableColumns<ICustomField> => {
  return [
    {
      title: t('commons.name'),
      key: 'name',
      align: 'center',
      render(row) {
        return h('span', [
          row.system
            ? h(
                'span',
                {},
                {
                  default: () => t(systemNameMap.value[row.name]),
                }
              )
            : h('span', {}, { default: () => row.name }),
        ])
      },
    },
    {
      title: t('custom_field.scene'),
      align: 'center',
      key: 'scene',
      render(rowData) {
        return h('span', {}, { default: () => t(sceneMap.value[rowData.scene]) })
      },
    },
    {
      title: t('custom_field.attribute_type'),
      align: 'center',
      key: 'type',
      render(rowData) {
        return h('span', {}, { default: () => t(fieldTypeMap.value[rowData.type]) })
      },
    },
    {
      title: t('custom_field.system_field'),
      align: 'center',
      key: 'system',
      render(row) {
        return h('span', [
          row.system
            ? h(
                'span',
                {},
                {
                  default: () => t('commons.yes'),
                }
              )
            : h('span', {}, { default: () => t('commons.no') }),
        ])
      },
    },
    {
      title: t('commons.remark'),
      align: 'center',
      key: 'remark',
    },
    {
      title: t('commons.create_time'),
      align: 'center',
      key: 'createTime',
    },
    {
      title: t('commons.update_time'),
      align: 'center',
      key: 'updateTime',
    },
    {
      title: t('commons.operating'),
      align: 'center',
      key: 'actions',
      fixed: 'right',
      render(row) {
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
                  onClick: () => handleEdit(row),
                },
                { default: () => t('commons.edit') }
              ),
              h(
                NButton,
                {
                  size: 'small',
                  type: 'error',
                  text: true,
                  onClick: () => handleDelete(row),
                },
                { default: () => t('commons.delete') }
              ),
            ],
          }
        )
      },
    },
  ]
}
const columns = createColumns()
/**
 * 加载列表数据
 */
const loadTableData = () => {
  show.value = true
  state.condition.projectId = appStore.getProjectId()
  getCustomFieldPages(state.condition).then((resp) => {
    tableData.records = resp.data.records
    tableData.total = resp.data.total
    show.value = false
  })
}

const handlePage = (page: number) => {
  state.condition.page = page
  loadTableData()
}
const handlePageSize = (pageSize: number) => {
  state.condition.limit = pageSize
  state.condition.page = 1
  loadTableData()
}
// * 删除数据
const deleteWorkspace = (param: ICustomField) => {
  console.log(param)
}
onMounted(() => {
  loadTableData()
})
</script>
<template>
  <n-spin :show="show">
    <n-card :bordered="false">
      <template #header>
        <search-component :search-form-value="state.condition" @handle-search="loadTableData">
          <template #other-action-buttons>
            <button-comp icon="add" type="info" :is-text="true" @handle-click="handleEdit()" />
          </template>
        </search-component>
      </template>
      <n-data-table :bordered="false" :columns="columns" :data="tableData.records" :row-key="rowKey" />
      <pagination-component
        :item-count="tableData.total"
        @handle-page="handlePage"
        @handle-page-size="handlePageSize"
      />
    </n-card>
  </n-spin>

  <delete-component ref="deleteComponent" :title="$t('workspace.delete')" @delete="deleteWorkspace" />
</template>

<style scoped></style>
