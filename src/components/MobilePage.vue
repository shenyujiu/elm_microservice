<template>
  <div class="app-bg">
    <div class="phone">
      <div class="screen">
        <header v-if="showHeader" class="topbar">
          <div class="topbar-side">
            <button v-if="showBack" class="topbar-back" type="button" @click="goBack">‹</button>
          </div>
          <div class="topbar-title">{{ title }}</div>
          <div class="topbar-side">
            <slot name="headerRight" />
          </div>
        </header>
        <main :class="contentClass">
          <slot />
        </main>
        <slot name="footer" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const props = defineProps({
  title: { type: String, default: '' },
  showHeader: { type: Boolean, default: true },
  noPadding: { type: Boolean, default: false },
})

const router = useRouter()
const route = useRoute()

const showBack = computed(() => !route.meta?.tab)

const goBack = () => {
  router.back()
}

const contentClass = computed(() => (props.noPadding ? 'content no-pad' : 'content'))
</script>
