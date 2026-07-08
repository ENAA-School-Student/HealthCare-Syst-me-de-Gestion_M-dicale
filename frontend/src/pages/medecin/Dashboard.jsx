import { useState, useEffect } from 'react';
import { Calendar, Users, FileText } from 'lucide-react';
import { getMyRendezVous } from '../../api/rendezVousApi';
import StatCard from '../../components/common/StatCard';
import Badge from '../../components/common/Badge';
import PageTransition from '../../components/common/PageTransition';
import toast from 'react-hot-toast';

export default function MedecinDashboard() {
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({ rdvCount: 0, patientCount: 0, dossierCount: 0 });
  const [upcoming, setUpcoming] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const rdvRes = await getMyRendezVous({ page: 0, size: 200 });
        const rdvs = Array.isArray(rdvRes.data.content) ? rdvRes.data.content : (Array.isArray(rdvRes.data) ? rdvRes.data : []);
        const patients = new Set(rdvs.map(r => r.patientId || r.patient?.id).filter(Boolean));
        setStats({ rdvCount: rdvs.length, patientCount: patients.size, dossierCount: 0 });
        const today = new Date().toISOString().split('T')[0];
        setUpcoming(rdvs.filter(r => (r.dateRendezVous || '').split('T')[0] >= today).sort((a, b) => a.dateRendezVous?.localeCompare(b.dateRendezVous)).slice(0, 5));
      } catch (err) {
        toast.error('Erreur lors du chargement du tableau de bord');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return (
    <div className="loading-container">
      <div className="spinner spinner-lg" />
      <p>Chargement de votre tableau de bord...</p>
    </div>
  );

  return (
    <PageTransition>
      <div>
        <div className="page-header">
          <div className="page-header-group">
            <h1 className="page-title">Tableau de bord</h1>
            <p className="page-subtitle">Vue d'ensemble de votre activité</p>
          </div>
        </div>

        <div className="stat-grid">
          <StatCard label="Rendez-vous" value={stats.rdvCount} icon={<Calendar size={22} />} color="var(--primary)" bg="var(--primary-soft)" />
          <StatCard label="Patients" value={stats.patientCount} icon={<Users size={22} />} color="var(--success)" bg="var(--success-soft)" />
          <StatCard label="Dossiers" value={stats.dossierCount} icon={<FileText size={22} />} color="var(--warning)" bg="var(--warning-soft)" />
        </div>

        <div className="card card-hoverable">
          <div className="card-header">
            <h2 className="card-title">
              <Calendar size={16} style={{ color: 'var(--primary)' }} />
              Prochains rendez-vous
            </h2>
          </div>
          {upcoming.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon">
                <Calendar size={32} />
              </div>
              <div className="empty-state-text">Aucun rendez-vous à venir</div>
              <div className="empty-state-sub">Vous n'avez pas de rendez-vous programmés</div>
            </div>
          ) : (
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>Patient</th>
                    <th>Date</th>
                    <th>Statut</th>
                  </tr>
                </thead>
                <tbody>
                  {upcoming.map(rdv => (
                    <tr key={rdv.id}>
                      <td>{rdv.patientNom || rdv.patient?.nom || '—'}</td>
                      <td>{rdv.dateRendezVous ? new Date(rdv.dateRendezVous).toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' }) : '—'}</td>
                      <td><Badge status={rdv.statut} /></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </PageTransition>
  );
}
