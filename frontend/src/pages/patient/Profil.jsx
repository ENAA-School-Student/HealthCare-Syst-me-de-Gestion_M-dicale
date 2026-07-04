import { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import { Save } from 'lucide-react';
import toast from 'react-hot-toast';
import { useAuth } from '../../context/AuthContext';
import { getPatientById, updatePatient } from '../../api/patientApi';

export default function Profil() {
  const { token } = useAuth();
  const [form, setForm] = useState({ nom: '', prenom: '', telephone: '', dateNaissance: '' });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const patientId = token ? jwtDecode(token).id : null;

  useEffect(() => {
    if (patientId) loadPatient();
  }, [patientId]);

  const loadPatient = async () => {
    try {
      const res = await getPatientById(patientId);
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
      await updatePatient(patientId, form);
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
