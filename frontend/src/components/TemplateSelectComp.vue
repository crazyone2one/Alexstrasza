<script setup lang="ts">
import { NSelect } from 'naive-ui'
import { ref } from 'vue'

interface Props {
  prop: string
  scene: string
  platform?: string
  data: {}
  projectId: string
  disabled?: boolean
}
withDefaults(defineProps<Props>(), {
  platform: '',
})

const selectedValue = ref(null)
const options = [
  {
    label: 'Drive My Car',
    value: 'song1',
  },
  {
    label: 'Norwegian Wood',
    value: 'song2',
  },
  {
    label: "You Won't See",
    value: 'song3',
  },
  {
    label: 'Nowhere Man',
    value: 'song4',
  },
]
</script>
<template>
  <n-select v-model:value="selectedValue" filterable :options="options" />
</template>

<style lang="scss" scoped></style>
