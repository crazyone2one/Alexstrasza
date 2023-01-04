<script setup lang="ts">
import { DataTableColumns, NCard, NDataTable, NSpin, NSpace, NButton } from 'naive-ui'
import { ref, h, reactive, onMounted, computed } from 'vue'
import SearchComponent from '/@/components/SearchComponent.vue'
import PaginationComponent from '/@/components/PaginationComponent.vue'
import ButtonComp from '/@/components/ButtonComponent.vue'
import { IPageResponse } from '/@/apis/interface'
import { useI18n } from 'vue-i18n'
import DeleteComponent from '/@/components/DeleteComponent.vue'
import { getProjectPages, IProject } from '/@/apis/modules/project'
import { useAppStore } from '/@/store/modules/app'
import EditProjectComp from './EditProjectComp.vue'

const show = ref(false)
const { t } = useI18n()
const appStore = useAppStore()
const editProjectComp = ref<InstanceType<typeof EditProjectComp> | null>(null)
const deleteComponent = ref<InstanceType<typeof DeleteComponent> | null>(null)
const state = reactive({
  condition: { name: '', limit: 10, page: 1, workspaceId: '' },
})
const tableData = reactive<IPageResponse<IProject>>({
  records: [],
  total: 0,
})
const projectId = computed(() => {
  return appStore.getProjectId()
})
const rowKey = (row: IProject) => {
  return row.id as string
}
const cellClick = (param: IProject) => {}
// * 添加或者编辑
const handleEdit = (param?: IProject): void => {
  editProjectComp.value?.openEditModal(param)
}
const handleDelete = (param: IProject) => {
  console.log('delete')
}

// * 创建列表表头
const createColumns = (): DataTableColumns<IProject> => {
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
      align: 'center',
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
      title: t('commons.create_user'),
      key: 'createUser',
      render(row) {
        return h('span', {}, { default: () => row.createUserName })
      },
    },
    {
      title: t('commons.create_time'),
      key: 'createTime',
      render(row) {
        return h('span', {}, { default: () => row.createTime })
      },
    },
    {
      title: t('commons.update_time'),
      key: 'updateTime',
      render(row) {
        return h('span', {}, { default: () => row.updateTime })
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
                  disabled: row.id === projectId.value,
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
  state.condition.workspaceId = appStore.getWorkspaceId()
  show.value = true
  getProjectPages(state.condition)
    .then((resp) => {
      const { records, total } = resp.data
      tableData.records = records
      tableData.total = total
      show.value = false
    })
    .finally(() => {
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
const deleteWorkspace = (param: IProject) => {
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
  <edit-project-comp ref="editProjectComp" @refresh="loadTableData" />
  <delete-component ref="deleteComponent" :title="$t('workspace.delete')" @delete="deleteWorkspace" />
</template>

<style scoped></style>
