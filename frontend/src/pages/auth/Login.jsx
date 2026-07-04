import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { login as loginApi } from '../../api/authApi';
import toast from 'react-hot-toast';
import { HeartPulse, LogIn } from 'lucide-react';

const schema = yup.object({
  email: yup.string().email('Email invalide').required('Email requis'),
  password: yup.string().min(4, 'Minimum 4 caractères').required('Mot de passe requis'),
});

const ROLE_REDIRECT = { ADMIN: '/admin/dashboard', MEDECIN: '/medecin/dashboard', PATIENT: '/patient/dashboard' };

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm({ resolver: yupResolver(schema) });

  const onSubmit = async (data) => {
    try {
      const res = await loginApi(data);
      const user = login(res.data.token);
      toast.success('Connexion réussie !');
      navigate(ROLE_REDIRECT[user.role] || '/');
    } catch (err) {
      toast.error(err.response?.data || 'Email ou mot de passe incorrect');
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-logo">
          <div className="auth-logo-icon"><HeartPulse size={28} color="white" /></div>
          <h1 className="auth-title">HealthCare+</h1>
          <p className="auth-subtitle">Connectez-vous à votre espace médical</p>
        </div>

        <form onSubmit={handleSubmit(onSubmit)}>
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
          <button className="btn btn-primary w-full btn-lg" type="submit" disabled={isSubmitting} style={{ marginTop: 8 }}>
            <LogIn size={16} />
            {isSubmitting ? 'Connexion...' : 'Se connecter'}
          </button>
        </form>

        <p style={{ textAlign: 'center', marginTop: 20, color: 'var(--text-secondary)', fontSize: 13 }}>
          Pas encore de compte ?{' '}
          <Link to="/register" style={{ color: 'var(--accent)', fontWeight: 600 }}>S'inscrire</Link>
        </p>
      </div>
    </div>
  );
}
