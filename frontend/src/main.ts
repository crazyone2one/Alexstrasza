import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from './store'
import { i18n } from '/@/i18n/index'
import mitt from 'mitt'
const Mit = mitt()

declare module 'vue' {
  export interface ComponentCustomProperties {
    $Bus: typeof Mit
  }
}
const app = createApp(App)
app.use(router).use(pinia).use(i18n)

app.config.globalProperties.$Bus = Mit
app.mount('#app')
