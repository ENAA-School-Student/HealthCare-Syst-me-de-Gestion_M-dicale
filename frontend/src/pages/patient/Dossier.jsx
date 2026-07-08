import { useState, useEffect } from 'react';
import { Download, FileText, Calendar, ClipboardList } from 'lucide-react';
import toast from 'react-hot-toast';
import { getMyDossier, downloadDossierPdf } from '../../api/dossierApi';
import { downloadBlob } from '../../utils/downloadBlob';
import PageTransition from '../../components/common/PageTransition';
import { createRipple } from '../../utils/ripple';

export default function Dossier() {
  const [dossier, setDossier] = useState(null);
  const [loading, setLoading] = useState(true);
  const [downloading, setDownloading] = useState(false);

  useEffect(() => { loadDossier(); }, []);

  const loadDossier = async () => {
    try {
      const res = await getMyDossier();
      setDossier(res.data);
    } catch (err) {
      if (err.response?.status !== 404) {
        toast.error('Erreur lors du chargement du dossier');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleDownload = async () => {
    if (!dossier?.id) return;
    setDownloading(true);
    try {
      const res = await downloadDossierPdf(dossier.id);
      downloadBlob(res.data, `dossier_medical_${dossier.id}.pdf`);
      toast.success('Téléchargement réussi');
    } catch {
      toast.error('Erreur lors du téléchargement');
    } finally {
      setDownloading(false);
    }
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="spinner spinner-lg" />
        <p>Chargement de votre dossier médical...</p>
      </div>
    );
  }

  if (!dossier) {
    return (
      <PageTransition>
        <div className="empty-state">
          <div className="empty-state-icon large">
            <ClipboardList size={40} />
          </div>
          <div className="empty-state-text">Aucun dossier médical</div>
          <div className="empty-state-sub">Votre dossier médical sera créé lors de votre première consultation</div>
        </div>
      </PageTransition>
    );
  }

  return (
    <PageTransition>
      <div>
        <div className="card card-hoverable">
          <div className="card-header">
            <h3 className="card-title">
              <FileText size={16} style={{ color: 'var(--primary)' }} />
              Mon Dossier Médical
            </h3>
            <button className="btn btn-primary btn-sm" onClick={(e) => { createRipple(e); handleDownload(); }} disabled={downloading}>
              <Download size={14} />
              {downloading ? 'Téléchargement...' : 'Télécharger PDF'}
            </button>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 20, padding: '8px 0' }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label" style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                <Calendar size={14} /> Date de création
              </label>
              <p className="font-semibold" style={{ fontSize: 15 }}>
                {new Date(dossier.dateCreation).toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' })}
              </p>
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Diagnostic</label>
              <div className="card" style={{ padding: 16, background: 'var(--bg-surface-2)', marginTop: 4 }}>
                {dossier.diagnostic || <span className="text-muted">Aucun diagnostic enregistré</span>}
              </div>
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Observation</label>
              <div className="card" style={{ padding: 16, background: 'var(--bg-surface-2)', marginTop: 4 }}>
                {dossier.observation || <span className="text-muted">Aucune observation enregistrée</span>}
              </div>
            </div>
          </div>
        </div>
      </div>
    </PageTransition>
  );
}
