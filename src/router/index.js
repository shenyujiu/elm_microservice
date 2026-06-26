import { createRouter, createWebHistory } from 'vue-router'
import { useAuth } from '../store/auth'

import Home from '../views/Home.vue'
import BusinessList from '../views/BusinessList.vue'
import BusinessDetail from '../views/BusinessDetail.vue'
import ConfirmOrder from '../views/ConfirmOrder.vue'
import Pay from '../views/Pay.vue'
import Orders from '../views/Orders.vue'
import My from '../views/My.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import AddressList from '../views/AddressList.vue'
import AddressForm from '../views/AddressForm.vue'
import Fallback from '../views/Fallback.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: Home, meta: { title: '首页', tab: true } },
    { path: '/discover', name: 'discover', component: BusinessList, meta: { title: '商家列表', tab: true } },
    { path: '/orders', name: 'orders', component: Orders, meta: { title: '订单', tab: true, auth: true } },
    { path: '/my', name: 'my', component: My, meta: { title: '我的', tab: true, auth: true } },

    { path: '/business/:merchantId', name: 'business-detail', component: BusinessDetail, meta: { title: '商家信息' } },
    { path: '/confirm', name: 'confirm', component: ConfirmOrder, meta: { title: '确认订单', auth: true } },
    { path: '/pay/:orderId', name: 'pay', component: Pay, meta: { title: '在线支付', auth: true } },
    { path: '/address-list', name: 'address-list', component: AddressList, meta: { title: '地址管理', auth: true } },
    { path: '/address-add', name: 'address-add', component: AddressForm, meta: { title: '新增送货地址', auth: true } },
    { path: '/address-edit/:addressId', name: 'address-edit', component: AddressForm, meta: { title: '编辑送货地址', auth: true } },

    { path: '/login', name: 'login', component: Login, meta: { title: '用户登陆' } },
    { path: '/register', name: 'register', component: Register, meta: { title: '用户注册' } },
    { path: '/fallback', name: 'fallback', component: Fallback, meta: { title: '服务繁忙' } },
  ],
})

router.beforeEach((to) => {
  const { isLoggedIn } = useAuth()
  if (to.meta?.auth && !isLoggedIn.value) {
    const redirect = encodeURIComponent(to.fullPath)
    return { path: '/login', query: { redirect } }
  }
  return true
})

export default router

