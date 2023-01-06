<script setup lang="ts">
import { NTag } from 'naive-ui'
import { ISystemGroup } from '../apis/modules/user-group'
interface Props {
  type?: 'default' | 'success' | 'error' | 'primary' | 'info' | 'warning'
  bordered?: boolean
  roles: ISystemGroup[]
}
withDefaults(defineProps<Props>(), {
  type: 'success',
  bordered: false,
})
</script>
<template>
  <div>
    <span v-for="(role, index) in roles" :key="index">
      <n-tag :type="type" :bordered="bordered">{{ role.name }}</n-tag>
    </span>
  </div>
</template>

<style scoped></style>
