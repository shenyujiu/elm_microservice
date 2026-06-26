<template>
  <MobilePage :title="isEdit ? '编辑送货地址' : '新增送货地址'">
    <div v-if="loading" class="hint panel">加载中...</div>
    <div v-if="error" class="error panel">{{ error }}</div>

    <form v-if="!loading" class="form panel white" @submit.prevent="save">
      <div class="form-row">
        <div class="label">联系人：</div>
        <input v-model="form.contactName" class="input" placeholder="联系人姓名" required />
      </div>
      <div class="form-row">
        <div class="label">性别：</div>
        <div class="radio-row">
          <label class="radio">
            <input v-model="form.gender" type="radio" value="男" />
            <span>男</span>
          </label>
          <label class="radio">
            <input v-model="form.gender" type="radio" value="女" />
            <span>女</span>
          </label>
        </div>
      </div>
      <div class="form-row">
        <div class="label">电话：</div>
        <input v-model="form.contactPhone" class="input" placeholder="电话" required />
      </div>
      <div class="form-row">
        <div class="label">收货地址：</div>
        <input v-model="form.detail" class="input" placeholder="收货地址" required />
      </div>
      <button class="btn-confirm" type="submit" :disabled="submitting">
        {{ submitting ? '保存中...' : isEdit ? '更新' : '保存' }}
      </button>
    </form>
  </MobilePage>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MobilePage from '../components/MobilePage.vue'
import { userApi } from '../api/services'
import { useAuth } from '../store/auth'

const route = useRoute()
const router = useRouter()
const { userId } = useAuth()

const addressId = computed(() => route.params.addressId)
const isEdit = computed(() => Boolean(addressId.value))

const loading = ref(false)
const error = ref('')
const submitting = ref(false)

const form = reactive({
  contactName: '',
  gender: '男',
  contactPhone: '',
  detail: '',
})

const load = async () => {
  if (!isEdit.value) return
  loading.value = true
  error.value = ''
  try {
    const a = await userApi.getAddress(userId.value, addressId.value)
    form.contactName = a?.contactName || ''
    form.contactPhone = a?.contactPhone || ''
    form.detail = a?.detail || ''
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    loading.value = false
  }
}

const save = async () => {
  submitting.value = true
  error.value = ''
  try {
    const payload = {
      contactName: form.contactName.trim(),
      contactPhone: form.contactPhone.trim(),
      province: '',
      city: '',
      district: '',
      detail: form.detail.trim(),
      tag: '',
      isDefault: false,
    }
    if (isEdit.value) {
      await userApi.updateAddress(userId.value, addressId.value, payload)
    } else {
      await userApi.createAddress(userId.value, payload)
    }
    router.replace({ path: '/address-list', query: { ...route.query } })
  } catch (e) {
    error.value = e.message || String(e)
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

