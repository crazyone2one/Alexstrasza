import { createApp } from 'vue'
// import './style.css'
import App from './App.vue'
import './styles'
import router from './router'
import pinia from './store'

const app = createApp(App)
app.use(router).use(pinia)

const meta = document.createElement('meta')
meta.name = 'naive-ui-style'
document.head.appendChild(meta)
app.mount('#app')
