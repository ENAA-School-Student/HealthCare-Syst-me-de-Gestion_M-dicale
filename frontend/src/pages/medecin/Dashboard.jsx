import { useState, useEffect } from 'react';
import { Calendar, Users, FileText } from 'lucide-react';
import { getMyRendezVous } from '../../api/rendezVousApi';
import { getAllDossiers } from '../../api/dossierApi';
import StatCard from '../../components/common/StatCard';
import Badge from '../../components/common/Badge';
import toast from 'react-hot-toast';

export default function MedecinDashboard() {
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({ rdvCount: 0, patientCount: 0, dossierCount: 0 });
  const [upcoming, setUpcoming] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [rdvRes, dossierRes] = await Promise.all([
          getMyRendezVous({ page: 0, size: 200 }),
          getAllDossiers({ page: 0, size: 200 }),
        ]);
        const rdvs = Array.isArray(rdvRes.data.content) ? rdvRes.data.content : (Array.isArray(rdvRes.data) ? rdvRes.data : []);
        const dossiers = Array.isArray(dossierRes.data.content) ? dossierRes.data.content : (Array.isArray(dossierRes.data) ? dossierRes.data : []);
        const patients = new Set(rdvs.map(r => r.patientId || r.patient?.id).filter(Boolean));
        setStats({ rdvCount: rdvs.length, patientCount: patients.size, dossierCount: dossiers.length });
        const today = new Date().toISOString().split('T')[0];
        setUpcoming(rdvs.filter(r => (r.date || '').split('T')[0] >= today).sort((a, b) => a.date?.localeCompare(b.date)).slice(0, 5));
      } catch (err) {
        toast.error('Erreur lors du chargement du tableau de bord');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading-container"><div className="spinner" /><p>Chargement…</p></div>;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Tableau de bord</h1>
          <p className="page-subtitle">Vue d'ensemble de votre activité</p>
        </div>
      </div>

      <div className="stat-grid">
        <StatCard label="Rendez-vous" value={stats.rdvCount} icon={<Calendar size={22} />} color="var(--accent)" bg="var(--accent-soft)" />
        <StatCard label="Patients" value={stats.patientCount} icon={<Users size={22} />} color="var(--success)" bg="var(--success-soft)" />
        <StatCard label="Dossiers" value={stats.dossierCount} icon={<FileText size={22} />} color="var(--warning)" bg="var(--warning-soft)" />
      </div>

      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Prochains rendez-vous</h2>
        </div>
        {upcoming.length === 0 ? (
          <div className="empty-state">
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
                    <td>{rdv.date ? new Date(rdv.date).toLocaleDateString('fr-FR') : '—'}</td>
                    <td><Badge status={rdv.statut} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
