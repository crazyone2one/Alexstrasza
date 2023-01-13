<script setup lang="ts">
import { ElMenuItem, ElSubMenu } from 'element-plus'
import { RouteRecordRaw, useRouter } from 'vue-router'
import { useConfigStore } from '/@/store/modules/config'

interface IProps {
  menus: RouteRecordRaw[]
}
const props = withDefaults(defineProps<IProps>(), {
  menus: () => [],
})
const config = useConfigStore()
const router = useRouter()
const onClickMenu = (menu: RouteRecordRaw) => {
  router.push({ name: menu.name })
}
</script>
<template>
  <template v-for="menu in props.menus">
    <template v-if="menu.children && menu.children.length > 0">
      <el-sub-menu :key="menu.path" :index="menu.path">
        <template #title>
          <Icon
            :color="config.getColorVal('menuColor')"
            :name="menu.meta?.icon ? menu.meta?.icon : config.layout.menuDefaultIcon"
          />
          <span>{{ menu.meta?.title ? menu.meta?.title : $t('noTitle') }}</span>
        </template>
        <menu-tree-comp :menus="menu.children" />
      </el-sub-menu>
    </template>
    <template v-else>
      <el-menu-item :key="menu.path" :index="menu.path" @click="onClickMenu(menu)">
        <Icon
          :color="config.getColorVal('menuColor')"
          :name="menu.meta?.icon ? menu.meta?.icon : config.layout.menuDefaultIcon"
        />
        <span>{{ menu.meta?.title ? menu.meta?.title : $t('noTitle') }}</span>
      </el-menu-item>
    </template>
  </template>
</template>

<style scoped lang="scss">
.el-sub-menu .icon,
.el-menu-item .icon {
  vertical-align: middle;
  margin-right: 5px;
  width: 24px;
  text-align: center;
}
.is-active > .icon {
  color: var(--el-menu-active-color) !important;
}
.el-menu-item.is-active {
  background-color: v-bind('config.getColorVal("menuActiveBackground")');
}
</style>
