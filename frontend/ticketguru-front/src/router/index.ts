import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import view_events from '../views/view_events.vue'
import view_login from "../views/view_login.vue"
import view_test from "../views/view_test.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: view_login
    },
    {
      path: '/events',
      name: 'events',
      component: view_events
    },
    {
      path: '/test',
      name: 'test',
      component: view_test
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
