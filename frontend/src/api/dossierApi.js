import api from './axiosInstance';

export const getAllDossiers       = (params) => api.get('/api/dossiers', { params });
export const getDossierById       = (id) => api.get(`/api/dossiers/${id}`);
export const getMyDossier         = () => api.get('/api/dossiers/me');
export const createDossier        = (data) => api.post('/api/dossiers', data);
export const addDiagnostic        = (id, data) => api.put(`/api/dossiers/${id}/diagnostic`, data);
export const addObservation       = (id, data) => api.put(`/api/dossiers/${id}/observation`, data);
export const downloadDossierPdf   = (id) => api.get(`/api/dossiers/${id}/download`, { responseType: 'blob' });
