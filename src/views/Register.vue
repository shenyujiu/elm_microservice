<template>
  <MobilePage title="用户注册">
    <form class="form panel white" @submit.prevent="submit">
      <div class="form-row">
        <div class="label">手机号：</div>
        <input v-model="phone" class="input" placeholder="手机号" required />
      </div>
      <div class="form-row">
        <div class="label">密码：</div>
        <input v-model="password" class="input" placeholder="密码" type="password" required />
      </div>
      <div class="form-row">
        <div class="label">确认密码：</div>
        <input v-model="confirmPassword" class="input" placeholder="确认密码" type="password" required />
      </div>
      <div class="form-row">
        <div class="label">用户姓名：</div>
        <input v-model="nickname" class="input" placeholder="用户姓名" required />
      </div>
      <div class="form-row">
        <div class="label">性别：</div>
        <div class="radio-row">
          <label class="radio">
            <input v-model="gender" type="radio" value="男" />
            <span>男</span>
          </label>
          <label class="radio">
            <input v-model="gender" type="radio" value="女" />
            <span>女</span>
          </label>
        </div>
      </div>
      <div v-if="error" class="error">{{ error }}</div>
      <div v-if="success" class="success">{{ success }}</div>
      <button class="btn-confirm" type="submit" :disabled="submitting">
        {{ submitting ? '注册中...' : '注册' }}
      </button>
    </form>
  </MobilePage>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import { userApi } from '../api/services'

const router = useRouter()

const phone = ref('')
const password = ref('')
const confirmPassword = ref('')
const nickname = ref('')
const gender = ref('男')
const error = ref('')
const success = ref('')
const submitting = ref(false)

const submit = async () => {
  if (password.value !== confirmPassword.value) {
    error.value = '两次输入的密码不一致'
    return
  }
  submitting.value = true
  error.value = ''
  success.value = ''
  try {
    await userApi.register({
      username: phone.value.trim(),
      password: password.value,
      phone: phone.value.trim(),
      nickname: nickname.value.trim(),
      gender: gender.value,
    })
    success.value = '注册成功，请返回登录'
    setTimeout(() => router.replace('/login'), 600)
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    submitting.value = false
  }
}
</script>

