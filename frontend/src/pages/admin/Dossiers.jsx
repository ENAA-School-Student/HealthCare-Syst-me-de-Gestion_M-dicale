import { useState, useEffect, useCallback } from 'react';
import { Eye, Download, FolderOpen } from 'lucide-react';
import Modal from '../../components/common/Modal';
import Pagination from '../../components/common/Pagination';
import { getAllDossiers, getDossierById, downloadDossierPdf } from '../../api/dossierApi';
import { downloadBlob } from '../../utils/downloadBlob';
import toast from 'react-hot-toast';

export default function Dossiers() {
  const [dossiers, setDossiers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [viewModalOpen, setViewModalOpen] = useState(false);
  const [selectedDossier, setSelectedDossier] = useState(null);

  const fetchDossiers = useCallback(async (p) => {
    setLoading(true);
    try {
      const res = await getAllDossiers({ page: p, size: 10 });
      setDossiers(res.data.content ?? res.data ?? []);
      setTotalPages(res.data.totalPages ?? 1);
    } catch {
      toast.error('Erreur lors du chargement des dossiers');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchDossiers(page); }, [page, fetchDossiers]);

  const handleView = async (id) => {
    try {
      const res = await getDossierById(id);
      setSelectedDossier(res.data);
      setViewModalOpen(true);
    } catch {
      toast.error('Erreur lors du chargement du dossier');
    }
  };

  const handleDownload = async (id) => {
    try {
      const res = await downloadDossierPdf(id);
      downloadBlob(res.data, `dossier-${id}.pdf`);
      toast.success('PDF téléchargé');
    } catch {
      toast.error('Erreur lors du téléchargement');
    }
  };

  const handlePageChange = (p) => setPage(p);

  if (loading && dossiers.length === 0) return <div className="loading-container"><div className="spinner" /></div>;

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Dossiers Médicaux</h1>
        <p className="page-subtitle">Gestion des dossiers médicaux</p>
      </div>
      <div className="card">
        <div className="card-header">
          <h3 className="card-title">Liste des dossiers</h3>
        </div>
        {dossiers.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon"><FolderOpen size={48} /></div>
            <div className="empty-state-text">Aucun dossier trouvé</div>
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Patient</th>
                  <th>Diagnostic</th>
                  <th>Observation</th>
                  <th>Date Création</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {dossiers.map((d) => (
                  <tr key={d.id}>
                    <td>{d.id}</td>
                    <td>{d.patient?.nom} {d.patient?.prenom}</td>
                    <td>{d.diagnostic ? `${d.diagnostic.slice(0, 50)}...` : '—'}</td>
                    <td>{d.observation ? `${d.observation.slice(0, 50)}...` : '—'}</td>
                    <td>{d.dateCreation ? new Date(d.dateCreation).toLocaleDateString('fr-FR') : '—'}</td>
                    <td>
                      <div className="flex gap-2">
                        <button className="btn btn-ghost btn-sm" onClick={() => handleView(d.id)} title="Voir détails"><Eye size={14} /></button>
                        <button className="btn btn-secondary btn-sm" onClick={() => handleDownload(d.id)} title="Télécharger PDF"><Download size={14} /></button>
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
        isOpen={viewModalOpen}
        onClose={() => setViewModalOpen(false)}
        title="Détails du dossier"
        footer={
          <div className="flex gap-2">
            <button className="btn btn-secondary" onClick={() => setViewModalOpen(false)}>Fermer</button>
          </div>
        }
      >
        <div style={{ padding: '1rem' }}>
          {selectedDossier && (
            <div>
              <div className="form-row">
                <div className="form-group">
                  <label className="form-label">ID</label>
                  <p className="text-muted">{selectedDossier.id}</p>
                </div>
                <div className="form-group">
                  <label className="form-label">Patient</label>
                  <p className="text-muted">{selectedDossier.patient?.nom} {selectedDossier.patient?.prenom}</p>
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Diagnostic</label>
                <p className="text-muted">{selectedDossier.diagnostic || 'Aucun diagnostic'}</p>
              </div>
              <div className="form-group">
                <label className="form-label">Observation</label>
                <p className="text-muted">{selectedDossier.observation || 'Aucune observation'}</p>
              </div>
              <div className="form-group">
                <label className="form-label">Date de Création</label>
                <p className="text-muted">{selectedDossier.dateCreation ? new Date(selectedDossier.dateCreation).toLocaleDateString('fr-FR') : '—'}</p>
              </div>
            </div>
          )}
        </div>
      </Modal>
    </div>
  );
}
