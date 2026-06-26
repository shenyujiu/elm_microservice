<template>
  <MobilePage title="商家列表">
    <div class="panel white">
      <div class="search-row">
        <input v-model="keywordInput" class="search-input" placeholder="搜索商家" @keydown.enter="applyKeyword" />
        <button class="btn-blue" type="button" @click="applyKeyword">搜索</button>
      </div>
      <div class="chip-row">
        <button class="chip" :class="{ active: !categoryId }" type="button" @click="selectCategory(null)">全部</button>
        <button
          v-for="c in categories"
          :key="c.id"
          class="chip"
          :class="{ active: String(c.id) === String(categoryId) }"
          type="button"
          @click="selectCategory(c)"
        >
          {{ c.name }}
        </button>
      </div>
    </div>

    <div v-if="loading" class="hint panel">加载中...</div>
    <div v-if="error" class="error panel">{{ error }}</div>
    <div v-if="!loading && !error && merchants.length === 0" class="hint panel">暂无商家</div>

    <div class="merchant-list">
      <button v-for="m in merchants" :key="m.id" class="merchant-row" type="button" @click="goMerchant(m.id)">
        <div class="merchant-thumb badge-wrap">
          <img v-if="m.coverUrl" :src="m.coverUrl" :alt="m.name" />
          <div v-else class="thumb-fallback">{{ (m.name || '店')[0] }}</div>
          <div v-if="cartCount[m.id] > 0" class="badge">{{ cartCount[m.id] }}</div>
        </div>
        <div class="merchant-main">
          <div class="merchant-title">
            <div class="merchant-name">{{ m.name }}</div>
            <div class="arrow">›</div>
          </div>
          <div class="merchant-sub">
            <span>￥{{ format0(m.startPrice) }}起送</span>
            <span class="split">|</span>
            <span>￥{{ format0(m.deliveryFee) }}配送</span>
          </div>
          <div class="merchant-sub">{{ m.announcement || '各种饺子水饺' }}</div>
        </div>
      </button>
    </div>

    <template #footer>
      <TabBar />
    </template>
  </MobilePage>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import TabBar from '../components/TabBar.vue'
import { cartApi, merchantApi } from '../api/services'
import { useAuth } from '../store/auth'

const route = useRoute()
const router = useRouter()
const { userId, isLoggedIn } = useAuth()

const categories = ref([])
const merchants = ref([])
const loading = ref(true)
const error = ref('')
const cartCount = reactive({})

const categoryId = computed(() => route.query.categoryId || '')
const keyword = computed(() => route.query.keyword || '')
const keywordInput = ref(keyword.value || '')

watch(keyword, (v) => {
  keywordInput.value = v || ''
})

const format0 = (value) => Number(value || 0).toFixed(0)
const goMerchant = (id) => router.push(`/business/${id}`)

const setQuery = (next) => {
  router.replace({ path: '/discover', query: { ...route.query, ...next } })
}

const selectCategory = (c) => {
  if (!c) {
    const { categoryId: _c, categoryName: _n, ...rest } = route.query
    router.replace({ path: '/discover', query: rest })
    return
  }
  setQuery({ categoryId: c.id, categoryName: c.name })
}

const applyKeyword = () => {
  const v = keywordInput.value.trim()
  if (!v) {
    const { keyword: _k, ...rest } = route.query
    router.replace({ path: '/discover', query: rest })
    return
  }
  setQuery({ keyword: v })
}

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    const [cs, ms] = await Promise.all([
      merchantApi.listCategories(),
      merchantApi.listMerchants({
        categoryId: categoryId.value || undefined,
        keyword: keyword.value || undefined,
      }),
    ])
    categories.value = cs || []
    merchants.value = ms || []
    Object.keys(cartCount).forEach((k) => delete cartCount[k])

    if (isLoggedIn.value) {
      await Promise.all(
        merchants.value.map(async (m) => {
          try {
            const summary = await cartApi.getMerchantSummary(userId.value, m.id)
            cartCount[m.id] = summary?.totalQuantity || 0
          } catch {
            cartCount[m.id] = 0
          }
        }),
      )
    }
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    loading.value = false
  }
}

onMounted(load)
watch([categoryId, keyword, () => userId.value], load)
</script>
