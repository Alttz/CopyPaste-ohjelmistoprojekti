import { createRouter, createWebHistory } from 'vue-router'
//import HomeView from '../views/HomeView.vue'
import view_buy2 from '../views/view_buy2.vue'
import view_login from "../views/view_login.vue"
import view_logout from "../views/view_logout.vue"
import view_ticketcheck from "../views/view_ticketcheck.vue"
import view_event from '@/views/view_event.vue'
import view_eventManagement from '@/views/view_eventManagement.vue'
import view_createEvent from '@/views/view_createEvent.vue'
import view_editEvent from '@/views/view_editEvent.vue'
import view_ticketTypes from '@/views/view_ticketTypes.vue'
import view_landing from '@/views/view_landing.vue'
import view_pagenotfound from '@/views/view_pagenotfound.vue'
import view_salesReport from '@/views/view_salesReport.vue'
import view_doPurchase from '@/views/view_doPurchase.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login2',
      component: view_login
    },
    {
      path: '/landing',
      name: 'landing',
      component: view_landing
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
      path: '/buy',
      name: 'buy',
      component: view_buy2
    },

    {
      path: '/doPurchase',
      name: 'doPurchase',
      component: view_doPurchase
    },

    {
      path: '/event_management',
      name: 'event_management',
      component: view_eventManagement
    },
    {
      path: '/createevent',
      name: 'createevent',
      component: view_createEvent
    },

    {
      path: '/editevent/:id',
      name: 'editevent',
      component: view_editEvent,
      props: true
    },


    {
      path: '/ticketcheck',
      name: 'ticketcheck',
      component: view_ticketcheck
    },

    {
      path: '/ticket-types/:id',
      name: 'ticketTypes',
      component: view_ticketTypes,
      props: true
    },

    {
      path: '/sales_report/:id',
      name: 'sales_report',
      component: view_salesReport,
      props: true
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


/*
    {
      path: '/:pathMatch(.*)*',
      component: view_pagenotfound
    }
* */


