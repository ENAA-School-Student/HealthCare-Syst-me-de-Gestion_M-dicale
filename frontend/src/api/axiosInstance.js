import axios from 'axios';

const api = axios.create({
  baseURL: 'https://healthcare-syst-me-de-gestionm-dicale-production.up.railway.app',
  headers: { 'Content-Type': 'application/json' },
});

// Inject JWT on every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('hc_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Handle 401 globally
api.interceptors.response.use(
  (res) => res,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('hc_token');
      localStorage.removeItem('hc_user');
      localStorage.removeItem('hc_profileId');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
