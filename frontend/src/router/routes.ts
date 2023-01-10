import { RouteRecordRaw } from 'vue-router'
const mainRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'dashboard',
    component: () => import('/@/views/dashboard/index.vue'),
    meta: {
      title: 'Home',
      requiresAuth: true,
    },
  },
  {
    name: 'users',
    path: '/users/:page?',
    component: () => import('/@/views/settings/system/user/index.vue'),
    meta: {
      title: 'Users',
      requiresAuth: true,
    },
  },
  {
    name: 'workspaces',
    path: '/workspaces/:page?',
    component: () => import('/@/views/settings/system/workspace/index.vue'),
    meta: {
      title: 'Workspaces',
      requiresAuth: true,
    },
  },
  {
    name: 'usergroup',
    path: '/usergroup/:page?',
    component: () => import('/@/views/settings/system/group/index.vue'),
    meta: {
      title: 'usergroup',
      requiresAuth: true,
    },
  },
  {
    name: 'member',
    path: '/member/:page?',
    component: () => import('/@/views/settings/workspace/member/index.vue'),
    meta: {
      title: 'member',
      requiresAuth: true,
    },
  },
  {
    name: 'project',
    path: 'project/:page?',
    component: () => import('/@/views/settings/workspace/project/index.vue'),
    meta: {
      title: 'project',
      requiresAuth: true,
    },
  },
  {
    name: 'template',
    path: 'template',
    component: () => import('/@/views/project/template/index.vue'),
    meta: {
      title: 'template',
      requiresAuth: true,
    },
  },
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
  // 404
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('/@/views/error/NotFound.vue'),
    meta: {
      title: 'Oh no!',
    },
  },
]
export default routes
