<template>
  <div></div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useMessage, useDialog, useLoadingBar, useNotification } from 'naive-ui'
export default defineComponent({
  setup() {
    window.$message = useMessage()
    window.$dialog = useDialog()
    window.$loadingBar = useLoadingBar()
    window.$notification = useNotification()
  },
})
</script>
