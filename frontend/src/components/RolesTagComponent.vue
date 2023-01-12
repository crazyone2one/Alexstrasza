<script setup lang="ts">
import { watchEffect, ref } from 'vue'
import { NTag } from 'naive-ui'
import { ISystemGroup } from '../apis/modules/user-group'
interface Props {
  type?: 'default' | 'success' | 'error' | 'primary' | 'info' | 'warning'
  bordered?: boolean
  roles: ISystemGroup[]
}
const props = withDefaults(defineProps<Props>(), {
  type: 'success',
  bordered: false,
})
const tags = ref<ISystemGroup[]>([])
watchEffect(() => {
  tags.value = props.roles
  console.log(tags.value)
})
</script>
<template>
  <div v-if="tags">
    <span v-for="(role, index) in tags" :key="index">
      <n-tag :type="type" :bordered="bordered">{{ role.name }}</n-tag>
    </span>
  </div>
</template>

<style scoped></style>
