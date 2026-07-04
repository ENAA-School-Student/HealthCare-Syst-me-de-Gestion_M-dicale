import { Outlet, useLocation } from 'react-router-dom';
import Sidebar from './Sidebar';

const TITLES = {
  '/admin/dashboard':   { title: 'Dashboard',        sub: 'Vue globale du système' },
  '/admin/patients':    { title: 'Gestion Patients',  sub: 'CRUD complet des patients' },
  '/admin/medecins':    { title: 'Gestion Médecins',  sub: 'CRUD complet des médecins' },
  '/admin/rendezvous':  { title: 'Rendez-vous',       sub: 'Tous les rendez-vous' },
  '/admin/dossiers':    { title: 'Dossiers Médicaux', sub: 'Gestion des dossiers' },
  '/medecin/dashboard': { title: 'Mon Dashboard',     sub: 'Bienvenue, Docteur' },
  '/medecin/rendezvous':{ title: 'Mes Rendez-vous',   sub: 'Votre planning' },
  '/medecin/dossiers':  { title: 'Dossiers Médicaux', sub: 'Consulter et mettre à jour' },
  '/medecin/profil':    { title: 'Mon Profil',        sub: 'Mes informations' },
  '/patient/dashboard': { title: 'Mon Dashboard',     sub: 'Bienvenue' },
  '/patient/rendezvous':{ title: 'Mes Rendez-vous',   sub: 'Mon planning' },
  '/patient/dossier':   { title: 'Mon Dossier Médical', sub: 'Mes données de santé' },
  '/patient/profil':    { title: 'Mon Profil',        sub: 'Mes informations personnelles' },
};

export default function AppLayout() {
  const { pathname } = useLocation();
  const info = TITLES[pathname] || { title: 'HealthCare+', sub: '' };

  return (
    <div className="app-layout">
      <Sidebar />
      <div className="main-content">
        <header className="header">
          <div className="header-title">
            {info.title}
            {info.sub && <span>{info.sub}</span>}
          </div>
        </header>
        <main className="page-body">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
