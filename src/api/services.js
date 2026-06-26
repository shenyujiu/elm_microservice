import client from './client'

export const userApi = {
  register: (payload) => client.post('/api/users/register', payload),
  login: (payload) => client.post('/api/users/login', payload),
  me: () => client.get('/api/users/me'),
  listAddresses: (userId) => client.get(`/api/users/${userId}/addresses`),
  getDefaultAddress: (userId) => client.get(`/api/users/${userId}/addresses/default`),
  getAddress: (userId, addressId) => client.get(`/api/users/${userId}/addresses/${addressId}`),
  createAddress: (userId, payload) => client.post(`/api/users/${userId}/addresses`, payload),
  updateAddress: (userId, addressId, payload) => client.put(`/api/users/${userId}/addresses/${addressId}`, payload),
  setDefaultAddress: (userId, addressId) => client.put(`/api/users/${userId}/addresses/${addressId}/default`),
  deleteAddress: (userId, addressId) => client.delete(`/api/users/${userId}/addresses/${addressId}`),
}

export const merchantApi = {
  listCategories: () => client.get('/api/categories'),
  listMerchants: (params = {}) => client.get('/api/merchants', { params }),
  getMerchant: (merchantId) => client.get(`/api/merchants/${merchantId}`),
  listFoods: (merchantId) => client.get(`/api/merchants/${merchantId}/foods`),
}

export const cartApi = {
  getMerchantItems: (userId, merchantId) => client.get(`/api/carts/${userId}/merchants/${merchantId}`),
  getMerchantSummary: (userId, merchantId) => client.get(`/api/carts/${userId}/merchants/${merchantId}/summary`),
  addItem: (userId, payload) => client.post(`/api/carts/${userId}/items`, payload),
  increment: (userId, itemId) => client.put(`/api/carts/${userId}/items/${itemId}/increment`),
  decrement: (userId, itemId) => client.put(`/api/carts/${userId}/items/${itemId}/decrement`),
  clearMerchantCart: (userId, merchantId) => client.delete(`/api/carts/${userId}/merchants/${merchantId}`),
}

export const orderApi = {
  confirm: (userId, merchantId) => client.get('/api/orders/confirm', { params: { userId, merchantId } }),
  create: (payload) => client.post('/api/orders', payload),
  getOrder: (orderId) => client.get(`/api/orders/${orderId}`),
  listUserOrders: (userId) => client.get(`/api/orders/user/${userId}`),
}

export const paymentApi = {
  create: (payload) => client.post('/api/payments/create', payload),
  getByOrderId: (orderId) => client.get(`/api/payments/${orderId}`),
  mockCallback: (payload) => client.post('/api/payments/callback/mock', payload),
}

