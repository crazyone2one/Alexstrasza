<script setup lang="ts">
import { DataTableColumns, NCard, NDataTable, NSpin, NSpace, NButton } from 'naive-ui'
import { ref, h, reactive, onMounted } from 'vue'
import SearchComponent from '/@/components/SearchComponent.vue'
import PaginationComponent from '/@/components/PaginationComponent.vue'
import ButtonComp from '/@/components/ButtonComponent.vue'
import { IPageResponse } from '/@/apis/interface'
import { useI18n } from 'vue-i18n'
import DeleteComponent from '/@/components/DeleteComponent.vue'
import { IUserInfo, specialGetUserGroup, specialListUsers } from '/@/apis/modules/user'
import EditUserComp from './EditUserComp.vue'
import RolesTagComponent from '/@/components/RolesTagComponent.vue'

const show = ref(false)
const { t } = useI18n()

const deleteComponent = ref<InstanceType<typeof DeleteComponent> | null>(null)
const editUserComp = ref<InstanceType<typeof EditUserComp> | null>(null)
const state = reactive({
  condition: { name: '', limit: 10, page: 1 },
})
const tableData = reactive<IPageResponse<IUserInfo>>({
  records: [],
  total: 0,
})
const rowKey = (row: IUserInfo) => {
  return row.id as string
}

// * 添加或者编辑
const handleEdit = (param?: IUserInfo): void => {
  editUserComp.value?.openEditModal(param, param ? 'Edit' : 'Add', param ? t('user.modify') : t('user.create'))
}
const handleDelete = (param: IUserInfo) => {}

// * 创建列表表头
const createColumns = (): DataTableColumns<IUserInfo> => {
  return [
    {
      title: t('commons.name'),
      key: 'name',
      align: 'center',
    },
    {
      title: t('commons.group'),
      align: 'center',
      key: 'type',
      render(row) {
        // fixme: 渲染角色tag
        return h(
          RolesTagComponent,
          {
            roles: row.authorities ? row.authorities : [],
          },
          {}
        )
      },
    },
    {
      title: t('commons.email'),
      key: 'email',
    },
    {
      title: t('commons.status'),
      key: 'status',
    },
    {
      title: t('commons.create_time'),
      align: 'center',
      key: 'createTime',
    },
    {
      title: t('user.source'),
      align: 'center',
      key: 'source',
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
  specialListUsers(state.condition)
    .then((resp) => {
      const { records, total } = resp.data
      records.forEach((row) => {
        // 查询权限
        specialGetUserGroup(row.id as string).then((resp) => {
          row.authorities = resp.data.groups
        })
      })
      tableData.records = records
      tableData.total = total
      show.value = false
    })
    .finally(() => (show.value = false))
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
const deleteWorkspace = (param: IUserInfo) => {
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
  <edit-user-comp ref="editUserComp" @refresh="loadTableData" />
  <delete-component ref="deleteComponent" :title="$t('workspace.delete')" @delete="deleteWorkspace" />
</template>

<style scoped></style>
