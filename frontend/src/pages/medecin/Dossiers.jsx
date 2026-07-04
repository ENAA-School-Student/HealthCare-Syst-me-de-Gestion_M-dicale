import { useState, useEffect, useCallback } from 'react';
import { Search, FileText, Plus, Download, Eye, Stethoscope, MessageSquare } from 'lucide-react';
import { getAllPatients, searchPatients } from '../../api/patientApi';
import { getAllDossiers, createDossier, getDossierById, addDiagnostic, addObservation, downloadDossierPdf } from '../../api/dossierApi';
import Modal from '../../components/common/Modal';
import Pagination from '../../components/common/Pagination';
import { downloadBlob } from '../../utils/downloadBlob';
import toast from 'react-hot-toast';

const PAGE_SIZE = 10;

export default function Dossiers() {
  const [patients, setPatients] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [dossierMap, setDossierMap] = useState({});

  const [selectedDossier, setSelectedDossier] = useState(null);
  const [dossierModal, setDossierModal] = useState(false);

  const [createModal, setCreateModal] = useState(false);
  const [createPatient, setCreatePatient] = useState(null);

  const [diagnostic, setDiagnostic] = useState('');
  const [observation, setObservation] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const fetchPatients = useCallback(async () => {
    try {
      setLoading(true);
      let res;
      if (searchTerm.trim()) {
        res = await searchPatients(searchTerm.trim(), { page, size: PAGE_SIZE });
      } else {
        res = await getAllPatients({ page, size: PAGE_SIZE });
      }
      const data = res.data;
      const list = data.content || data || [];
      setPatients(Array.isArray(list) ? list : []);
      setTotalPages(data.totalPages || 1);
    } catch (err) {
      toast.error('Erreur lors du chargement des patients');
    } finally {
      setLoading(false);
    }
  }, [page, searchTerm]);

  useEffect(() => { fetchPatients(); }, [fetchPatients]);

  useEffect(() => {
    const fetchDossiers = async () => {
      try {
        const res = await getAllDossiers({ page: 0, size: 500 });
        const list = res.data.content || res.data || [];
        if (Array.isArray(list)) {
          const map = {};
          list.forEach(d => { if (d.patientId || d.patient?.id) map[d.patientId || d.patient.id] = d; });
          setDossierMap(map);
        }
      } catch (_) {}
    };
    fetchDossiers();
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    fetchPatients();
  };

  const openDossier = async (patient) => {
    const existing = dossierMap[patient.id];
    if (existing) {
      try {
        const res = await getDossierById(existing.id);
        setSelectedDossier(res.data);
        setDiagnostic(res.data.diagnostic || '');
        setObservation(res.data.observation || '');
        setDossierModal(true);
      } catch (err) {
        toast.error('Erreur lors du chargement du dossier');
      }
    } else {
      setCreatePatient(patient);
      setCreateModal(true);
    }
  };

  const handleCreateDossier = async () => {
    if (!createPatient) return;
    try {
      setSubmitting(true);
      const res = await createDossier({ patientId: createPatient.id });
      toast.success('Dossier créé avec succès');
      setCreateModal(false);
      setCreatePatient(null);
      const newDossier = res.data;
      setDossierMap(prev => ({ ...prev, [createPatient.id]: newDossier }));
      setSelectedDossier(newDossier);
      setDiagnostic('');
      setObservation('');
      setDossierModal(true);
    } catch (err) {
      toast.error(err.response?.data || 'Erreur lors de la création du dossier');
    } finally {
      setSubmitting(false);
    }
  };

  const handleAddDiagnostic = async () => {
    if (!selectedDossier || !diagnostic.trim()) return;
    try {
      setSubmitting(true);
      const res = await addDiagnostic(selectedDossier.id, { diagnostic: diagnostic.trim() });
      setSelectedDossier(res.data);
      toast.success('Diagnostic ajouté');
    } catch (err) {
      toast.error(err.response?.data || 'Erreur lors de l\'ajout du diagnostic');
    } finally {
      setSubmitting(false);
    }
  };

  const handleAddObservation = async () => {
    if (!selectedDossier || !observation.trim()) return;
    try {
      setSubmitting(true);
      const res = await addObservation(selectedDossier.id, { observation: observation.trim() });
      setSelectedDossier(res.data);
      toast.success('Observation ajoutée');
    } catch (err) {
      toast.error(err.response?.data || 'Erreur lors de l\'ajout de l\'observation');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDownloadPdf = async (id) => {
    try {
      const res = await downloadDossierPdf(id);
      downloadBlob(res.data, `dossier_${id}.pdf`);
      toast.success('PDF téléchargé');
    } catch (err) {
      toast.error('Erreur lors du téléchargement du PDF');
    }
  };

  if (loading) return <div className="loading-container"><div className="spinner" /><p>Chargement…</p></div>;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Dossiers médicaux</h1>
          <p className="page-subtitle">Consultez et gérez les dossiers des patients</p>
        </div>
      </div>

      <div className="card" style={{ marginBottom: 20 }}>
        <form className="search-bar" onSubmit={handleSearch} style={{ maxWidth: 400 }}>
          <Search size={16} style={{ color: 'var(--text-muted)' }} />
          <input placeholder="Rechercher un patient..." value={searchTerm} onChange={e => setSearchTerm(e.target.value)} />
        </form>
      </div>

      <div className="card">
        {patients.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-text">Aucun patient trouvé</div>
            <div className="empty-state-sub">Essayez de modifier votre recherche</div>
          </div>
        ) : (
          <>
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Téléphone</th>
                    <th>Dossier</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {patients.map(p => {
                    const hasDossier = !!dossierMap[p.id];
                    return (
                      <tr key={p.id}>
                        <td>{p.nom || '—'}</td>
                        <td>{p.email || '—'}</td>
                        <td>{p.telephone || '—'}</td>
                        <td>{hasDossier ? <span className="badge badge-success">Créé</span> : <span className="badge badge-warning">Aucun</span>}</td>
                        <td>
                          <div className="td-actions">
                            <button className="btn btn-ghost btn-sm" onClick={() => openDossier(p)}>
                              {hasDossier ? <><Eye size={14} /> Voir dossier</> : <><Plus size={14} /> Créer dossier</>}
                            </button>
                            {hasDossier && (
                              <button className="btn btn-ghost btn-sm" onClick={() => handleDownloadPdf(dossierMap[p.id].id)}>
                                <Download size={14} /> PDF
                              </button>
                            )}
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
            <Pagination page={page} totalPages={totalPages} onChange={setPage} />
          </>
        )}
      </div>

      <Modal isOpen={dossierModal} onClose={() => setDossierModal(false)} title="Dossier médical">
        {selectedDossier && (
          <div>
            <div className="form-group">
              <label className="form-label">Patient ID</label>
              <p>{selectedDossier.patientId || selectedDossier.patient?.id || '—'}</p>
            </div>

            <div className="card" style={{ marginBottom: 16, padding: 16 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 12 }}>
                <Stethoscope size={16} style={{ color: 'var(--accent)' }} />
                <h3 style={{ fontSize: 14, fontWeight: 700 }}>Diagnostic</h3>
              </div>
              <textarea className="form-input" rows={2} value={diagnostic} onChange={e => setDiagnostic(e.target.value)} placeholder="Ajouter un diagnostic..." style={{ resize: 'vertical' }} />
              <button className="btn btn-primary btn-sm mt-4" onClick={handleAddDiagnostic} disabled={submitting || !diagnostic.trim()}>
                {submitting ? 'Enregistrement…' : 'Enregistrer le diagnostic'}
              </button>
            </div>

            <div className="card" style={{ padding: 16 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 12 }}>
                <MessageSquare size={16} style={{ color: 'var(--warning)' }} />
                <h3 style={{ fontSize: 14, fontWeight: 700 }}>Observation</h3>
              </div>
              <textarea className="form-input" rows={2} value={observation} onChange={e => setObservation(e.target.value)} placeholder="Ajouter une observation..." style={{ resize: 'vertical' }} />
              <button className="btn btn-primary btn-sm mt-4" onClick={handleAddObservation} disabled={submitting || !observation.trim()}>
                {submitting ? 'Enregistrement…' : 'Enregistrer l\'observation'}
              </button>
            </div>
          </div>
        )}
        <div className="modal-footer">
          <button className="btn btn-ghost btn-sm" onClick={() => handleDownloadPdf(selectedDossier?.id)}><Download size={14} /> Télécharger PDF</button>
        </div>
      </Modal>

      <Modal isOpen={createModal} onClose={() => { setCreateModal(false); setCreatePatient(null); }} title="Créer un dossier">
        <p style={{ marginBottom: 16 }}>Créer un dossier médical pour <strong>{createPatient?.nom || 'ce patient'}</strong> ?</p>
        <div className="modal-footer">
          <button className="btn btn-ghost" onClick={() => { setCreateModal(false); setCreatePatient(null); }}>Annuler</button>
          <button className="btn btn-primary" onClick={handleCreateDossier} disabled={submitting}>
            {submitting ? 'Création…' : 'Créer le dossier'}
          </button>
        </div>
      </Modal>
    </div>
  );
}
