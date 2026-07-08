import { useState, useEffect, useCallback } from 'react';
import { Plus, Pencil, Trash2, Search, Stethoscope } from 'lucide-react';
import Modal from '../../components/common/Modal';
import Pagination from '../../components/common/Pagination';
import PageTransition from '../../components/common/PageTransition';
import { getAllMedecins, createMedecin, updateMedecin, deleteMedecin, searchMedecins } from '../../api/medecinApi';
import { createRipple } from '../../utils/ripple';
import toast from 'react-hot-toast';

export default function Medecins() {
  const [medecins, setMedecins] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editingMedecin, setEditingMedecin] = useState(null);
  const [form, setForm] = useState({ nom: '', specialite: '', email: '', telephone: '' });

  const fetchMedecins = useCallback(async (p, search) => {
    setLoading(true);
    try {
      const res = search?.trim()
        ? await searchMedecins(search, { page: p, size: 10 })
        : await getAllMedecins({ page: p, size: 10 });
      setMedecins(res.data.content ?? res.data ?? []);
      setTotalPages(res.data.totalPages ?? 1);
    } catch {
      toast.error('Erreur lors du chargement des médecins');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchMedecins(page, searchTerm); }, [page, searchTerm, fetchMedecins]);

  const handleSearch = () => { setPage(0); fetchMedecins(0, searchTerm); };

  const openCreate = () => {
    setEditingMedecin(null);
    setForm({ nom: '', specialite: '', email: '', telephone: '' });
    setModalOpen(true);
  };

  const openEdit = (medecin) => {
    setEditingMedecin(medecin);
    setForm({
      nom: medecin.nom,
      specialite: medecin.specialite,
      email: medecin.email,
      telephone: medecin.telephone,
    });
    setModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingMedecin) {
        await updateMedecin(editingMedecin.id, form);
        toast.success('Médecin modifié avec succès');
      } else {
        await createMedecin(form);
        toast.success('Médecin créé avec succès');
      }
      setModalOpen(false);
      fetchMedecins(page, searchTerm);
    } catch {
      toast.error('Erreur lors de la sauvegarde');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Confirmer la suppression ?')) return;
    try {
      await deleteMedecin(id);
      toast.success('Médecin supprimé');
      fetchMedecins(page, searchTerm);
    } catch {
      toast.error('Erreur lors de la suppression');
    }
  };

  const handlePageChange = (p) => setPage(p);

  if (loading && medecins.length === 0) return (
    <div className="loading-container">
      <div className="spinner spinner-lg" />
      <p>Chargement des médecins...</p>
    </div>
  );

  return (
    <PageTransition>
      <div>
        <div className="page-header">
          <div className="page-header-group">
            <h1 className="page-title">Médecins</h1>
            <p className="page-subtitle">Gestion des médecins</p>
          </div>
        </div>
        <div className="card card-hoverable">
          <div className="card-header">
            <div className="search-bar">
              <Search size={18} />
              <input
                type="text"
                placeholder="Rechercher par spécialité..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              />
            </div>
            <button className="btn btn-primary" onClick={(e) => { createRipple(e); openCreate(); }}>
              <Plus size={16} /> Ajouter Médecin
            </button>
          </div>
          {medecins.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon large">
                <Stethoscope size={40} />
              </div>
              <div className="empty-state-text">Aucun médecin trouvé</div>
              <div className="empty-state-sub">Commencez par ajouter un nouveau médecin</div>
              <button className="btn btn-primary" onClick={(e) => { createRipple(e); openCreate(); }}>
                <Plus size={16} /> Ajouter Médecin
              </button>
            </div>
          ) : (
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>Nom</th>
                    <th>Spécialité</th>
                    <th>Email</th>
                    <th>Téléphone</th>
                    <th style={{ width: 110 }}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {medecins.map((m) => (
                    <tr key={m.id}>
                      <td>{m.nom}</td>
                      <td>
                        <span className="badge badge-accent">
                          <span className="badge-dot" />
                          {m.specialite}
                        </span>
                      </td>
                      <td>{m.email}</td>
                      <td>{m.telephone}</td>
                      <td>
                        <div className="td-actions">
                          <button className="btn btn-ghost btn-icon btn-sm" onClick={(e) => { createRipple(e); openEdit(m); }} title="Modifier">
                            <Pencil size={14} />
                          </button>
                          <button className="btn btn-danger btn-icon btn-sm" onClick={(e) => { createRipple(e); handleDelete(m.id); }} title="Supprimer">
                            <Trash2 size={14} />
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
          title={editingMedecin ? 'Modifier Médecin' : 'Ajouter Médecin'}
          footer={
            <div className="flex gap-2">
              <button className="btn btn-secondary" onClick={(e) => { createRipple(e); setModalOpen(false); }}>Annuler</button>
              <button className="btn btn-primary" form="medecin-form">Enregistrer</button>
            </div>
          }
        >
          <form id="medecin-form" onSubmit={handleSubmit} style={{ padding: '0.5rem 0' }}>
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Nom</label>
                <input className="form-input" value={form.nom} onChange={(e) => setForm({ ...form, nom: e.target.value })} required placeholder="Dr. Martin" />
              </div>
              <div className="form-group">
                <label className="form-label">Spécialité</label>
                <input className="form-input" value={form.specialite} onChange={(e) => setForm({ ...form, specialite: e.target.value })} required placeholder="Cardiologie" />
              </div>
            </div>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input className="form-input" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required placeholder="martin@hopital.fr" />
            </div>
            <div className="form-group">
              <label className="form-label">Téléphone</label>
              <input className="form-input" value={form.telephone} onChange={(e) => setForm({ ...form, telephone: e.target.value })} required placeholder="+33 6 12 34 56 78" />
            </div>
          </form>
        </Modal>
      </div>
    </PageTransition>
  );
}
