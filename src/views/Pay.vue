<template>
  <MobilePage title="在线支付">
    <div v-if="loading" class="hint panel">加载中...</div>
    <div v-if="error" class="error panel">{{ error }}</div>

    <div v-if="order" class="panel white">
      <div class="pay-row">
        <div>订单信息：</div>
      </div>
      <div class="pay-row big">
        <div>{{ order.merchantName }}</div>
        <div class="money">￥{{ format0(order.payAmount) }}</div>
      </div>
      <div v-for="it in order.items" :key="it.id" class="pay-row">
        <div>{{ it.foodName }} x {{ it.quantity }}</div>
        <div>￥{{ format0(it.amount ?? (Number(it.foodPrice || 0) * Number(it.quantity || 0))) }}</div>
      </div>
      <div class="pay-row">
        <div>配送费</div>
        <div>￥{{ format0(order.deliveryFee) }}</div>
      </div>
    </div>

    <div class="panel white">
      <button class="channel" :class="{ active: payChannel === 'ALIPAY' }" type="button" @click="payChannel = 'ALIPAY'">
        <div class="ch-left">
          <div class="ch-icon alipay">支</div>
          <div>支付宝</div>
        </div>
        <div class="ch-check">{{ payChannel === 'ALIPAY' ? '✔' : '' }}</div>
      </button>
      <button class="channel" :class="{ active: payChannel === 'WECHAT' }" type="button" @click="payChannel = 'WECHAT'">
        <div class="ch-left">
          <div class="ch-icon wechat">微</div>
          <div>微信支付</div>
        </div>
        <div class="ch-check">{{ payChannel === 'WECHAT' ? '✔' : '' }}</div>
      </button>
      <button class="btn-confirm" type="button" :disabled="submitting || paid" @click="confirmPay">
        {{ paid ? '已完成支付' : submitting ? '确认中...' : '确认支付' }}
      </button>
    </div>
  </MobilePage>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import { orderApi, paymentApi } from '../api/services'

const route = useRoute()
const router = useRouter()
const orderId = computed(() => route.params.orderId)

const order = ref(null)
const payment = ref(null)
const payChannel = ref('ALIPAY')
const loading = ref(true)
const error = ref('')
const submitting = ref(false)

const format0 = (value) => Number(value || 0).toFixed(0)
const paid = computed(() => order.value?.status === 'PAID' || payment.value?.status === 'SUCCESS')

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    order.value = await orderApi.getOrder(orderId.value)
    payment.value = await paymentApi.getByOrderId(orderId.value)
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    loading.value = false
  }
}

const confirmPay = async () => {
  if (!order.value) return
  submitting.value = true
  error.value = ''
  try {
    if (!payment.value) {
      payment.value = await paymentApi.create({
        orderId: order.value.id,
        orderNo: order.value.orderNo,
        amount: order.value.payAmount,
        payChannel: payChannel.value === 'WECHAT' ? 'MOCK_WECHAT' : 'MOCK_ALIPAY',
      })
    }
    await paymentApi.mockCallback({ orderId: Number(orderId.value), success: true })
    await load()
    router.replace('/orders')
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

