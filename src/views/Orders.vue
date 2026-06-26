<template>
  <MobilePage title="订单">
    <div v-if="loading" class="hint panel">加载中...</div>
    <div v-if="error" class="error panel">{{ error }}</div>
    <div v-if="!loading && !error && orders.length === 0" class="hint panel">暂无历史订单</div>

    <div class="order-list">
      <div v-for="o in orders" :key="o.id" class="panel white">
        <div class="order-head">
          <div class="order-merchant">{{ o.merchantName }}</div>
          <div class="order-status" :class="{ paid: o.status === 'PAID' }">{{ statusText(o.status) }}</div>
        </div>
        <div class="order-sub">订单号：{{ o.orderNo }}</div>
        <div v-for="it in o.items || []" :key="it.id" class="pay-row">
          <div>{{ it.foodName }} x {{ it.quantity }}</div>
          <div>￥{{ format0(it.amount ?? (Number(it.foodPrice || 0) * Number(it.quantity || 0))) }}</div>
        </div>
        <div class="pay-row">
          <div class="order-sub">{{ formatDate(o.createdAt) }}</div>
          <div class="money">￥{{ format0(o.payAmount) }}</div>
        </div>
        <button v-if="o.status === 'UNPAID'" class="btn-confirm small" type="button" @click="goPay(o.id)">去支付</button>
      </div>
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
import { orderApi } from '../api/services'
import { useAuth } from '../store/auth'

const router = useRouter()
const { userId } = useAuth()

const orders = ref([])
const loading = ref(true)
const error = ref('')

const format0 = (value) => Number(value || 0).toFixed(0)

const statusText = (s) =>
  ({
    UNPAID: '未支付',
    PAID: '已支付',
  }[s] || s)

const formatDate = (v) => {
  if (!v) return ''
  try {
    return new Date(v).toLocaleString('zh-CN', { hour12: false })
  } catch {
    return String(v)
  }
}

const goPay = (id) => router.push(`/pay/${id}`)

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    orders.value = (await orderApi.listUserOrders(userId.value)) || []
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

