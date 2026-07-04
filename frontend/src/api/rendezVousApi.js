import api from './axiosInstance';

export const getAllRendezVous       = (params) => api.get('/api/rendezvous', { params });
export const getMyRendezVous        = (params) => api.get('/api/rendezvous/me', { params });
export const createRendezVous       = (data) => api.post('/api/rendezvous', data);
export const updateRendezVous       = (id, data) => api.put(`/api/rendezvous/${id}`, data);
export const annulerRendezVous      = (id) => api.put(`/api/rendezvous/${id}/annuler`);
export const getRendezVousByStatut  = (statut, params) => api.get(`/api/rendezvous/statut/${statut}`, { params });
export const getRendezVousByPatient = (nom, params) => api.get(`/api/rendezvous/patient/${nom}`, { params });
export const getRendezVousByMedecin = (nom, params) => api.get(`/api/rendezvous/medecin/${nom}`, { params });
export const getRendezVousByMedecinDate = (id, date, params) => api.get('/api/rendezvous/medecin', { params: { id, date, ...params } });
export const downloadRendezVousPdf  = (patientId) => api.get(`/api/rendezvous/patient/${patientId}/download`, { responseType: 'blob' });
