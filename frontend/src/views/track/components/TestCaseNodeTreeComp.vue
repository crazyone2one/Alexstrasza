<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import NodeTreeComp from './NodeTreeComp.vue'
import { useAppStore } from '/@/store/modules/app'
import { getTestCaseNodesByCaseFilter } from '/@/apis/modules/test-case'
import { ITreeNode } from '/@/apis/modules/test-case-node'

const appStore = useAppStore()
const projectId = computed(() => {
  return appStore.getProjectId()
})
const treeNodes = ref<ITreeNode[]>([])
const list = () => {
  if (projectId.value) {
    getTestCaseNodesByCaseFilter(projectId.value, {}).then((res) => {
      treeNodes.value = res.data
    })
  }
}
const emits = defineEmits(['setTreeNodes'])
onMounted(() => {
  list()
})
watch(
  () => treeNodes.value,
  (newValue, oldValue) => {
    emits('setTreeNodes', newValue)
  }
)
</script>
<template>
  <div>
    <slot name="header"></slot>
    <node-tree-comp :tree-nodes="treeNodes" local-suffix="test_case"></node-tree-comp>
  </div>
</template>

<style scoped></style>
