<script setup lang="ts">
import { NLayoutHeader, NSpace, NDropdown, NAvatar } from 'naive-ui'
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserInfoStore } from '../store/modules/user'
import avatar from '/@/assets/title-logo.svg'

const userStore = useUserInfoStore()
const router = useRouter()
const options = computed(() => [
  { key: 'divider', type: 'divider' },
  // { key: 'profile', label: () => h(RouterLink, { to: '/profile' }, 'Your Profiles') },
  // { key: 'settings', label: () => h(RouterLink, { to: '/profile/settings' }, 'Settings') },
  { key: 'divider', type: 'divider' },
  { key: 'logout', label: 'Sign out' },
])
const handleOptionsSelect = (key: string): void => {
  if (key === 'logout') {
    userStore.resetAuthStore()
    router.push({ name: 'login' })
  }
}
</script>
<template>
  <n-layout-header bordered>
    <n-space :size="20" align="center" style="line-height: 1" class="ml-auto">
      <n-dropdown placement="bottom-end" show-arrow :options="options" @select="handleOptionsSelect">
        <n-avatar round size="small" :src="avatar" />
      </n-dropdown>
    </n-space>
  </n-layout-header>
</template>

<style scoped>
.n-layout-header {
  position: sticky;
  top: 0;
  z-index: 1;
  display: flex;
  align-items: center;
  padding: 9px 18px;
}
</style>
