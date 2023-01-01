<script setup lang="ts">
import { NButton, NPopover } from 'naive-ui'
import IconComp from './IconComponent.vue'

interface Props {
  usePopover?: boolean
  text?: string
  icon: string
  type?: 'default' | 'tertiary' | 'primary' | 'info' | 'success' | 'warning' | 'error'
  disable?: boolean
  iconSize?: string | number
  isText?: boolean
}
withDefaults(defineProps<Props>(), {
  usePopover: false,
  text: '',
  type: 'default',
  disable: false,
  iconSize: 25,
  isText: false,
})
const emit = defineEmits(['handleClick'])
</script>
<template>
  <n-popover v-if="usePopover" trigger="hover">
    <template #trigger>
      <n-button :type="type" :text="isText" :disabled="disable" @click="emit('handleClick')">
        <template #icon>
          <icon-comp :type="icon" :size="iconSize" :depth="2" />
        </template>
      </n-button>
    </template>
    {{ text }}
  </n-popover>
  <n-button v-else :type="type" :disabled="disable" :text="isText" @click="emit('handleClick')">
    <template #icon>
      <icon-comp :type="icon" :size="iconSize" :depth="2" />
    </template>
  </n-button>
</template>

<style scoped></style>
