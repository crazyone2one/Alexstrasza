<script setup lang="ts">
import { ref } from 'vue'
import { NTabs, NTabPane } from 'naive-ui'
import TestCaseTemplateList from './case/TestCaseTemplateList.vue'
import CustomFieldList from './custom/CustomFieldList.vue'

const testCaseTemplateList = ref<InstanceType<typeof TestCaseTemplateList> | null>(null)
const customFieldList = ref<InstanceType<typeof CustomFieldList> | null>(null)
</script>
<template>
  <n-tabs :bar-width="28" type="line" class="custom-tabs">
    <n-tab-pane name="field" :tab="$t('custom_field.name')">
      <custom-field-list ref="customFieldList" />
    </n-tab-pane>
    <n-tab-pane name="caseTemplate" :tab="$t('workspace.case_template_manage')">
      <test-case-template-list ref="testCaseTemplateList" />
    </n-tab-pane>
    <n-tab-pane name="issueTemplate" :tab="$t('workspace.issue_template_manage')"> 七里香 </n-tab-pane>
  </n-tabs>
</template>

<style scoped></style>
