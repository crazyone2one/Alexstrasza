<script setup lang="ts">
import { ref, watch, computed, h } from 'vue'
import { NSpace, NInput, NTree, TreeOption, NDropdown, DropdownOption } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import { allLabelCase } from '/@/utils/constants'
import { ITreeNode } from '/@/apis/modules/test-case-node'

interface IProps {
  treeNodes: ITreeNode[]
  localSuffix: string
  allLabel?: string
  type?: string
  defaultLabel?: string
  nameLimit?: number
  defaultExpandAll?: boolean
  showCaseNum?: boolean
}
const { t } = useI18n()
const props = withDefaults(defineProps<IProps>(), {
  allLabel: allLabelCase,
  type: 'view',
  defaultLabel: '未规划用例',
  nameLimit: 50,
  defaultExpandAll: false,
  showCaseNum: true,
})
const pattern = ref('')
const emit = defineEmits(['update:treeNodes'])
const _treeNodes = ref<TreeOption[]>([])
// 默认展开项
const expandedKeys = ref<string[]>([])
// * 右键菜单选择项
const showDropdown = ref(false)
const x = ref(0)
const y = ref(0)
const optionsRef = ref<DropdownOption[]>([])
const options = optionsRef

const handleSelect = (param: any) => {
  console.log(param)
  showDropdown.value = false
}
const handleClickoutside = () => {
  showDropdown.value = false
}
const buildOptions = (option: TreeOption): DropdownOption[] => {
  const result = []
  // 是否显示添加节点按钮
  if (!isDefault(option)) {
    const _option: DropdownOption = { label: t('test_track.module.add_submodule') }
    result.push(_option)
  }
  // // 是否显示重命名节点按钮
  if (
    (option.key !== 'root' && option.label !== props.defaultLabel) ||
    (option.label === props.defaultLabel && option.level !== 1)
  ) {
    const _option: DropdownOption = { label: t('test_track.module.rename') }
    result.push(_option)
  }
  // // 是否显示删除节点按钮
  if (
    (option.key !== 'root' && option.label !== props.defaultLabel) ||
    (option.label === props.defaultLabel && option.level !== 1)
  ) {
    const _option: DropdownOption = { label: t('test_track.module.rename') }
    result.push(_option)
  }
  return result
}
// * 右键菜单选择项结束

const data = computed({
  get: () => props.treeNodes,
  set: (val) => {
    emit('update:treeNodes', val)
  },
})
const mapping = (items: ITreeNode[]): TreeOption[] =>
  items.map((item) => ({
    ...item,
    key: item.id,
    caseNum: item.caseNum,
    label: item.label,
    level: item.level,
    children: item.children && mapping(item.children),
  }))
// 添加节点
// const appendNode = (param: TreeOption) => {
//   console.log(param)
// }
// const editNode = (param: TreeOption) => {
//   console.log(param)
// }

const isDefault = (param: TreeOption) => {
  return param.label === props.defaultLabel && param.level === 1
}
// * 渲染后缀
const renderSuffix = ({ option }: { option: TreeOption }) => {
  return h(
    NSpace,
    { justify: 'center' },
    {
      default: () => {
        const result = []
        result.push(h('span', {}, { default: () => option.caseNum }))
        return result
      },
    }
  )
}
// 为节点绑定事件
const nodeProps = ({ option }: { option: TreeOption }) => {
  return {
    // 点击节点事件
    onClick() {
      window.$message?.info('[Click] ' + option.key)
    },
    onContextmenu(e: MouseEvent): void {
      optionsRef.value = buildOptions(option)
      showDropdown.value = true
      x.value = e.clientX
      y.value = e.clientY
      e.preventDefault()
    },
  }
}
// 初始化
const init = (treeNodes: ITreeNode[]) => {
  let num = 0
  treeNodes.forEach((t) => {
    num += t.caseNum
  })
  _treeNodes.value = []
  _treeNodes.value.unshift({
    key: 'root',
    label: props.allLabel,
    level: 0,
    children: mapping(data.value),
    caseNum: num > 0 ? num : '',
  })
  if (expandedKeys.value.length === 0) {
    expandedKeys.value.push('root')
  }
}
watch(
  () => data.value,
  (newValue) => {
    init(newValue)
  }
)
</script>
<template>
  <div>
    <n-space vertical :size="12">
      <n-input v-model:value="pattern" size="small" placeholder="搜索" />
      <n-tree
        :pattern="pattern"
        :data="_treeNodes"
        block-line
        :render-suffix="renderSuffix"
        :selectable="false"
        :node-props="nodeProps"
      />
      <n-dropdown
        trigger="manual"
        placement="bottom-start"
        :show="showDropdown"
        :options="options"
        :x="x"
        :y="y"
        size="small"
        @select="handleSelect"
        @clickoutside="handleClickoutside"
      />
    </n-space>
  </div>
</template>

<style scoped></style>
