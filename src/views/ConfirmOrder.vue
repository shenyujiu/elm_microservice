<template>
  <MobilePage title="确认订单">
    <div v-if="loading" class="hint panel">加载中...</div>
    <div v-if="error" class="error panel">{{ error }}</div>

    <button v-if="confirm" class="addr-banner" type="button" @click="goAddressList">
      <div class="addr-label">订单配送至：</div>
      <div class="addr-main">{{ addressText }}</div>
      <div class="addr-sub">{{ confirm.address?.contactName }} {{ confirm.address?.contactPhone }}</div>
      <div class="addr-arrow">›</div>
    </button>

    <div v-if="confirm" class="panel white">
      <div class="order-merchant">{{ confirm.merchant?.name }}</div>
      <div v-for="it in confirm.cartItems" :key="it.id" class="order-row">
        <div class="order-left">
          <div class="order-thumb">
            <img v-if="it.foodImageUrl" :src="it.foodImageUrl" :alt="it.foodName" />
            <div v-else class="thumb-fallback">{{ (it.foodName || '菜')[0] }}</div>
          </div>
          <div>{{ it.foodName }}</div>
        </div>
        <div class="order-mid">x {{ it.quantity }}</div>
        <div class="order-price">￥{{ format0(it.foodPrice) }}</div>
      </div>
      <div class="fee-row">
        <div>配送费</div>
        <div>￥{{ format0(confirm.merchant?.deliveryFee || 0) }}</div>
      </div>
    </div>

    <div v-if="confirm" class="panel white">
      <div class="order-merchant">备注</div>
      <textarea v-model="remark" class="remark" placeholder="请输入备注，例如少辣、先电话联系" rows="3" />
    </div>

    <template #footer>
      <div class="paybar">
        <div class="pay-total">￥{{ format0(totalAmount) }}</div>
        <button class="btn-pay" type="button" :disabled="submitting || !canSubmit" @click="submitOrder">
          {{ submitting ? '提交中' : '去支付' }}
        </button>
      </div>
    </template>
  </MobilePage>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import { orderApi } from '../api/services'
import { useAuth } from '../store/auth'

const route = useRoute()
const router = useRouter()
const { userId } = useAuth()

const merchantId = computed(() => route.query.merchantId)
const confirm = ref(null)
const remark = ref('')
const loading = ref(true)
const error = ref('')
const submitting = ref(false)

const format0 = (value) => Number(value || 0).toFixed(0)

const addressText = computed(() => {
  const a = confirm.value?.address
  if (!a) return '请选择送货地址'
  const parts = [a.province, a.city, a.district, a.detail].filter(Boolean)
  return parts.join('')
})

const totalAmount = computed(() => {
  const goods = Number(confirm.value?.summary?.goodsAmount || 0)
  const delivery = Number(confirm.value?.merchant?.deliveryFee || 0)
  return goods + delivery
})

const canSubmit = computed(() => Boolean(confirm.value?.address?.id && confirm.value?.cartItems?.length))

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    confirm.value = await orderApi.confirm(userId.value, merchantId.value)
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    loading.value = false
  }
}

const goAddressList = () => {
  const returnTo = encodeURIComponent(route.fullPath)
  router.push({ path: '/address-list', query: { merchantId: merchantId.value, returnTo } })
}

const submitOrder = async () => {
  if (!canSubmit.value) return
  submitting.value = true
  error.value = ''
  try {
    const order = await orderApi.create({
      userId: Number(userId.value),
      merchantId: Number(merchantId.value),
      addressId: confirm.value.address.id,
      remark: remark.value,
    })
    router.push(`/pay/${order.id}`)
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

