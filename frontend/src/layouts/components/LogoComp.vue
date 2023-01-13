<script setup lang="ts">
import { useAppStore } from '../../store/modules/app'
import { useConfigStore } from '/@/store/modules/config'

const config = useConfigStore()
const appStore = useAppStore()
const onMenuCollapse = () => {
  config.setLayout('menuCollapse', !config.layout.menuCollapse)
}
</script>
<template>
  <div class="layout-logo">
    <img v-if="!config.layout.menuCollapse" class="logo-img" src="../../assets/title-logo.svg" alt="logo" />
    <div
      v-if="!config.layout.menuCollapse"
      :style="{ color: config.getColorVal('menuActiveColor') }"
      class="website-name"
    >
      {{ appStore.app.siteName }}
    </div>
    <Icon
      :name="config.layout.menuCollapse ? 'fa fa-indent' : 'fa fa-dedent'"
      :class="config.layout.menuCollapse ? 'unfold' : ''"
      :color="config.getColorVal('menuActiveColor')"
      size="18"
      class="fold"
      @click="onMenuCollapse"
    />
  </div>
</template>

<style scoped lang="scss">
.layout-logo {
  width: 100%;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  padding: 10px;
  background: #fcfcfc;
}
.logo-img {
  width: 28px;
}
.website-name {
  display: block;
  width: 180px;
  padding-left: 4px;
  font-size: var(--el-font-size-extra-large);
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.fold {
  margin-left: auto;
}
.unfold {
  margin: 0 auto;
}
</style>
