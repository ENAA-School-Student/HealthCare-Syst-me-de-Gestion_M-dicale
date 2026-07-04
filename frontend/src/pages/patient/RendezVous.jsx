import { useState, useEffect } from 'react';
import { Plus, X } from 'lucide-react';
import toast from 'react-hot-toast';
import { getMyRendezVous, createRendezVous, annulerRendezVous } from '../../api/rendezVousApi';
import { getAllMedecins } from '../../api/medecinApi';
import Badge from '../../components/common/Badge';
import Pagination from '../../components/common/Pagination';
import Modal from '../../components/common/Modal';

export default function RendezVous() {
  const [rdvs, setRdvs] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [modalOpen, setModalOpen] = useState(false);
  const [medecins, setMedecins] = useState([]);
  const [form, setForm] = useState({ medecinId: '', date: '', comments: '' });
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => { loadRdvs(); }, [page]);

  const loadRdvs = async () => {
    try {
      const res = await getMyRendezVous({ page, size: 10 });
      setRdvs(res.data.content || []);
      setTotalPages(res.data.totalPages || 0);
    } catch {
      toast.error('Erreur lors du chargement');
    } finally {
      setLoading(false);
    }
  };

  const openNew = async () => {
    try {
      const res = await getAllMedecins({ page: 0, size: 100 });
      setMedecins(res.data.content || res.data || []);
      setForm({ medecinId: '', date: '', comments: '' });
      setModalOpen(true);
    } catch {
      toast.error('Erreur lors du chargement des médecins');
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    if (!form.medecinId || !form.date) {
      toast.error('Veuillez remplir tous les champs obligatoires');
      return;
    }
    setSubmitting(true);
    try {
      await createRendezVous({
        medecin: { id: parseInt(form.medecinId) },
        date: form.date,
        comments: form.comments,
      });
      toast.success('Rendez-vous créé avec succès');
      setModalOpen(false);
      loadRdvs();
    } catch (err) {
      toast.error(err.response?.data || 'Erreur lors de la création');
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm('Annuler ce rendez-vous ?')) return;
    try {
      await annulerRendezVous(id);
      toast.success('Rendez-vous annulé');
      loadRdvs();
    } catch {
      toast.error("Erreur lors de l'annulation");
    }
  };

  if (loading) {
    return <div className="loading-container"><div className="spinner" /></div>;
  }

  return (
    <div>
      <div className="page-header">
        <div>
          <div className="page-title">Mes Rendez-vous</div>
          <div className="page-subtitle">Gérez vos rendez-vous médicaux</div>
        </div>
        <button className="btn btn-primary" onClick={openNew}>
          <Plus size={16} /> Nouveau Rendez-vous
        </button>
      </div>

      {rdvs.length === 0 ? (
        <div className="empty-state">
          <div className="empty-state-icon">📅</div>
          <div className="empty-state-text">Aucun rendez-vous</div>
          <div className="empty-state-sub">Prenez votre premier rendez-vous médical</div>
        </div>
      ) : (
        <>
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Médecin</th>
                  <th>Date</th>
                  <th>Statut</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {rdvs.map(rdv => (
                  <tr key={rdv.id}>
                    <td>{rdv.medecin?.nom || '—'}</td>
                    <td>{new Date(rdv.date).toLocaleDateString('fr-FR')}</td>
                    <td><Badge status={rdv.statut} /></td>
                    <td>
                      {rdv.statut === 'EN_ATTENTE' && (
                        <button className="btn btn-danger btn-sm" onClick={() => handleCancel(rdv.id)}>
                          <X size={14} /> Annuler
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <Pagination page={page} totalPages={totalPages} onChange={setPage} />
        </>
      )}

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title="Nouveau Rendez-vous"
        footer={
          <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
            <button className="btn btn-secondary" onClick={() => setModalOpen(false)}>Annuler</button>
            <button className="btn btn-primary" onClick={handleCreate} disabled={submitting}>
              {submitting ? 'Création...' : 'Confirmer'}
            </button>
          </div>
        }
      >
        <form onSubmit={handleCreate} style={{ padding: 16 }}>
          <div className="form-group">
            <label className="form-label">Médecin</label>
            <select
              className="form-select"
              value={form.medecinId}
              onChange={e => setForm(f => ({ ...f, medecinId: e.target.value }))}
              required
            >
              <option value="">Sélectionnez un médecin</option>
              {medecins.map(m => (
                <option key={m.id} value={m.id}>{m.nom} {m.prenom} — {m.specialite}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Date</label>
            <input
              className="form-input"
              type="datetime-local"
              value={form.date}
              onChange={e => setForm(f => ({ ...f, date: e.target.value }))}
              required
            />
          </div>
          <div className="form-group">
            <label className="form-label">Commentaires (optionnel)</label>
            <textarea
              className="form-input"
              rows={3}
              value={form.comments}
              onChange={e => setForm(f => ({ ...f, comments: e.target.value }))}
            />
          </div>
        </form>
      </Modal>
    </div>
  );
}
