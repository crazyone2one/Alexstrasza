import { RouteRecordRaw } from 'vue-router'
const mainRoutes: RouteRecordRaw[] = [
  { path: '/', name: 'dashboard', component: () => import('/@/views/dashboard/index.vue') },
]
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: () => import('/@/views/login/index.vue'),
    meta: {
      title: 'Sign In',
    },
  },
  {
    name: 'layout',
    path: '/',
    component: () => import('/@/layouts/index.vue'),
    children: [...mainRoutes],
  },
]
export default routes
