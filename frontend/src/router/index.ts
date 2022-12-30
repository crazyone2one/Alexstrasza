import { createRouter, createWebHashHistory } from 'vue-router'
//  Define some routes
import routes from './routes'

// Create the router instance and pass the `routes` option
const router = createRouter({
  // Provide the history implementation to use. We are using the hash history for simplicity here.
  history: createWebHashHistory(),
  routes, // short for `routes: routes`
})

export default router
