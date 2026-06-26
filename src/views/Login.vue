<template>
  <MobilePage title="用户登陆">
    <form class="form panel white" @submit.prevent="submit">
      <div class="form-row">
        <div class="label">手机号：</div>
        <input v-model="phone" class="input" placeholder="手机号" required />
      </div>
      <div class="form-row">
        <div class="label">密码：</div>
        <input v-model="password" class="input" placeholder="密码" type="password" required />
      </div>
      <div v-if="error" class="error">{{ error }}</div>
      <button class="btn-confirm" type="submit" :disabled="submitting">
        {{ submitting ? '登陆中...' : '登陆' }}
      </button>
      <button class="btn-secondary" type="button" @click="toRegister">去注册</button>
    </form>
  </MobilePage>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import { userApi } from '../api/services'
import { useAuth } from '../store/auth'

const route = useRoute()
const router = useRouter()
const { setAuth } = useAuth()

const phone = ref('')
const password = ref('')
const error = ref('')
const submitting = ref(false)

const submit = async () => {
  submitting.value = true
  error.value = ''
  try {
    const token = await userApi.login({ username: phone.value.trim(), password: password.value })
    setAuth(token, null) // 先设置 token，以便后续请求携带认证信息
    const me = await userApi.me()
    setAuth(token, me) // 更新用户 ID
    const redirect = route.query.redirect ? decodeURIComponent(route.query.redirect) : '/'
    router.replace(redirect)
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    submitting.value = false
  }
}

const toRegister = () => {
  router.push('/register')
}
</script>

