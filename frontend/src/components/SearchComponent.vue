<script setup lang="ts">
import { computed } from 'vue'
import { NSpace, NInput } from 'naive-ui'
import ButtonComp from '/@/components/ButtonComponent.vue'

interface Props {
  searchFormValue: {
    name: string
    limit: number
    page: number
    [key: string]: unknown
  }
  showAdd?: boolean
}
const props = withDefaults(defineProps<Props>(), {
  showAdd: true,
})
const emits = defineEmits(['handleSearch', 'update:props'])
const searchFormValue = computed({
  get: () => props.searchFormValue,
  set: (val) => {
    emits('update:props', val)
  },
})
</script>
<template>
  <div class="search-form">
    <n-space>
      <n-input v-model:value="searchFormValue.name" :placeholder="$t('commons.search_by_name')" size="small" />
      <slot name="other-search-item"></slot>
      <button-comp
        :use-popover="true"
        :text="$t('commons.adv_search.search')"
        icon="search"
        :is-text="true"
        @handle-click="emits('handleSearch')"
      />

      <slot name="other-action-buttons"></slot>
    </n-space>
  </div>
</template>

<style scoped></style>
