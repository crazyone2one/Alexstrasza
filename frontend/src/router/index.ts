import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserInfoStore } from '../store/modules/user'

//  Define some routes
import routes from './routes'

// Create the router instance and pass the `routes` option
const router = createRouter({
  // Provide the history implementation to use. We are using the hash history for simplicity here.
  history: createWebHashHistory(),
  routes, // short for `routes: routes`
})

router.beforeEach((to) => {
  const userStore = useUserInfoStore()
  // need authorize & token is invalid
  if (to.meta.requiresAuth === true && !userStore.getToken()) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }
})

router.afterEach((to) => {
  const items = [import.meta.env.VITE_APP_TITLE]
  to.meta.title != null && items.unshift(to.meta.title as string)
  document.title = items.join(' - ')
})

export default router
