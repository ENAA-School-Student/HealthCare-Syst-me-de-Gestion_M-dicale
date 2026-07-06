import { useState, useEffect } from 'react';
import { Save } from 'lucide-react';
import toast from 'react-hot-toast';
import { getPatientById, updatePatient } from '../../api/patientApi';
import { useAuth } from '../../context/AuthContext';

export default function Profil() {
  const { profileId } = useAuth();
  const [form, setForm] = useState({ nom: '', prenom: '', telephone: '', dateNaissance: '' });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (profileId) loadPatient();
    else setLoading(false);
  }, [profileId]);

  const loadPatient = async () => {
    try {
      const res = await getPatientById(profileId);
      const p = res.data;
      setForm({
        nom: p.nom || '',
        prenom: p.prenom || '',
        telephone: p.telephone || '',
        dateNaissance: p.dateNaissance ? p.dateNaissance.split('T')[0] : '',
      });
    } catch {
      toast.error('Erreur lors du chargement du profil');
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await updatePatient(profileId, {
        ...form,
        telephone: Number(form.telephone),
      });
      toast.success('Profil mis à jour avec succès');
    } catch (err) {
      toast.error(err.response?.data || 'Erreur lors de la mise à jour');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <div className="loading-container"><div className="spinner" /></div>;
  }

  if (!profileId) {
    return (
      <div className="card" style={{ padding: 24, textAlign: 'center' }}>
        <p>Session expirée. Veuillez vous <a href="/login">reconnecter</a> pour accéder à votre profil.</p>
      </div>
    );
  }

  return (
    <div>
      <div className="card">
        <div className="card-header">
          <h3 className="card-title">Mon Profil</h3>
        </div>
        <form onSubmit={handleSave} style={{ padding: 16, display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="form-group">
            <label className="form-label">Nom</label>
            <input
              className="form-input"
              value={form.nom}
              onChange={e => setForm(f => ({ ...f, nom: e.target.value }))}
              required
            />
          </div>
          <div className="form-group">
            <label className="form-label">Prénom</label>
            <input
              className="form-input"
              value={form.prenom}
              onChange={e => setForm(f => ({ ...f, prenom: e.target.value }))}
              required
            />
          </div>
          <div className="form-group">
            <label className="form-label">Téléphone</label>
            <input
              className="form-input"
              value={form.telephone}
              onChange={e => setForm(f => ({ ...f, telephone: e.target.value }))}
            />
          </div>
          <div className="form-group">
            <label className="form-label">Date de naissance</label>
            <input
              className="form-input"
              type="date"
              value={form.dateNaissance}
              onChange={e => setForm(f => ({ ...f, dateNaissance: e.target.value }))}
            />
          </div>
          <div>
            <button className="btn btn-primary" type="submit" disabled={saving}>
              <Save size={16} />
              {saving ? 'Enregistrement...' : 'Enregistrer'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
