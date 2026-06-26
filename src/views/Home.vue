<template>
  <MobilePage :showHeader="false" :noPadding="true">
    <div class="home-hero">
      <div class="home-location">
        <div class="loc-left">📍沈阳市规划大厦</div>
        <div class="loc-right">▾</div>
      </div>
      <div class="home-search">
        <input v-model="keyword" class="search-input" placeholder="搜索饿了么商家、商品名称" @keydown.enter="goSearch" />
      </div>
    </div>

    <div class="panel white">
      <div v-if="loading" class="hint">加载中...</div>
      <div v-if="error" class="error">{{ error }}</div>
      <div class="category-grid">
        <button v-for="item in categories" :key="item.id" class="category-item" type="button" @click="goCategory(item)">
          <div class="category-icon">{{ item.icon || emoji(item.name) }}</div>
          <div class="category-name">{{ item.name }}</div>
        </button>
      </div>
    </div>

    <div class="panel">
      <div class="banner">
        <div>
          <div class="banner-title">品质套餐</div>
          <div class="banner-sub">搭配齐全吃得好</div>
          <div class="banner-link">立即抢购 &gt;</div>
        </div>
        <div class="banner-img">🍢</div>
      </div>
    </div>

    <div class="panel">
      <div class="section-title">推荐商家</div>
      <div class="sort-row">
        <span>综合排序</span>
        <span>距离最近</span>
        <span>销量最高</span>
        <span>筛选</span>
      </div>
      <div class="merchant-list">
        <button v-for="m in merchants" :key="m.id" class="merchant-row" type="button" @click="goMerchant(m.id)">
          <div class="merchant-thumb">
            <img v-if="m.coverUrl" :src="m.coverUrl" :alt="m.name" />
            <div v-else class="thumb-fallback">{{ (m.name || '店')[0] }}</div>
          </div>
          <div class="merchant-main">
            <div class="merchant-title">
              <div class="merchant-name">{{ m.name }}</div>
              <div class="tag">蜂鸟专送</div>
            </div>
            <div class="merchant-sub">
              <span class="stars">★★★★★</span>
              <span>{{ m.rating }}</span>
              <span>月售{{ m.monthlySales }}</span>
            </div>
            <div class="merchant-sub">
              <span>￥{{ format0(m.startPrice) }}起送</span>
              <span>￥{{ format0(m.deliveryFee) }}配送</span>
            </div>
          </div>
        </button>
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
import { merchantApi } from '../api/services'

const router = useRouter()
const categories = ref([])
const merchants = ref([])
const keyword = ref('')
const loading = ref(true)
const error = ref('')

const defaultCategories = [
  { id: 1, name: '美食' },
  { id: 2, name: '早餐' },
  { id: 3, name: '跑腿代购' },
  { id: 4, name: '汉堡披萨' },
  { id: 5, name: '甜品饮品' },
  { id: 6, name: '速食简餐' },
  { id: 7, name: '地方小吃' },
  { id: 8, name: '米粉面食' },
  { id: 9, name: '包子粥铺' },
  { id: 10, name: '烧烤炸串' }
]

const emoji = (name) => {
  if (name?.includes('美食')) return '🍱'
  if (name?.includes('早餐')) return '🥐'
  if (name?.includes('跑腿代购')) return '🚴'
  if (name?.includes('汉堡')) return '🍔'
  if (name?.includes('甜品饮品')) return '🥤'
  if (name?.includes('速食简餐')) return '🍕'
  if (name?.includes('地方小吃')) return '🍜'
  if (name?.includes('米粉面食')) return '🍝'
  if (name?.includes('包子粥铺')) return '🥟'
  if (name?.includes('烧烤炸串')) return '🍢'
  return '🍱'
}

const format0 = (value) => Number(value || 0).toFixed(0)

const goMerchant = (id) => router.push(`/business/${id}`)

const goCategory = (c) => {
  const query = { categoryId: c.id, categoryName: c.name }
  router.push({ path: '/discover', query })
}

const goSearch = () => {
  const q = {}
  if (keyword.value.trim()) q.keyword = keyword.value.trim()
  router.push({ path: '/discover', query: q })
}

onMounted(async () => {
  loading.value = true
  error.value = ''
  try {
    const [cs, ms] = await Promise.all([merchantApi.listCategories(), merchantApi.listMerchants({})])
    const result = [...defaultCategories]
    if (cs && cs.length > 0) {
      cs.forEach(c => {
        const index = result.findIndex(dc => dc.id === c.id)
        if (index >= 0) {
          result[index] = { ...result[index], ...c }
        }
      })
    }
    categories.value = result
    merchants.value = (ms || []).slice(0, 6)
  } catch (e) {
    error.value = ''
    categories.value = defaultCategories
    merchants.value = []
  } finally {
    loading.value = false
  }
})
</script>
