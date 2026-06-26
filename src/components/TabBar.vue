<template>
  <nav class="tabbar">
    <router-link class="tab" :class="{ active: isActive('/') }" to="/">
      <div class="tab-icon">🏠</div>
      <div class="tab-text">首页</div>
    </router-link>
    <router-link class="tab" :class="{ active: isActive('/discover') }" to="/discover">
      <div class="tab-icon">🧭</div>
      <div class="tab-text">发现</div>
    </router-link>
    <router-link class="tab" :class="{ active: isActive('/orders') }" to="/orders">
      <div class="tab-icon">📄</div>
      <div class="tab-text">订单</div>
    </router-link>
    <router-link class="tab" :class="{ active: isActive('/my') }" to="/my">
      <div class="tab-icon">👤</div>
      <div class="tab-text">我的</div>
    </router-link>
  </nav>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()

const isActive = (path) => {
  if (path === '/') return route.path === '/'
  return route.path.startsWith(path)
}
</script>

