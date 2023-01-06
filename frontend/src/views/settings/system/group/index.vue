<script setup lang="ts">
import { DataTableColumns, NCard, NDataTable, NSpin, NSpace, NButton } from 'naive-ui'
import { ref, h, reactive, onMounted, computed } from 'vue'
import SearchComponent from '/@/components/SearchComponent.vue'
import PaginationComponent from '/@/components/PaginationComponent.vue'
import ButtonComp from '/@/components/ButtonComponent.vue'
import { IPageResponse } from '/@/apis/interface'
import { useI18n } from 'vue-i18n'
import DeleteComponent from '/@/components/DeleteComponent.vue'
import { getUserGroupListByPage, ISystemGroup } from '/@/apis/modules/user-group'
import EditUserGroupComp from './EditUserGroupComp.vue'
import { USER_GROUP_SCOPE } from '/@/utils/table-constants'

const show = ref(false)
const { t } = useI18n()
const editUserGroupComp = ref<InstanceType<typeof EditUserGroupComp> | null>(null)
const deleteComponent = ref<InstanceType<typeof DeleteComponent> | null>(null)
const state = reactive({
  condition: { name: '', limit: 10, page: 1 },
})
const tableData = reactive<IPageResponse<ISystemGroup>>({
  records: [],
  total: 0,
})
const rowKey = (row: ISystemGroup) => {
  return row.id as string
}
const cellClick = (param: ISystemGroup) => {}
// * 添加或者编辑
const handleEdit = (param?: ISystemGroup): void => {
  editUserGroupComp.value?.openEditModal(param, 'create', param ? t('group.edit') : t('group.create'))
}
const handleDelete = (param: ISystemGroup) => {
  if (param.system) {
    window.$message?.warning(`t{'group.admin_not_allow_delete'}`)
    return false
  }
  deleteComponent.value?.openDeleteModal(param)
}
const userGroupType = computed(() => {
  return USER_GROUP_SCOPE
})
// * 创建列表表头
const createColumns = (): DataTableColumns<ISystemGroup> => {
  return [
    {
      title: t('commons.name'),
      key: 'name',
      align: 'center',
    },
    {
      title: t('group.type'),
      align: 'center',
      key: 'type',
      render(rowData) {
        return h(
          'span',
          {},
          { default: () => (userGroupType.value[rowData.type] ? t(userGroupType.value[rowData.type]) : rowData.type) }
        )
      },
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
          { default: () => row.memberSize || 0 }
        )
      },
    },
    {
      title: t('group.scope'),
      align: 'center',
      key: 'scopeName',
      render(rowData) {
        return h(
          'span',
          {},
          {
            default: () => {
              if (rowData.scopeId === 'global') {
                return t('group.global')
              } else if (rowData.scopeId === 'system') {
                return t('group.system')
              }
              return rowData.scopeName
            },
          }
        )
      },
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
      title: t('group.operator'),
      align: 'center',
      key: 'creator',
    },
    {
      title: t('group.description'),
      align: 'center',
      key: 'description',
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
  // show.value = true
  getUserGroupListByPage(state.condition).then((resp) => {
    const { records, total } = resp.data
    tableData.records = records
    tableData.total = total
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
const deleteWorkspace = (param: ISystemGroup) => {
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
  <edit-user-group-comp ref="editUserGroupComp" />
  <delete-component ref="deleteComponent" :title="$t('workspace.delete')" @delete="deleteWorkspace" />
</template>

<style scoped></style>
