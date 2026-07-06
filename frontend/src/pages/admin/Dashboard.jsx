import { useState, useEffect } from 'react';
import { Users, Stethoscope, CalendarDays, FolderOpen } from 'lucide-react';
import StatCard from '../../components/common/StatCard';
import Badge from '../../components/common/Badge';
import { getAllPatients } from '../../api/patientApi';
import { getAllMedecins } from '../../api/medecinApi';
import { getAllRendezVous } from '../../api/rendezVousApi';
import { getAllDossiers } from '../../api/dossierApi';
import toast from 'react-hot-toast';

export default function Dashboard() {
  const [stats, setStats] = useState({ patients: 0, medecins: 0, rendezVous: 0, dossiers: 0 });
  const [recentRdvs, setRecentRdvs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [patientsRes, medecinsRes, rdvsRes, dossiersRes] = await Promise.all([
          getAllPatients({ page: 0, size: 1 }),
          getAllMedecins({ page: 0, size: 1 }),
          getAllRendezVous({ page: 0, size: 5, sort: 'dateRendezVous,desc' }),
          getAllDossiers({ page: 0, size: 1 }),
        ]);
        setStats({
          patients: patientsRes.data.totalElements ?? patientsRes.data.length ?? 0,
          medecins: medecinsRes.data.totalElements ?? medecinsRes.data.length ?? 0,
          rendezVous: rdvsRes.data.totalElements ?? rdvsRes.data.length ?? 0,
          dossiers: dossiersRes.data.totalElements ?? dossiersRes.data.length ?? 0,
        });
        setRecentRdvs(rdvsRes.data.content ?? rdvsRes.data ?? []);
      } catch {
        toast.error('Erreur lors du chargement du tableau de bord');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return <div className="loading-container"><div className="spinner" /></div>;

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Tableau de bord</h1>
        <p className="page-subtitle">Aperçu général du système</p>
      </div>
      <div className="stat-grid">
        <StatCard label="Total Patients" value={stats.patients} icon={<Users size={24} />} />
        <StatCard label="Total Médecins" value={stats.medecins} icon={<Stethoscope size={24} />} />
        <StatCard label="Total Rendez-vous" value={stats.rendezVous} icon={<CalendarDays size={24} />} />
        <StatCard label="Total Dossiers" value={stats.dossiers} icon={<FolderOpen size={24} />} />
      </div>
      <div className="card mt-4">
        <div className="card-header">
          <h3 className="card-title">Rendez-vous récents</h3>
        </div>
        {recentRdvs.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-text">Aucun rendez-vous récent</div>
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Patient</th>
                  <th>Médecin</th>
                  <th>Date</th>
                  <th>Statut</th>
                </tr>
              </thead>
              <tbody>
                {recentRdvs.map((rdv) => (
                  <tr key={rdv.id}>
                    <td>{rdv.patientNom} {rdv.patientPrenom}</td>
                    <td>Dr. {rdv.medecinNom}</td>
                    <td>{new Date(rdv.dateRendezVous).toLocaleDateString('fr-FR')}</td>
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
