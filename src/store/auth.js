import { computed, ref } from 'vue'

const TOKEN_KEY = 'elm_token'
const USER_ID_KEY = 'elm_user_id'

const tokenRef = ref(localStorage.getItem(TOKEN_KEY) || '')
const userIdRef = ref(localStorage.getItem(USER_ID_KEY) || '')

export function useAuth() {
  const isLoggedIn = computed(() => Boolean(tokenRef.value && userIdRef.value))

  const setAuth = (token, userId) => {
    tokenRef.value = token || ''
    userIdRef.value = userId ? String(userId) : ''
    if (tokenRef.value) {
      localStorage.setItem(TOKEN_KEY, tokenRef.value)
    } else {
      localStorage.removeItem(TOKEN_KEY)
    }
    if (userIdRef.value) {
      localStorage.setItem(USER_ID_KEY, userIdRef.value)
    } else {
      localStorage.removeItem(USER_ID_KEY)
    }
  }

  const logout = () => setAuth('', '')

  return {
    token: tokenRef,
    userId: userIdRef,
    isLoggedIn,
    setAuth,
    logout,
  }
}

