import { useState, useEffect, useCallback } from 'react';
import { Eye, CheckCircle, XCircle, CalendarDays } from 'lucide-react';
import { getMyRendezVous, updateRendezVous, annulerRendezVous } from '../../api/rendezVousApi';
import Badge from '../../components/common/Badge';
import Modal from '../../components/common/Modal';
import Pagination from '../../components/common/Pagination';
import PageTransition from '../../components/common/PageTransition';
import { createRipple } from '../../utils/ripple';
import toast from 'react-hot-toast';

const PAGE_SIZE = 10;

export default function RendezVous() {
  const [rdvs, setRdvs] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState(null);

  const fetchRdvs = useCallback(async () => {
    try {
      setLoading(true);
      const res = await getMyRendezVous({ page, size: PAGE_SIZE });
      const data = res.data;
      setRdvs(data.content || data || []);
      setTotalPages(data.totalPages || 1);
    } catch (err) {
      toast.error('Erreur lors du chargement des rendez-vous');
    } finally {
      setLoading(false);
    }
  }, [page]);

  useEffect(() => { fetchRdvs(); }, [fetchRdvs]);

  const handleConfirm = async (id) => {
    try {
      const rdv = rdvs.find(r => r.id === id);
      await updateRendezVous(id, {
        dateRendezVous: rdv?.dateRendezVous,
        statut: 'CONFIRME',
      });
      toast.success('Rendez-vous confirmé');
      fetchRdvs();
    } catch (err) {
      const d = err.response?.data;
      toast.error(typeof d === 'string' ? d : 'Erreur lors de la confirmation');
    }
  };

  const handleCancel = async (id) => {
    try {
      await annulerRendezVous(id);
      toast.success('Rendez-vous annulé');
      fetchRdvs();
    } catch (err) {
      const d = err.response?.data;
      toast.error(typeof d === 'string' ? d : "Erreur lors de l'annulation");
    }
  };

  if (loading) return (
    <div className="loading-container">
      <div className="spinner spinner-lg" />
      <p>Chargement des rendez-vous...</p>
    </div>
  );

  return (
    <PageTransition>
      <div>
        <div className="page-header">
          <div className="page-header-group">
            <h1 className="page-title">Mes rendez-vous</h1>
            <p className="page-subtitle">Gérez vos rendez-vous patients</p>
          </div>
        </div>

        <div className="card card-hoverable">
          {rdvs.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon large">
                <CalendarDays size={40} />
              </div>
              <div className="empty-state-text">Aucun rendez-vous</div>
              <div className="empty-state-sub">Vous n'avez pas encore de rendez-vous</div>
            </div>
          ) : (
            <>
              <div className="table-wrapper">
                <table>
                  <thead>
                    <tr>
                      <th>Patient</th>
                      <th>Date</th>
                      <th>Statut</th>
                      <th style={{ width: 200 }}>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rdvs.map(rdv => (
                      <tr key={rdv.id}>
                        <td>{rdv.patientNom || rdv.patient?.nom || '—'}</td>
                        <td>{rdv.dateRendezVous ? new Date(rdv.dateRendezVous).toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' }) : '—'}</td>
                        <td><Badge status={rdv.statut} /></td>
                        <td>
                          <div className="td-actions">
                            <button className="btn btn-ghost btn-sm" onClick={(e) => { createRipple(e); setSelected(rdv); }}>
                              <Eye size={14} /> Détails
                            </button>
                            {rdv.statut === 'EN_ATTENTE' && (
                              <>
                                <button className="btn btn-primary btn-sm" onClick={(e) => { createRipple(e); handleConfirm(rdv.id); }}>
                                  <CheckCircle size={14} /> Confirmer
                                </button>
                                <button className="btn btn-danger btn-sm" onClick={(e) => { createRipple(e); handleCancel(rdv.id); }}>
                                  <XCircle size={14} /> Annuler
                                </button>
                              </>
                            )}
                            {rdv.statut === 'CONFIRME' && (
                              <button className="btn btn-danger btn-sm" onClick={(e) => { createRipple(e); handleCancel(rdv.id); }}>
                                <XCircle size={14} /> Annuler
                              </button>
                            )}
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <Pagination page={page} totalPages={totalPages} onChange={setPage} />
            </>
          )}
        </div>

        <Modal isOpen={!!selected} onClose={() => setSelected(null)} title="Détails du rendez-vous">
          {selected && (
            <div style={{ padding: '0 0 8px' }}>
              <div className="form-group">
                <label className="form-label">Patient</label>
                <p className="font-semibold">{selected.patientNom || selected.patient?.nom || '—'}</p>
              </div>
              <div className="form-group">
                <label className="form-label">Date</label>
                <p>{selected.date ? new Date(selected.date).toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' }) : '—'}</p>
              </div>
              <div className="form-group">
                <label className="form-label">Statut</label>
                <p><Badge status={selected.statut} /></p>
              </div>
            </div>
          )}
        </Modal>
      </div>
    </PageTransition>
  );
}
