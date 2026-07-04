import { useState, useEffect } from 'react';
import { Download } from 'lucide-react';
import toast from 'react-hot-toast';
import { getMyDossier, downloadDossierPdf } from '../../api/dossierApi';
import { downloadBlob } from '../../utils/downloadBlob';

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
    return <div className="loading-container"><div className="spinner" /></div>;
  }

  if (!dossier) {
    return (
      <div className="empty-state">
        <div className="empty-state-icon">📋</div>
        <div className="empty-state-text">Aucun dossier médical</div>
        <div className="empty-state-sub">Votre dossier médical sera créé lors de votre première consultation</div>
      </div>
    );
  }

  return (
    <div>
      <div className="card">
        <div className="card-header">
          <h3 className="card-title">Mon Dossier Médical</h3>
          <button className="btn btn-primary btn-sm" onClick={handleDownload} disabled={downloading}>
            <Download size={14} />
            {downloading ? 'Téléchargement...' : 'Télécharger PDF'}
          </button>
        </div>
        <div style={{ padding: 16, display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div>
            <div className="form-label">Date de création</div>
            <div>{new Date(dossier.dateCreation).toLocaleDateString('fr-FR')}</div>
          </div>
          <div>
            <div className="form-label">Diagnostic</div>
            <div>{dossier.diagnostic || 'Aucun diagnostic enregistré'}</div>
          </div>
          <div>
            <div className="form-label">Observation</div>
            <div>{dossier.observation || 'Aucune observation enregistrée'}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
