import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { CalendarDays, Clock, ArrowRight, Calendar } from 'lucide-react';
import toast from 'react-hot-toast';
import { getMyRendezVous } from '../../api/rendezVousApi';
import StatCard from '../../components/common/StatCard';
import Badge from '../../components/common/Badge';
import PageTransition from '../../components/common/PageTransition';
import { createRipple } from '../../utils/ripple';

export default function Dashboard() {
  const [rdvs, setRdvs] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => { loadRdvs(); }, []);

  const loadRdvs = async () => {
    try {
      const res = await getMyRendezVous({ page: 0, size: 50 });
      setRdvs(res.data.content || res.data || []);
    } catch {
      toast.error('Erreur lors du chargement des rendez-vous');
    } finally {
      setLoading(false);
    }
  };

  const now = new Date();
  const nextRdv = rdvs
    .filter(r => r.statut !== 'ANNULE' && r.statut !== 'TERMINE' && new Date(r.dateRendezVous) > now)
    .sort((a, b) => new Date(a.dateRendezVous) - new Date(b.dateRendezVous))[0];

  const statCards = [
    { label: 'Mes Rendez-vous', value: rdvs.length, icon: <CalendarDays size={20} />, color: 'var(--primary)', bg: 'var(--primary-soft)' },
    { label: 'Prochain Rendez-vous', value: nextRdv ? new Date(nextRdv.dateRendezVous).toLocaleDateString('fr-FR', { day: 'numeric', month: 'short' }) : 'Aucun', icon: <Clock size={20} />, color: 'var(--success)', bg: 'var(--success-soft)' },
  ];

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner spinner-lg" />
        <p>Chargement de votre tableau de bord...</p>
      </div>
    );
  }

  const upcoming = rdvs.filter(r => r.statut !== 'ANNULE' && r.statut !== 'TERMINE');

  return (
    <PageTransition>
      <div>
        <div className="stat-grid">
          {statCards.map((s, i) => <StatCard key={i} {...s} />)}
        </div>

        <div className="card card-hoverable" style={{ marginTop: 24 }}>
          <div className="card-header">
            <h3 className="card-title">
              <Calendar size={16} style={{ color: 'var(--primary)' }} />
              Prochains Rendez-vous
            </h3>
          </div>
          {upcoming.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">
                <CalendarDays size={32} />
              </div>
              <div className="empty-state-text">Aucun rendez-vous à venir</div>
              <div className="empty-state-sub">Vous pouvez en prendre un depuis la page dédiée</div>
              <button className="btn btn-primary" onClick={(e) => { createRipple(e); navigate('/patient/rendezvous'); }}>
                Prendre un rendez-vous
              </button>
            </div>
          ) : (
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>Médecin</th>
                    <th>Date</th>
                    <th>Statut</th>
                  </tr>
                </thead>
                <tbody>
                  {upcoming.slice(0, 5).map(rdv => (
                    <tr key={rdv.id}>
                      <td>{rdv.medecinNom || '—'}</td>
                      <td>{new Date(rdv.dateRendezVous).toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' })}</td>
                      <td><Badge status={rdv.statut} /></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
          {upcoming.length > 5 && (
            <div style={{ padding: '12px 16px', textAlign: 'right' }}>
              <button className="btn btn-ghost btn-sm" onClick={(e) => { createRipple(e); navigate('/patient/rendezvous'); }}>
                Voir tout <ArrowRight size={14} />
              </button>
            </div>
          )}
        </div>
      </div>
    </PageTransition>
  );
}
