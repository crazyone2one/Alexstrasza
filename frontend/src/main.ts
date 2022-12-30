import { createApp } from 'vue'
// import './style.css'
import App from './App.vue'
import './styles'
import router from './router'
import pinia from './store'
import { i18n } from '/@/i18n/index'

const app = createApp(App)
app.use(router).use(pinia).use(i18n)

const meta = document.createElement('meta')
meta.name = 'naive-ui-style'
document.head.appendChild(meta)

app.mount('#app')
