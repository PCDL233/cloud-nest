import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import UserLoginPage from '@/pages/user/UserLoginPage.vue'
import UserRegisterPage from '@/pages/user/UserRegisterPage.vue'
import UserManagePage from '@/pages/admin/UserManagePage.vue'
import UserInfoPage from '@/pages/user/UserInfoPage.vue'
import AboutView from '@/views/AboutView.vue'
import NoAuthPage from '@/pages/user/NoAuthPage.vue'
import ACCESS_ENUM from '@/access/accessEnum.ts'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
      path: '/user/login',
      name: '用户登录',
      component: UserLoginPage,
    },
    {
      path: '/user/register',
      name: '用户注册',
      component: UserRegisterPage,
    },
    {
      path: '/admin/userManage',
      name: '用户管理',
      component: UserManagePage,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
    {
      path: '/user/info',
      component: UserInfoPage,
    },
    {
      path: '/about',
      name: 'about',
      component: AboutView,
    },
    {
      path: '/noAuth',
      name: 'noAuth',
      component: NoAuthPage,
    },
  ],
})

export default router
