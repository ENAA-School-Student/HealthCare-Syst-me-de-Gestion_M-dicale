import { useState, useEffect } from 'react';
import { Save, User } from 'lucide-react';
import toast from 'react-hot-toast';
import { getPatientById, updatePatient } from '../../api/patientApi';
import { useAuth } from '../../context/AuthContext';
import PageTransition from '../../components/common/PageTransition';
import { createRipple } from '../../utils/ripple';

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
    return (
      <div className="loading-container">
        <div className="spinner spinner-lg" />
        <p>Chargement du profil...</p>
      </div>
    );
  }

  if (!profileId) {
    return (
      <PageTransition>
        <div className="card" style={{ padding: 24, textAlign: 'center' }}>
          <p className="text-muted">
            Session expirée. Veuillez vous <a href="/login" style={{ color: 'var(--primary)', fontWeight: 600 }}>reconnecter</a> pour accéder à votre profil.
          </p>
        </div>
      </PageTransition>
    );
  }

  return (
    <PageTransition>
      <div>
        <div className="page-header">
          <div className="page-header-group">
            <h1 className="page-title">Mon Profil</h1>
            <p className="page-subtitle">Mes informations personnelles</p>
          </div>
        </div>
        <div className="card card-hoverable" style={{ maxWidth: 640 }}>
          <div className="profile-header">
            <div className="profile-avatar">
              <User size={28} />
            </div>
            <div className="profile-info">
              <h2>{form.nom || 'Patient'}</h2>
              <p>{form.prenom ? `${form.prenom}` : 'Patient'}</p>
            </div>
          </div>
          <form onSubmit={handleSave} style={{ padding: '0 4px' }}>
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Nom</label>
                <input
                  className="form-input"
                  value={form.nom}
                  onChange={e => setForm(f => ({ ...f, nom: e.target.value }))}
                  required
                  placeholder="Dupont"
                />
              </div>
              <div className="form-group">
                <label className="form-label">Prénom</label>
                <input
                  className="form-input"
                  value={form.prenom}
                  onChange={e => setForm(f => ({ ...f, prenom: e.target.value }))}
                  required
                  placeholder="Jean"
                />
              </div>
            </div>
            <div className="form-group">
              <label className="form-label">Téléphone</label>
              <input
                className="form-input"
                value={form.telephone}
                onChange={e => setForm(f => ({ ...f, telephone: e.target.value }))}
                placeholder="+33 6 12 34 56 78"
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
            <div className="mt-4">
              <button className="btn btn-primary" type="submit" disabled={saving} onClick={(e) => createRipple(e)}>
                <Save size={16} />
                {saving ? 'Enregistrement...' : 'Enregistrer'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </PageTransition>
  );
}
