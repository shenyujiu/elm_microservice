<template>
  <MobilePage title="服务繁忙" :showBack="false">
    <div class="fallback-container">
      <div class="fallback-icon">😰</div>
      <div class="fallback-title">哎呀，服务器繁忙中...</div>
      <div class="fallback-sub">请稍后再试</div>
      <button class="fallback-btn" type="button" @click="retry">返回重试</button>
    </div>
  </MobilePage>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'

const router = useRouter()
const route = useRoute()

const retry = () => {
  const redirect = route.query.redirect || '/'
  router.push(decodeURIComponent(redirect))
}

onMounted(() => {
  document.title = '服务繁忙'
})
</script>