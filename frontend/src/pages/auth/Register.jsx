import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { useNavigate, Link } from 'react-router-dom';
import { register as registerApi } from '../../api/authApi';
import toast from 'react-hot-toast';
import { HeartPulse, UserPlus } from 'lucide-react';

const schema = yup.object({
  username: yup.string().min(2, 'Minimum 2 caractères').required('Nom d\'utilisateur requis'),
  email: yup.string().email('Email invalide').required('Email requis'),
  password: yup.string().min(4, 'Minimum 4 caractères').required('Mot de passe requis'),
  role: yup.string().oneOf(['PATIENT', 'MEDECIN'], 'Rôle invalide').required('Rôle requis'),
});

export default function Register() {
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm({ resolver: yupResolver(schema) });

  const onSubmit = async (data) => {
    try {
      await registerApi(data);
      toast.success('Inscription réussie ! Vous pouvez vous connecter.');
      navigate('/login');
    } catch (err) {
      toast.error(err.response?.data || 'Erreur lors de l\'inscription');
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-logo">
          <div className="auth-logo-icon"><HeartPulse size={28} color="white" /></div>
          <h1 className="auth-title">HealthCare+</h1>
          <p className="auth-subtitle">Créez votre compte médical</p>
        </div>

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="form-group">
            <label className="form-label">Nom d'utilisateur</label>
            <input {...register('username')} className="form-input" placeholder="Jean Dupont" />
            {errors.username && <p className="form-error">{errors.username.message}</p>}
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input {...register('email')} className="form-input" placeholder="votre@email.com" type="email" />
            {errors.email && <p className="form-error">{errors.email.message}</p>}
          </div>
          <div className="form-group">
            <label className="form-label">Mot de passe</label>
            <input {...register('password')} className="form-input" placeholder="••••••••" type="password" />
            {errors.password && <p className="form-error">{errors.password.message}</p>}
          </div>
          <div className="form-group">
            <label className="form-label">Vous êtes</label>
            <select {...register('role')} className="form-select">
              <option value="">Sélectionner...</option>
              <option value="PATIENT">Patient</option>
              <option value="MEDECIN">Médecin</option>
            </select>
            {errors.role && <p className="form-error">{errors.role.message}</p>}
          </div>
          <button className="btn btn-primary w-full btn-lg" type="submit" disabled={isSubmitting} style={{ marginTop: 8 }}>
            <UserPlus size={16} />
            {isSubmitting ? 'Inscription...' : 'S\'inscrire'}
          </button>
        </form>

        <p style={{ textAlign: 'center', marginTop: 20, color: 'var(--text-secondary)', fontSize: 13 }}>
          Déjà un compte ?{' '}
          <Link to="/login" style={{ color: 'var(--accent)', fontWeight: 600 }}>Se connecter</Link>
        </p>
      </div>
    </div>
  );
}
