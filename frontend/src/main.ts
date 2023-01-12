import { createApp } from 'vue'
// import './style.css'
import App from './App.vue'
import './styles'
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

const meta = document.createElement('meta')
meta.name = 'naive-ui-style'
document.head.appendChild(meta)

app.config.globalProperties.$Bus = Mit
app.mount('#app')
