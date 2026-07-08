import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  LayoutDashboard, Users, UserRound, CalendarDays,
  FolderOpen, LogOut, Stethoscope, HeartPulse,
  ChevronLeft, Menu
} from 'lucide-react';

const NAV = {
  ADMIN: [
    { label: 'Dashboard',       path: '/admin/dashboard',    icon: LayoutDashboard },
    { label: 'Patients',        path: '/admin/patients',     icon: Users },
    { label: 'Médecins',        path: '/admin/medecins',     icon: Stethoscope },
    { label: 'Rendez-vous',     path: '/admin/rendezvous',   icon: CalendarDays },
    { label: 'Dossiers Méd.',   path: '/admin/dossiers',     icon: FolderOpen },
  ],
  MEDECIN: [
    { label: 'Dashboard',       path: '/medecin/dashboard',  icon: LayoutDashboard },
    { label: 'Mes Rendez-vous', path: '/medecin/rendezvous', icon: CalendarDays },
    { label: 'Dossiers Méd.',   path: '/medecin/dossiers',   icon: FolderOpen },
    { label: 'Mon Profil',      path: '/medecin/profil',     icon: UserRound },
  ],
  PATIENT: [
    { label: 'Dashboard',       path: '/patient/dashboard',  icon: LayoutDashboard },
    { label: 'Mes Rendez-vous', path: '/patient/rendezvous', icon: CalendarDays },
    { label: 'Mon Dossier',     path: '/patient/dossier',    icon: FolderOpen },
    { label: 'Mon Profil',      path: '/patient/profil',     icon: UserRound },
  ],
};

const ROLE_LABEL = { ADMIN: 'Administrateur', MEDECIN: 'Médecin', PATIENT: 'Patient' };

export default function Sidebar({ collapsed, onToggle, mobileOpen, onMobileClose }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const items = NAV[user?.role] || [];

  const handleLogout = () => { logout(); navigate('/login'); };

  return (
    <aside className={`sidebar ${collapsed ? 'collapsed' : ''} ${mobileOpen ? 'mobile-open' : ''}`}>
      <button className="sidebar-toggle" onClick={onToggle} title={collapsed ? 'Développer' : 'Réduire'}>
        <ChevronLeft size={14} />
      </button>

      {mobileOpen && (
        <button
          className="sidebar-toggle"
          onClick={onMobileClose}
          style={{ right: 14, top: 16, transform: 'none', zIndex: 20 }}
          title="Fermer"
        >
          <Menu size={14} />
        </button>
      )}

      <div className="sidebar-logo">
        <div className="sidebar-logo-icon"><HeartPulse size={20} color="white" /></div>
        <div className="sidebar-logo-text">Health<span>Care+</span></div>
      </div>

      <nav className="sidebar-nav">
        <div className="nav-section-label">Navigation</div>
        {items.map(({ label, path, icon: Icon }) => (
          <NavLink
            key={path}
            to={path}
            className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
            data-label={label}
            onClick={onMobileClose}
          >
            <Icon size={16} />
            <span>{label}</span>
          </NavLink>
        ))}
      </nav>

      <div className="sidebar-footer">
        <div className="user-info">
          <div className="user-avatar">{user?.email?.[0]?.toUpperCase() || 'U'}</div>
          <div>
            <div className="user-name">{user?.email?.split('@')[0]}</div>
            <div className="user-role">{ROLE_LABEL[user?.role]}</div>
          </div>
        </div>
        <button className="btn btn-ghost w-full btn-sm" onClick={handleLogout}>
          <LogOut size={14} /> Déconnexion
        </button>
      </div>
    </aside>
  );
}
