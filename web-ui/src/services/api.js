const API_BASE = '/api';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });
  if (!response.ok) {
    throw new Error(`API error: ${response.status}`);
  }
  return response.json();
}

export const parkingApi = {
  getAll: () => request('/parking'),
  getAvailable: () => request('/parking/available'),
  getById: (id) => request(`/parking/${id}`),
  getByOwner: (ownerId) => request(`/parking/owner/${ownerId}`),
  create: (data) => request('/parking', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/parking/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/parking/${id}`, { method: 'DELETE' }),
};

export const bookingApi = {
  getById: (id) => request(`/bookings/${id}`),
  getByUser: (userId) => request(`/bookings/user/${userId}`),
  getByParkingLot: (parkingLotId) => request(`/bookings/parking/${parkingLotId}`),
  create: (data) => request('/bookings', { method: 'POST', body: JSON.stringify(data) }),
  confirm: (id) => request(`/bookings/${id}/confirm`, { method: 'PUT' }),
  cancel: (id) => request(`/bookings/${id}/cancel`, { method: 'PUT' }),
  complete: (id) => request(`/bookings/${id}/complete`, { method: 'PUT' }),
};

export const userApi = {
  getAll: () => request('/users'),
  getById: (id) => request(`/users/${id}`),
  create: (data) => request('/users', { method: 'POST', body: JSON.stringify(data) }),
  delete: (id) => request(`/users/${id}`, { method: 'DELETE' }),
};

export const paymentApi = {
  getById: (id) => request(`/payments/${id}`),
  getByBooking: (bookingId) => request(`/payments/booking/${bookingId}`),
  getByUser: (userId) => request(`/payments/user/${userId}`),
  initiate: (data) => request('/payments', { method: 'POST', body: JSON.stringify(data) }),
  complete: (id) => request(`/payments/${id}/complete`, { method: 'PUT' }),
  refund: (id) => request(`/payments/${id}/refund`, { method: 'PUT' }),
};
