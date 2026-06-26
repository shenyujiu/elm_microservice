<template>
  <MobilePage title="地址管理">

    <div v-if="loading" class="hint panel">加载中...</div>
    <div v-if="error" class="error panel">{{ error }}</div>

    <div class="addr-list">
      <button v-for="a in addresses" :key="a.id" class="addr-row" type="button" @click="selectAddress(a.id)">
        <div class="addr-main">
          <div class="addr-title">
            <span>{{ a.contactName }} {{ a.contactPhone }}</span>
            <span v-if="a.isDefault" class="badge-default">默认</span>
          </div>
          <div class="addr-detail">{{ buildAddr(a) }}</div>
        </div>
        <div class="addr-actions">
          <button class="icon-btn" type="button" @click.stop="goEdit(a.id)">✎</button>
          <button class="icon-btn danger" type="button" @click.stop="remove(a.id)">✕</button>
        </div>
      </button>
    </div>

    <button class="addr-add" type="button" @click="goAdd">＋ 新增收货地址</button>
  </MobilePage>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import { userApi } from '../api/services'
import { useAuth } from '../store/auth'

const route = useRoute()
const router = useRouter()
const { userId } = useAuth()

const returnTo = computed(() => route.query.returnTo || '')

const addresses = ref([])
const loading = ref(true)
const error = ref('')

const buildAddr = (a) => {
  const parts = [a.province, a.city, a.district, a.detail].filter(Boolean)
  return parts.join('')
}

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    addresses.value = (await userApi.listAddresses(userId.value)) || []
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    loading.value = false
  }
}

const selectAddress = async (addressId) => {
  if (!returnTo.value) return
  error.value = ''
  try {
    await userApi.setDefaultAddress(userId.value, addressId)
    router.replace(decodeURIComponent(returnTo.value))
  } catch (e) {
    error.value = e.message || String(e)
  }
}

const goAdd = () => {
  router.push({ path: '/address-add', query: { ...route.query } })
}

const goEdit = (id) => {
  router.push({ path: `/address-edit/${id}`, query: { ...route.query } })
}

const remove = async (id) => {
  error.value = ''
  try {
    await userApi.deleteAddress(userId.value, id)
    await load()
  } catch (e) {
    error.value = e.message || String(e)
  }
}

onMounted(load)
</script>

