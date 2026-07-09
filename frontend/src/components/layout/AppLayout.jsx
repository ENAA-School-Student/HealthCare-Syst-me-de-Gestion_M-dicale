import { useState, useCallback } from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import Sidebar from './Sidebar';
import { Menu, Search } from 'lucide-react';

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
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const info = TITLES[pathname] || { title: 'HealthCare+', sub: '' };

  const toggleSidebar = useCallback(() => {
    setSidebarCollapsed(prev => !prev);
  }, []);

  const toggleMobile = useCallback(() => {
    setMobileOpen(prev => !prev);
  }, []);

  const closeMobile = useCallback(() => {
    setMobileOpen(false);
  }, []);

  return (
    <div className="app-layout">
      {/* Mobile overlay */}
      {mobileOpen && (
        <div
          style={{
            position: 'fixed', inset: 0, zIndex: 99,
            background: 'rgba(0,0,0,0.5)',
          }}
          onClick={closeMobile}
        />
      )}

      <Sidebar
        collapsed={sidebarCollapsed}
        onToggle={toggleSidebar}
        mobileOpen={mobileOpen}
        onMobileClose={closeMobile}
      />

      <div className={`main-content ${sidebarCollapsed ? 'expanded' : ''}`}>
        <header className="header">
          <button
            className="btn btn-ghost btn-sm mobile-menu-btn"
            onClick={toggleMobile}
            style={{ display: 'none', padding: 8 }}
          >
            <Menu size={20} />
          </button>
          <div className="header-title">
            {info.title}
            {info.sub && <span>{info.sub}</span>}
          </div>
          <div className="search-bar" style={{ marginLeft: 'auto', minWidth: 220 }}>
            <Search size={16} />
            <input type="text" placeholder="Rechercher..." />
          </div>
        </header>
        <main className="page-body">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
