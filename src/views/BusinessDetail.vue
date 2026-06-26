<template>
  <MobilePage title="商家信息">
    <div v-if="loading" class="hint panel">加载中...</div>
    <div v-if="error" class="error panel">{{ error }}</div>

    <div v-if="merchant" class="panel white center">
      <div class="hero-img">
        <img v-if="merchant.coverUrl" :src="merchant.coverUrl" :alt="merchant.name" />
        <div v-else class="hero-fallback">{{ (merchant.name || '店')[0] }}</div>
      </div>
      <div class="hero-name">{{ merchant.name }}</div>
      <div class="hero-sub">￥{{ format0(merchant.startPrice) }}起送 ￥{{ format0(merchant.deliveryFee) }}配送</div>
      <div class="hero-sub">{{ merchant.announcement || '' }}</div>
    </div>

    <div class="food-list">
      <div v-for="f in foods" :key="f.id" class="food-row">
        <div class="food-thumb">
          <img v-if="f.imageUrl" :src="f.imageUrl" :alt="f.name" />
          <div v-else class="thumb-fallback">{{ (f.name || '菜')[0] }}</div>
        </div>
        <div class="food-main">
          <div class="food-name">{{ f.name }}</div>
          <div class="food-sub">{{ f.description || '' }}</div>
          <div class="food-price">￥{{ format0(f.price) }}</div>
        </div>
        <div class="stepper">
          <button v-if="qtyMap[f.id] > 0" class="step minus" type="button" :disabled="busy" @click="decrement(f.id)">-</button>
          <div v-if="qtyMap[f.id] > 0" class="qty">{{ qtyMap[f.id] }}</div>
          <button class="step plus" type="button" :disabled="busy" @click="increment(f)">+</button>
        </div>
      </div>
    </div>

    <template #footer>
      <div class="bottom-bar">
        <div class="cart-left">
          <div class="cart-circle">🛒</div>
          <div class="cart-info">
            <div class="cart-amount">￥{{ format2(summary?.goodsAmount || 0) }}</div>
            <div class="cart-note">另需配送费￥{{ format0(merchant?.deliveryFee || 0) }}</div>
          </div>
        </div>
        <button class="btn-pay" type="button" :disabled="!summary?.totalQuantity" @click="goConfirm">去结算</button>
      </div>
    </template>
  </MobilePage>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import { cartApi, merchantApi } from '../api/services'
import { useAuth } from '../store/auth'

const route = useRoute()
const router = useRouter()
const { userId, isLoggedIn } = useAuth()

const merchantId = computed(() => route.params.merchantId)

const merchant = ref(null)
const foods = ref([])
const cartItems = ref([])
const summary = ref(null)
const qtyMap = ref({})

const loading = ref(true)
const error = ref('')
const busy = ref(false)

const format0 = (value) => Number(value || 0).toFixed(0)
const format2 = (value) => Number(value || 0).toFixed(2)

const refreshCart = async () => {
  if (!isLoggedIn.value) {
    cartItems.value = []
    summary.value = null
    qtyMap.value = {}
    return
  }
  const [items, s] = await Promise.all([
    cartApi.getMerchantItems(userId.value, merchantId.value),
    cartApi.getMerchantSummary(userId.value, merchantId.value),
  ])
  cartItems.value = items || []
  summary.value = s
  const map = {}
  cartItems.value.forEach((it) => {
    map[it.foodId] = it.quantity
  })
  qtyMap.value = map
}

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    const [m, fs] = await Promise.all([merchantApi.getMerchant(merchantId.value), merchantApi.listFoods(merchantId.value)])
    merchant.value = m
    foods.value = fs || []
    await refreshCart()
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    loading.value = false
  }
}

const ensureLogin = () => {
  if (!isLoggedIn.value) {
    router.push({ path: '/login', query: { redirect: encodeURIComponent(route.fullPath) } })
    return false
  }
  return true
}

const increment = async (food) => {
  if (!ensureLogin()) return
  busy.value = true
  error.value = ''
  try {
    const existing = cartItems.value.find((it) => it.foodId === food.id)
    if (existing) {
      await cartApi.increment(userId.value, existing.id)
    } else {
      await cartApi.addItem(userId.value, {
        merchantId: Number(merchantId.value),
        foodId: food.id,
        foodName: food.name,
        foodPrice: food.price,
        foodImageUrl: food.imageUrl,
        quantity: 1,
      })
    }
    await refreshCart()
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    busy.value = false
  }
}

const decrement = async (foodId) => {
  if (!ensureLogin()) return
  const existing = cartItems.value.find((it) => it.foodId === foodId)
  if (!existing) return
  busy.value = true
  error.value = ''
  try {
    await cartApi.decrement(userId.value, existing.id)
    await refreshCart()
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    busy.value = false
  }
}

const goConfirm = () => {
  if (!ensureLogin()) return
  router.push({ path: '/confirm', query: { merchantId: merchantId.value } })
}

onMounted(load)
</script>

