<script setup lang="ts">
import { DataTableColumns, NCard, NDataTable, NSpin, NSpace, NButton } from 'naive-ui'
import { ref, h, reactive, onMounted } from 'vue'
import SearchComponent from '/@/components/SearchComponent.vue'
import PaginationComponent from '/@/components/PaginationComponent.vue'
import ButtonComp from '/@/components/ButtonComponent.vue'
import { IPageResponse } from '/@/apis/interface'
import { useI18n } from 'vue-i18n'
import { useAppStore } from '/@/store/modules/app'
import { IUserInfo, getWorkspaceMemberPages, getWorkspaceMemberGroup } from '/@/apis/modules/user'

const show = ref(false)
const { t } = useI18n()
const appStore = useAppStore()

const state = reactive({
  condition: { name: '', limit: 10, page: 1, workspaceId: '' },
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
  console.log('handleEdit')
}

// * 创建列表表头
const createColumns = (): DataTableColumns<IUserInfo> => {
  return [
    {
      title: t('commons.username'),
      key: 'name',
      align: 'center',
    },
    {
      title: t('commons.email'),
      align: 'center',
      key: 'email',
    },

    {
      title: t('commons.phone'),
      key: 'phone',
    },
    {
      title: t('commons.group'),
      key: 'groups',
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
  getWorkspaceMemberPages(state.condition)
    .then((resp) => {
      const { records, total } = resp.data
      records.forEach((u) => {
        getWorkspaceMemberGroup(appStore.getWorkspaceId(), u.id as string).then((resp) => {
          u.groups = resp.data
        })
      })
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
</template>

<style scoped></style>
