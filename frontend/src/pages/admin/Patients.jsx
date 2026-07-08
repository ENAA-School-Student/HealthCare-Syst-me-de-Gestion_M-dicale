import { useState, useEffect, useCallback } from 'react';
import { Plus, Pencil, Trash2, Download, Search, Users } from 'lucide-react';
import Modal from '../../components/common/Modal';
import Pagination from '../../components/common/Pagination';
import PageTransition from '../../components/common/PageTransition';
import { getAllPatients, createPatient, updatePatient, deletePatient, searchPatients, downloadPatientReport } from '../../api/patientApi';
import { downloadBlob } from '../../utils/downloadBlob';
import { createRipple } from '../../utils/ripple';
import toast from 'react-hot-toast';

export default function Patients() {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editingPatient, setEditingPatient] = useState(null);
  const [form, setForm] = useState({ nom: '', prenom: '', telephone: '', dateNaissance: '' });

  const fetchPatients = useCallback(async (p, search) => {
    setLoading(true);
    try {
      const res = search?.trim()
        ? await searchPatients(search, { page: p, size: 10 })
        : await getAllPatients({ page: p, size: 10 });
      setPatients(res.data.content ?? res.data ?? []);
      setTotalPages(res.data.totalPages ?? 1);
    } catch {
      toast.error('Erreur lors du chargement des patients');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchPatients(page, searchTerm); }, [page, searchTerm, fetchPatients]);

  const handleSearch = () => { setPage(0); fetchPatients(0, searchTerm); };

  const openCreate = () => {
    setEditingPatient(null);
    setForm({ nom: '', prenom: '', telephone: '', dateNaissance: '' });
    setModalOpen(true);
  };

  const openEdit = (patient) => {
    setEditingPatient(patient);
    setForm({
      nom: patient.nom,
      prenom: patient.prenom,
      telephone: patient.telephone,
      dateNaissance: patient.dateNaissance ? patient.dateNaissance.split('T')[0] : '',
    });
    setModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingPatient) {
        await updatePatient(editingPatient.id, form);
        toast.success('Patient modifié avec succès');
      } else {
        await createPatient(form);
        toast.success('Patient créé avec succès');
      }
      setModalOpen(false);
      fetchPatients(page, searchTerm);
    } catch {
      toast.error('Erreur lors de la sauvegarde');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Confirmer la suppression ?')) return;
    try {
      await deletePatient(id);
      toast.success('Patient supprimé');
      fetchPatients(page, searchTerm);
    } catch {
      toast.error('Erreur lors de la suppression');
    }
  };

  const handleDownload = async (id) => {
    try {
      const res = await downloadPatientReport(id);
      downloadBlob(res.data, `rapport-patient-${id}.pdf`);
      toast.success('Rapport téléchargé');
    } catch {
      toast.error('Erreur lors du téléchargement');
    }
  };

  const handlePageChange = (p) => setPage(p);

  if (loading && patients.length === 0) return (
    <div className="loading-container">
      <div className="spinner spinner-lg" />
      <p>Chargement des patients...</p>
    </div>
  );

  return (
    <PageTransition>
      <div>
        <div className="page-header">
          <div className="page-header-group">
            <h1 className="page-title">Patients</h1>
            <p className="page-subtitle">Gestion des patients</p>
          </div>
        </div>
        <div className="card card-hoverable">
          <div className="card-header">
            <div className="search-bar">
              <Search size={18} />
              <input
                type="text"
                placeholder="Rechercher par nom..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              />
            </div>
            <button className="btn btn-primary" onClick={(e) => { createRipple(e); openCreate(); }}>
              <Plus size={16} /> Ajouter Patient
            </button>
          </div>
          {patients.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon large">
                <Users size={40} />
              </div>
              <div className="empty-state-text">Aucun patient trouvé</div>
              <div className="empty-state-sub">Commencez par ajouter un nouveau patient</div>
              <button className="btn btn-primary" onClick={(e) => { createRipple(e); openCreate(); }}>
                <Plus size={16} /> Ajouter Patient
              </button>
            </div>
          ) : (
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Téléphone</th>
                    <th>Date Naissance</th>
                    <th style={{ width: 140 }}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {patients.map((p) => (
                    <tr key={p.id}>
                      <td>{p.nom}</td>
                      <td>{p.prenom}</td>
                      <td>{p.telephone}</td>
                      <td>{p.dateNaissance ? new Date(p.dateNaissance).toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' }) : '—'}</td>
                      <td>
                        <div className="td-actions">
                          <button className="btn btn-ghost btn-icon btn-sm" onClick={(e) => { createRipple(e); openEdit(p); }} title="Modifier">
                            <Pencil size={14} />
                          </button>
                          <button className="btn btn-danger btn-icon btn-sm" onClick={(e) => { createRipple(e); handleDelete(p.id); }} title="Supprimer">
                            <Trash2 size={14} />
                          </button>
                          <button className="btn btn-ghost btn-icon btn-sm" onClick={(e) => { createRipple(e); handleDownload(p.id); }} title="Télécharger rapport">
                            <Download size={14} />
                          </button>
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
          title={editingPatient ? 'Modifier Patient' : 'Ajouter Patient'}
          footer={
            <div className="flex gap-2">
              <button className="btn btn-secondary" onClick={(e) => { createRipple(e); setModalOpen(false); }}>Annuler</button>
              <button className="btn btn-primary" form="patient-form">Enregistrer</button>
            </div>
          }
        >
          <form id="patient-form" onSubmit={handleSubmit} style={{ padding: '0.5rem 0' }}>
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Nom</label>
                <input className="form-input" value={form.nom} onChange={(e) => setForm({ ...form, nom: e.target.value })} required placeholder="Dupont" />
              </div>
              <div className="form-group">
                <label className="form-label">Prénom</label>
                <input className="form-input" value={form.prenom} onChange={(e) => setForm({ ...form, prenom: e.target.value })} required placeholder="Jean" />
              </div>
            </div>
            <div className="form-group">
              <label className="form-label">Téléphone</label>
              <input className="form-input" value={form.telephone} onChange={(e) => setForm({ ...form, telephone: e.target.value })} required placeholder="+33 6 12 34 56 78" />
            </div>
            <div className="form-group">
              <label className="form-label">Date de Naissance</label>
              <input className="form-input" type="date" value={form.dateNaissance} onChange={(e) => setForm({ ...form, dateNaissance: e.target.value })} />
            </div>
          </form>
        </Modal>
      </div>
    </PageTransition>
  );
}
