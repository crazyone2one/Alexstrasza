<script setup lang="ts">
import { reactive } from 'vue'
import { useConfigStore } from '/@/store/modules/config'
import { ElPopover, ElAvatar, ElButton } from 'element-plus'

const config = useConfigStore()
const state = reactive({
  isFullScreen: false,
  currentNavMenu: '',
  showLayoutDrawer: false,
})
const onCurrentNavMenu = (status: boolean, name: string) => {
  state.currentNavMenu = status ? name : ''
}
const handleExit = () => {}
</script>
<template>
  <div class="nav-menus Default">
    <el-popover
      placement="bottom-end"
      :hide-after="0"
      :width="260"
      trigger="click"
      popper-class="admin-info-box"
      @show="onCurrentNavMenu(true, 'adminInfo')"
      @hide="onCurrentNavMenu(false, 'adminInfo')"
    >
      <template #reference>
        <div class="admin-info" :class="state.currentNavMenu == 'adminInfo' ? 'hover' : ''">
          <el-avatar :size="25" fit="fill">
            <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" alt="" />
          </el-avatar>
          <div class="admin-name">administrator</div>
        </div>
      </template>
      <div>
        <div class="admin-info-base">
          <el-avatar :size="50" fit="fill">
            <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" alt="" />
          </el-avatar>
          <div class="admin-info-other">
            <div class="admin-info-name">administrator</div>
            <div class="admin-info-lasttime">2023-01-13</div>
          </div>
        </div>
        <div class="admin-info-footer">
          <el-button type="primary" plain>个人信息</el-button>
          <el-button type="danger" plain @click="handleExit">退出系统</el-button>
        </div>
      </div>
    </el-popover>
    <div class="nav-menu-item">
      <Icon :color="config.getColorVal('headerBarTabColor')" class="nav-menu-icon" name="fa fa-cogs" size="18" />
    </div>
  </div>
</template>

<style scoped lang="scss">
.nav-menus.Default {
  border-radius: var(--el-border-radius-base);
  box-shadow: var(--el-box-shadow-light);
}
.nav-menus {
  display: flex;
  align-items: center;
  height: 100%;
  margin-left: auto;
  background-color: v-bind('config.getColorVal("headerBarBackground")');
  overflow: hidden;
  .nav-menu-item {
    height: 100%;
    width: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    .nav-menu-icon {
      box-sizing: content-box;
      color: v-bind('config.getColorVal("headerBarTabColor")');
    }
    &:hover {
      .icon {
        animation: twinkle 0.3s ease-in-out;
      }
    }
  }
  .admin-info {
    display: flex;
    height: 100%;
    padding: 0 10px;
    align-items: center;
    cursor: pointer;
    user-select: none;
    color: v-bind('config.getColorVal("headerBarTabColor")');
  }
  .admin-name {
    padding-left: 6px;
    white-space: nowrap;
  }
  .nav-menu-item:hover,
  .admin-info:hover,
  .nav-menu-item.hover,
  .admin-info.hover {
    background: v-bind('config.getColorVal("headerBarHoverBackground")');
  }
}
.dropdown-menu-box :deep(.el-dropdown-menu__item) {
  justify-content: center;
}
.admin-info-base {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  padding-top: 10px;
  .admin-info-other {
    display: block;
    width: 100%;
    text-align: center;
    padding: 10px 0;
    .admin-info-name {
      font-size: var(--el-font-size-large);
    }
  }
}
.admin-info-footer {
  padding: 10px 0;
  margin: 0 -12px -12px -12px;
  display: flex;
  justify-content: space-around;
}
.pt2 {
  padding-top: 2px;
}

@keyframes twinkle {
  0% {
    transform: scale(0);
  }
  80% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>
