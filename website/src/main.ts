import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import { createPinia } from 'pinia'
import { createRouter, createWebHistory } from "vue-router"
import routes from './routes.ts'
const app = createApp(App)
const pinia = createPinia()
import 'normalize.css'

const router = createRouter({
  history: createWebHistory(),
  routes,
})

app.use(pinia);
app.use(router);
app.mount('#app')
