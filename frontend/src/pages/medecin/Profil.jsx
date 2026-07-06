import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { User, Save } from 'lucide-react';
import { getAllMedecins, getMedecinById, updateMedecin } from '../../api/medecinApi';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

const schema = yup.object({
  nom: yup.string().required('Nom requis'),
  specialite: yup.string().required('Spécialité requise'),
  email: yup.string().email('Email invalide').required('Email requis'),
  telephone: yup.string().required('Téléphone requis'),
});

export default function Profil() {
  const { profileId, user } = useAuth();
  const [medecin, setMedecin] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);

  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm({ resolver: yupResolver(schema) });

  useEffect(() => {
    const fetchMedecin = async () => {
      try {
        if (profileId) {
          const res = await getMedecinById(profileId);
          const found = res.data;
          setMedecin(found);
          reset({ nom: found.nom || '', specialite: found.specialite || '', email: found.email || '', telephone: found.telephone || '' });
          return;
        }
        const res = await getAllMedecins({ page: 0, size: 100 });
        const found = res.data.content.find(m => m.email === user?.email);
        if (found) {
          setMedecin(found);
          reset({ nom: found.nom || '', specialite: found.specialite || '', email: found.email || '', telephone: found.telephone || '' });
        }
      } catch (err) {
        toast.error('Erreur lors du chargement du profil: ' + (err.response?.data || err.message));
      } finally {
        setLoading(false);
      }
    };
    fetchMedecin();
  }, [profileId, user?.email, reset]);

  const onSubmit = async (data) => {
    if (!medecin?.id) { toast.error('Impossible de mettre à jour : profil non chargé'); return; }
    try {
      const res = await updateMedecin(medecin.id, { ...data, telephone: Number(data.telephone) });
      setMedecin(res.data);
      setEditing(false);
      toast.success('Profil mis à jour');
    } catch (err) {
      toast.error(err.response?.data || 'Erreur lors de la mise à jour');
    }
  };

  if (loading) return <div className="loading-container"><div className="spinner" /><p>Chargement…</p></div>;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Mon profil</h1>
          <p className="page-subtitle">Informations personnelles et paramètres</p>
        </div>
        <button className="btn btn-secondary" onClick={() => setEditing(!editing)}>
          <Save size={16} /> {editing ? 'Annuler' : 'Modifier'}
        </button>
      </div>

      <div className="card" style={{ maxWidth: 600 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 24 }}>
          <div style={{ width: 56, height: 56, borderRadius: '50%', background: 'linear-gradient(135deg, var(--accent), #818cf8)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <User size={24} color="white" />
          </div>
          <div>
            <h2 style={{ fontSize: 18, fontWeight: 700 }}>{medecin?.nom || 'Médecin'}</h2>
            <p style={{ fontSize: 13, color: 'var(--accent)', textTransform: 'capitalize' }}>{medecin?.specialite || 'Médecin'}</p>
          </div>
        </div>

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Nom</label>
              <input {...register('nom')} className="form-input" disabled={!editing} />
              {errors.nom && <p className="form-error">{errors.nom.message}</p>}
            </div>
            <div className="form-group">
              <label className="form-label">Spécialité</label>
              <input {...register('specialite')} className="form-input" disabled={!editing} />
              {errors.specialite && <p className="form-error">{errors.specialite.message}</p>}
            </div>
          </div>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Email</label>
              <input {...register('email')} className="form-input" type="email" disabled={!editing} />
              {errors.email && <p className="form-error">{errors.email.message}</p>}
            </div>
            <div className="form-group">
              <label className="form-label">Téléphone</label>
              <input {...register('telephone')} className="form-input" disabled={!editing} />
              {errors.telephone && <p className="form-error">{errors.telephone.message}</p>}
            </div>
          </div>
          {editing && (
            <button className="btn btn-primary w-full" type="submit" disabled={isSubmitting} style={{ marginTop: 8 }}>
              <Save size={16} />
              {isSubmitting ? 'Enregistrement…' : 'Enregistrer les modifications'}
            </button>
          )}
        </form>
      </div>
    </div>
  );
}
