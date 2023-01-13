import { App, CSSProperties } from 'vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import Icon from '/@/components/icon/index.vue'
import { ElForm } from 'element-plus'

/**
 * 是否是外部链接
 * @param path
 */
export const isExternal = (path: string): boolean => {
  return /^(https?|ftp|mailto|tel):/.test(path)
}

export const registerIcons = (app: App) => {
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(`el-icon-${key}`, component)
  }
  /*
   * 全局注册 Icon
   * 使用方式: <Icon name="name" size="size" color="color" />
   */
  app.component('Icon', Icon)
}
/**
 * 加载网络css文件
 * @param url css资源url
 */
export const loadCss = (url: string): void => {
  const link = document.createElement('link')
  link.rel = 'stylesheet'
  link.href = url
  link.crossOrigin = 'anonymous'
  document.getElementsByTagName('head')[0].appendChild(link)
}

/**
 * 加载网络js文件
 * @param url js资源url
 */
export const loadJs = (url: string): void => {
  const link = document.createElement('script')
  link.src = url
  document.body.appendChild(link)
}
/**
 * 表单重置
 * @param formEl
 */
export const onResetForm = (formEl: InstanceType<typeof ElForm> | undefined) => {
  if (!formEl) return
  formEl.resetFields && formEl.resetFields()
}

export const mainHeight = (extra = 0): CSSProperties => {
  let height = extra
  height += 60
  return {
    height: 'calc(100vh - ' + height.toString() + 'px)',
  }
}
