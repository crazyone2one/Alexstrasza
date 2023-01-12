<script setup lang="ts">
import { reactive, ref } from 'vue'
import { NCard, NSpace } from 'naive-ui'
import TestCaseNodeTreeComp from '../components/TestCaseNodeTreeComp.vue'
import SearchComponent from '/@/components/SearchComponent.vue'
import { ITreeNode } from '/@/apis/modules/test-case-node'

const state = reactive({
  condition: { name: '', limit: 10, page: 1, workspaceId: '' },
})
const treeNodes = ref<ITreeNode[]>([])
const setTreeNodes = (param: ITreeNode[]) => {
  treeNodes.value = param
}
</script>
<template>
  <n-card>
    <n-space>
      <test-case-node-tree-comp @set-tree-nodes="setTreeNodes" />
      <n-card>
        <template #header>
          <search-component :search-form-value="state.condition"></search-component>
        </template>
        aaa
      </n-card>
    </n-space>
  </n-card>
</template>

<style scoped></style>
