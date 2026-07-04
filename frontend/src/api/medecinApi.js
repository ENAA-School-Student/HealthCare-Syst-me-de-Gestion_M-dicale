import api from './axiosInstance';

export const getAllMedecins   = (params) => api.get('/api/medecins', { params });
export const getMedecinById   = (id) => api.get(`/api/medecins/${id}`);
export const createMedecin    = (data) => api.post('/api/medecins', data);
export const updateMedecin    = (id, data) => api.put(`/api/medecins/${id}`, data);
export const deleteMedecin    = (id) => api.delete(`/api/medecins/${id}`);
export const searchMedecins   = (specialite, params) => api.get('/api/medecins/search', { params: { specialite, ...params } });
export const getMedecinByTele = (tele, params) => api.get('/api/medecins/telephone', { params: { tele, ...params } });
