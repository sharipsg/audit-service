import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Employees from '../views/Employees.vue'
import AuditHistory from '../views/AuditHistory.vue'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/employees',
    name: 'Employees',
    component: Employees,
    beforeEnter: (to, from, next) => {
      const token = localStorage.getItem('jwt_token')
      if (!token) {
        next('/')
      } else {
        next()
      }
    }
  },
  {
    path: '/audit/:entity/:id',
    name: 'AuditHistory',
    component: AuditHistory,
    beforeEnter: (to, from, next) => {
      const token = localStorage.getItem('jwt_token')
      if (!token) {
        next('/')
      } else {
        next()
      }
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

