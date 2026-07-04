import api from './axiosInstance';

export const getAllPatients  = (params) => api.get('/api/patients', { params });
export const getPatientById  = (id) => api.get(`/api/patients/${id}`);
export const createPatient   = (data) => api.post('/api/patients', data);
export const updatePatient   = (id, data) => api.put(`/api/patients/${id}`, data);
export const deletePatient   = (id) => api.delete(`/api/patients/${id}`);
export const searchPatients  = (nom, params) => api.get('/api/patients/search', { params: { nom, ...params } });
export const downloadPatientReport = (id) => api.get(`/api/patients/${id}/report`, { responseType: 'blob' });
