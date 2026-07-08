import { useState, useEffect, useCallback } from 'react';
import { Eye, Download, FolderOpen, FileText, Calendar } from 'lucide-react';
import Modal from '../../components/common/Modal';
import Pagination from '../../components/common/Pagination';
import PageTransition from '../../components/common/PageTransition';
import { getAllDossiers, getDossierById, downloadDossierPdf } from '../../api/dossierApi';
import { downloadBlob } from '../../utils/downloadBlob';
import { createRipple } from '../../utils/ripple';
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

  if (loading && dossiers.length === 0) return (
    <div className="loading-container">
      <div className="spinner spinner-lg" />
      <p>Chargement des dossiers...</p>
    </div>
  );

  return (
    <PageTransition>
      <div>
        <div className="page-header">
          <div className="page-header-group">
            <h1 className="page-title">Dossiers Médicaux</h1>
            <p className="page-subtitle">Gestion des dossiers médicaux</p>
          </div>
        </div>
        <div className="card card-hoverable">
          <div className="card-header">
            <h3 className="card-title">
              <FileText size={16} style={{ color: 'var(--primary)' }} />
              Liste des dossiers
            </h3>
          </div>
          {dossiers.length === 0 ? (
            <div className="empty-state">
              <div className="empty-state-icon large">
                <FolderOpen size={40} />
              </div>
              <div className="empty-state-text">Aucun dossier trouvé</div>
              <div className="empty-state-sub">Les dossiers médicaux apparaîtront ici une fois créés</div>
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
                    <th style={{ width: 110 }}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {dossiers.map((d) => (
                    <tr key={d.id}>
                      <td style={{ color: 'var(--text-muted)', fontFamily: 'monospace' }}>#{d.id}</td>
                      <td>{d.patient?.nom} {d.patient?.prenom}</td>
                      <td className="truncate" style={{ maxWidth: 180 }}>{d.diagnostic || <span style={{ color: 'var(--text-muted)' }}>—</span>}</td>
                      <td className="truncate" style={{ maxWidth: 180 }}>{d.observation || <span style={{ color: 'var(--text-muted)' }}>—</span>}</td>
                      <td>{d.dateCreation ? new Date(d.dateCreation).toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' }) : '—'}</td>
                      <td>
                        <div className="td-actions">
                          <button className="btn btn-ghost btn-icon btn-sm" onClick={(e) => { createRipple(e); handleView(d.id); }} title="Voir détails">
                            <Eye size={14} />
                          </button>
                          <button className="btn btn-ghost btn-icon btn-sm" onClick={(e) => { createRipple(e); handleDownload(d.id); }} title="Télécharger PDF">
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
          isOpen={viewModalOpen}
          onClose={() => setViewModalOpen(false)}
          title="Détails du dossier"
          footer={
            <div className="flex gap-2">
              <button className="btn btn-secondary" onClick={(e) => { createRipple(e); setViewModalOpen(false); }}>Fermer</button>
            </div>
          }
        >
          <div style={{ padding: '0.5rem 0' }}>
            {selectedDossier && (
              <div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">ID</label>
                    <p className="text-muted" style={{ fontFamily: 'monospace', fontSize: 15 }}>#{selectedDossier.id}</p>
                  </div>
                  <div className="form-group">
                    <label className="form-label">Patient</label>
                    <p className="font-semibold">{selectedDossier.patient?.nom} {selectedDossier.patient?.prenom}</p>
                  </div>
                </div>
                <div className="form-group">
                  <label className="form-label">Diagnostic</label>
                  <div className="card" style={{ padding: 14, background: 'var(--bg-surface-2)', border: '1px solid var(--glass-border)', borderRadius: 'var(--radius-sm)' }}>
                    {selectedDossier.diagnostic || <span className="text-muted">Aucun diagnostic</span>}
                  </div>
                </div>
                <div className="form-group">
                  <label className="form-label">Observation</label>
                  <div className="card" style={{ padding: 14, background: 'var(--bg-surface-2)', border: '1px solid var(--glass-border)', borderRadius: 'var(--radius-sm)' }}>
                    {selectedDossier.observation || <span className="text-muted">Aucune observation</span>}
                  </div>
                </div>
                <div className="form-group">
                  <label className="form-label">Date de Création</label>
                  <p className="text-muted">{selectedDossier.dateCreation ? new Date(selectedDossier.dateCreation).toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' }) : '—'}</p>
                </div>
              </div>
            )}
          </div>
        </Modal>
      </div>
    </PageTransition>
  );
}
