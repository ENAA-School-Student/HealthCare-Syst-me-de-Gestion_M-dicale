import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/layout/ProtectedRoute'
import AppLayout from './components/layout/AppLayout'
import Login from './pages/auth/Login'
import Register from './pages/auth/Register'
import AdminDashboard from './pages/admin/Dashboard'
import AdminPatients from './pages/admin/Patients'
import AdminMedecins from './pages/admin/Medecins'
import AdminRendezVous from './pages/admin/RendezVous'
import AdminDossiers from './pages/admin/Dossiers'
import MedecinDashboard from './pages/medecin/Dashboard'
import MedecinRendezVous from './pages/medecin/RendezVous'
import MedecinDossiers from './pages/medecin/Dossiers'
import MedecinProfil from './pages/medecin/Profil'
import PatientDashboard from './pages/patient/Dashboard'
import PatientRendezVous from './pages/patient/RendezVous'
import PatientDossier from './pages/patient/Dossier'
import PatientProfil from './pages/patient/Profil'
import './index.css'

function App() {
  return (
    <StrictMode>
      <BrowserRouter>
        <AuthProvider>
          <Toaster position="top-right" toastOptions={{
            style: { background: '#1e293b', color: '#f1f5f9', border: '1px solid rgba(148,163,184,0.1)' }
          }} />
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/" element={<Navigate to="/login" replace />} />

            <Route element={<ProtectedRoute allowedRoles={['ADMIN']}><AppLayout /></ProtectedRoute>}>
              <Route path="/admin/dashboard" element={<AdminDashboard />} />
              <Route path="/admin/patients" element={<AdminPatients />} />
              <Route path="/admin/medecins" element={<AdminMedecins />} />
              <Route path="/admin/rendezvous" element={<AdminRendezVous />} />
              <Route path="/admin/dossiers" element={<AdminDossiers />} />
            </Route>

            <Route element={<ProtectedRoute allowedRoles={['MEDECIN']}><AppLayout /></ProtectedRoute>}>
              <Route path="/medecin/dashboard" element={<MedecinDashboard />} />
              <Route path="/medecin/rendezvous" element={<MedecinRendezVous />} />
              <Route path="/medecin/dossiers" element={<MedecinDossiers />} />
              <Route path="/medecin/profil" element={<MedecinProfil />} />
            </Route>

            <Route element={<ProtectedRoute allowedRoles={['PATIENT']}><AppLayout /></ProtectedRoute>}>
              <Route path="/patient/dashboard" element={<PatientDashboard />} />
              <Route path="/patient/rendezvous" element={<PatientRendezVous />} />
              <Route path="/patient/dossier" element={<PatientDossier />} />
              <Route path="/patient/profil" element={<PatientProfil />} />
            </Route>

            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </StrictMode>
  )
}

createRoot(document.getElementById('app')!).render(<App />)
