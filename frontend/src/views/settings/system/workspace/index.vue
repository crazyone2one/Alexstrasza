<script setup lang="ts">
import { DataTableColumns, NCard, NDataTable, NSpin, NSpace, NButton } from 'naive-ui'
import { ref, h, reactive, onMounted } from 'vue'
import SearchComponent from '/@/components/SearchComponent.vue'
import PaginationComponent from '/@/components/PaginationComponent.vue'
import ButtonComp from '/@/components/ButtonComponent.vue'
import { IPageResponse } from '/@/apis/interface'
import { getWorkspaceList, IWorkspace } from '/@/apis/modules/workspace'
import { useI18n } from 'vue-i18n'
import EditWorkspaceComp from './EditWorkspaceComp.vue'
import DeleteComponent from '/@/components/DeleteComponent.vue'

const show = ref(false)
const { t } = useI18n()
const editWorkspaceComp = ref<InstanceType<typeof EditWorkspaceComp> | null>(null)
const deleteComponent = ref<InstanceType<typeof DeleteComponent> | null>(null)
const state = reactive({
  condition: { name: '', limit: 10, page: 1 },
})
const tableData = reactive<IPageResponse<IWorkspace>>({
  records: [],
  total: 0,
})
const rowKey = (row: IWorkspace) => {
  return row.id as string
}
const cellClick = (workspace: IWorkspace) => {}
// * 添加或者编辑
const handleEdit = (workspace?: IWorkspace): void => {
  editWorkspaceComp.value?.openEditModal(workspace)
}
const handleDelete = (workspace: IWorkspace) => {
  deleteComponent.value?.openDeleteModal(workspace)
}

// * 创建列表表头
const createColumns = (): DataTableColumns<IWorkspace> => {
  return [
    {
      title: t('commons.name'),
      key: 'name',
      align: 'center',
    },
    {
      title: t('commons.description'),
      align: 'center',
      key: 'description',
      ellipsis: true,
    },
    {
      title: t('commons.member'),
      key: 'memberSize',
      render(row) {
        return h(
          NButton,
          {
            text: true,
            tag: 'a',
            onClick: () => cellClick(row),
          },
          { default: () => row.memberSize }
        )
      },
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
  getWorkspaceList(state.condition).then((resp) => {
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
const deleteWorkspace = (param: IWorkspace) => {
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
  <edit-workspace-comp ref="editWorkspaceComp" @refresh="loadTableData" />
  <delete-component ref="deleteComponent" :title="$t('workspace.delete')" @delete="deleteWorkspace" />
</template>

<style scoped></style>
