import { useState, useEffect, useCallback } from 'react';
import { Plus, Pencil, XCircle, Download, Search, CalendarDays } from 'lucide-react';
import Modal from '../../components/common/Modal';
import Pagination from '../../components/common/Pagination';
import Badge from '../../components/common/Badge';
import { getAllRendezVous, createRendezVous, updateRendezVous, annulerRendezVous, getRendezVousByStatut, getRendezVousByPatient, getRendezVousByMedecin, downloadRendezVousPdf } from '../../api/rendezVousApi';
import { getAllPatients } from '../../api/patientApi';
import { getAllMedecins } from '../../api/medecinApi';
import { downloadBlob } from '../../utils/downloadBlob';
import toast from 'react-hot-toast';

export default function RendezVous() {
  const [rdvs, setRdvs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [filterType, setFilterType] = useState('');
  const [filterValue, setFilterValue] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRdv, setEditingRdv] = useState(null);
  const [patients, setPatients] = useState([]);
  const [medecins, setMedecins] = useState([]);
  const [form, setForm] = useState({ patient: '', medecin: '', date: '', statut: 'EN_ATTENTE' });

  const fetchRdvs = useCallback(async (p, type, value) => {
    setLoading(true);
    try {
      let res;
      if (type === 'statut' && value) {
        res = await getRendezVousByStatut(value, { page: p, size: 10 });
      } else if (type === 'patient' && value) {
        res = await getRendezVousByPatient(value, { page: p, size: 10 });
      } else if (type === 'medecin' && value) {
        res = await getRendezVousByMedecin(value, { page: p, size: 10 });
      } else {
        res = await getAllRendezVous({ page: p, size: 10 });
      }
      setRdvs(res.data.content ?? res.data ?? []);
      setTotalPages(res.data.totalPages ?? 1);
    } catch {
      toast.error('Erreur lors du chargement des rendez-vous');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchRdvs(page, filterType, filterValue); }, [page, filterType, filterValue, fetchRdvs]);

  const fetchDropdowns = async () => {
    try {
      const [pRes, mRes] = await Promise.all([
        getAllPatients({ page: 0, size: 200 }),
        getAllMedecins({ page: 0, size: 200 }),
      ]);
      setPatients(pRes.data.content ?? pRes.data ?? []);
      setMedecins(mRes.data.content ?? mRes.data ?? []);
    } catch {
      toast.error('Erreur lors du chargement des listes');
    }
  };

  const openCreate = async () => {
    setEditingRdv(null);
    setForm({ patient: '', medecin: '', date: '', statut: 'EN_ATTENTE' });
    await fetchDropdowns();
    setModalOpen(true);
  };

  const openEdit = async (rdv) => {
    setEditingRdv(rdv);
    setForm({
      patient: rdv.patient?.id ?? '',
      medecin: rdv.medecin?.id ?? '',
      date: rdv.dateRendezVous ? rdv.dateRendezVous.slice(0, 16) : '',
      statut: rdv.statut,
    });
    await fetchDropdowns();
    setModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = { ...form, patient: { id: form.patient }, medecin: { id: form.medecin } };
      if (editingRdv) {
        await updateRendezVous(editingRdv.id, payload);
        toast.success('Rendez-vous modifié avec succès');
      } else {
        await createRendezVous(payload);
        toast.success('Rendez-vous créé avec succès');
      }
      setModalOpen(false);
      fetchRdvs(page, filterType, filterValue);
    } catch {
      toast.error('Erreur lors de la sauvegarde');
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm('Confirmer l\'annulation ?')) return;
    try {
      await annulerRendezVous(id);
      toast.success('Rendez-vous annulé');
      fetchRdvs(page, filterType, filterValue);
    } catch {
      toast.error('Erreur lors de l\'annulation');
    }
  };

  const handleDownload = async (patientId) => {
    try {
      const res = await downloadRendezVousPdf(patientId);
      downloadBlob(res.data, `rendez-vous-patient-${patientId}.pdf`);
      toast.success('PDF téléchargé');
    } catch {
      toast.error('Erreur lors du téléchargement');
    }
  };

  const handleFilter = (type) => {
    setFilterType(type);
    setFilterValue('');
    setPage(0);
    if (!type) fetchRdvs(0, '', '');
  };

  const handlePageChange = (p) => setPage(p);

  if (loading && rdvs.length === 0) return <div className="loading-container"><div className="spinner" /></div>;

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Rendez-vous</h1>
        <p className="page-subtitle">Gestion des rendez-vous</p>
      </div>
      <div className="card">
        <div className="card-header">
          <div className="flex gap-2">
            <select className="form-select" value={filterType} onChange={(e) => handleFilter(e.target.value)}>
              <option value="">Tous les rendez-vous</option>
              <option value="statut">Par statut</option>
              <option value="patient">Par patient</option>
              <option value="medecin">Par médecin</option>
            </select>
            {filterType && (
              <div className="search-bar">
                <Search size={18} />
                <input
                  type="text"
                  placeholder={filterType === 'statut' ? 'EN_ATTENTE, CONFIRME, ...' : `Nom du ${filterType === 'patient' ? 'patient' : 'médecin'}...`}
                  value={filterValue}
                  onChange={(e) => setFilterValue(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                      setPage(0);
                      fetchRdvs(0, filterType, filterValue);
                    }
                  }}
                />
              </div>
            )}
          </div>
          <button className="btn btn-primary" onClick={openCreate}><Plus size={16} /> Ajouter Rendez-vous</button>
        </div>
        {rdvs.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon"><CalendarDays size={48} /></div>
            <div className="empty-state-text">Aucun rendez-vous trouvé</div>
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
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {rdvs.map((rdv) => (
                  <tr key={rdv.id}>
                    <td>{rdv.patient?.nom} {rdv.patient?.prenom}</td>
                    <td>Dr. {rdv.medecin?.nom}</td>
                    <td>{new Date(rdv.dateRendezVous).toLocaleDateString('fr-FR')}</td>
                    <td><Badge status={rdv.statut} /></td>
                    <td>
                      <div className="flex gap-2">
                        <button className="btn btn-ghost btn-sm" onClick={() => openEdit(rdv)} title="Modifier"><Pencil size={14} /></button>
                        {rdv.statut !== 'ANNULE' && (
                          <button className="btn btn-danger btn-sm" onClick={() => handleCancel(rdv.id)} title="Annuler"><XCircle size={14} /></button>
                        )}
                        <button className="btn btn-secondary btn-sm" onClick={() => handleDownload(rdv.patient?.id)} title="Télécharger PDF"><Download size={14} /></button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
        <Pagination page={page} totalPages={totalPages} onChange={handlePageChange} />
      </div>
      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editingRdv ? 'Modifier Rendez-vous' : 'Ajouter Rendez-vous'}
        footer={
          <div className="flex gap-2">
            <button className="btn btn-secondary" onClick={() => setModalOpen(false)}>Annuler</button>
            <button className="btn btn-primary" form="rdv-form">Enregistrer</button>
          </div>
        }
      >
        <form id="rdv-form" onSubmit={handleSubmit} style={{ padding: '1rem' }}>
          <div className="form-group">
            <label className="form-label">Patient</label>
            <select className="form-select" value={form.patient} onChange={(e) => setForm({ ...form, patient: e.target.value })} required>
              <option value="">Sélectionner un patient</option>
              {patients.map((p) => (
                <option key={p.id} value={p.id}>{p.nom} {p.prenom}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Médecin</label>
            <select className="form-select" value={form.medecin} onChange={(e) => setForm({ ...form, medecin: e.target.value })} required>
              <option value="">Sélectionner un médecin</option>
              {medecins.map((m) => (
                <option key={m.id} value={m.id}>{m.nom} — {m.specialite}</option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Date</label>
            <input className="form-input" type="datetime-local" value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} required />
          </div>
          <div className="form-group">
            <label className="form-label">Statut</label>
            <select className="form-select" value={form.statut} onChange={(e) => setForm({ ...form, statut: e.target.value })} required>
              <option value="EN_ATTENTE">En attente</option>
              <option value="CONFIRME">Confirmé</option>
              <option value="ANNULE">Annulé</option>
              <option value="TERMINE">Terminé</option>
            </select>
          </div>
        </form>
      </Modal>
    </div>
  );
}
