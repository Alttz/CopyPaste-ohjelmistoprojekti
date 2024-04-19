import { createRouter, createWebHistory } from 'vue-router'
//import HomeView from '../views/HomeView.vue'
import view_events from '../views/view_events.vue'
import view_login from "../views/view_login.vue"
import view_logout from "../views/view_logout.vue"
import view_ticketcheck from "../views/view_ticketcheck.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login2',
      component: view_login
    },
    {
      path: '/login',
      name: 'login',
      component: view_login
    },
    {
      path: '/logout',
      name: 'logout',
      component: view_logout
    },
    {
      path: '/events',
      name: 'events',
      component: view_events
    },
    {
      path: '/ticketcheck',
      name: 'ticketcheck',
      component: view_ticketcheck
    },

    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    }
  ]
})

export default router
