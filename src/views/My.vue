<template>
  <MobilePage title="我的">
    <div class="panel white center">
      <div class="avatar">👤</div>
      <div class="my-id">用户ID：{{ userId }}</div>
      <div class="my-sub">默认地址：{{ defaultAddress || '暂无默认地址' }}</div>
      <div v-if="error" class="error">{{ error }}</div>
    </div>

    <div class="panel white">
      <button class="btn-secondary" type="button" @click="goAddresses">地址管理</button>
      <button class="btn-secondary" type="button" @click="goOrders">我的订单</button>
      <button class="btn-secondary danger" type="button" @click="doLogout">退出登录</button>
    </div>

    <template #footer>
      <TabBar />
    </template>
  </MobilePage>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import TabBar from '../components/TabBar.vue'
import { userApi } from '../api/services'
import { useAuth } from '../store/auth'

const router = useRouter()
const { userId, logout } = useAuth()

const defaultAddress = ref('')
const error = ref('')

const buildAddr = (a) => {
  const parts = [a.province, a.city, a.district, a.detail].filter(Boolean)
  return parts.join('')
}

const load = async () => {
  error.value = ''
  try {
    const a = await userApi.getDefaultAddress(userId.value)
    defaultAddress.value = a ? buildAddr(a) : ''
  } catch (e) {
    error.value = e.message || String(e)
  }
}

const goAddresses = () => router.push('/address-list')
const goOrders = () => router.push('/orders')
const doLogout = () => {
  logout()
  router.replace('/login')
}

onMounted(load)
</script>

