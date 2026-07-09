import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { login as loginApi } from '../../api/authApi';
import { createRipple } from '../../utils/ripple';
import toast from 'react-hot-toast';
import {
  HeartPulse, LogIn, Eye, EyeOff, Shield, Stethoscope, CalendarCheck, Lock
} from 'lucide-react';

const schema = yup.object({
  email: yup.string().email('Email invalide').required('Email requis'),
  password: yup.string().min(4, 'Minimum 4 caractères').required('Mot de passe requis'),
});

const ROLE_REDIRECT = { ADMIN: '/admin/dashboard', MEDECIN: '/medecin/dashboard', PATIENT: '/patient/dashboard' };

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm({ resolver: yupResolver(schema) });

  const onSubmit = async (data) => {
    try {
      const res = await loginApi(data);
      const user = login(res.data);
      toast.success('Connexion réussie !');
      navigate(ROLE_REDIRECT[user.role] || '/');
    } catch (err) {
      toast.error(err.response?.data || 'Email ou mot de passe incorrect');
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-split">
        <div className="auth-brand">
          <div className="floating-icons">
            <HeartPulse size={48} className="floating-icon" />
            <Stethoscope size={36} className="floating-icon" />
            <Shield size={40} className="floating-icon" />
            <CalendarCheck size={32} className="floating-icon" />
            <Lock size={28} className="floating-icon" />
          </div>
          <div className="auth-brand-content">
            <div className="auth-brand-icon">
              <HeartPulse size={42} color="white" />
            </div>
            <h1>Health<span>Care+</span></h1>
            <p>Plateforme de gestion médicale intelligente conçue pour les professionnels de santé et leurs patients.</p>
                            <div className="auth-features">
              <div className="auth-feature">
                <div className="auth-feature-icon">
                  <Shield size={18} />
                </div>
                <div className="auth-feature-text">
                  <strong>Gestion sécurisée</strong>
                  <span>Données médicales protégées et conformes aux normes</span>
                </div>
              </div>
              <div className="auth-feature">
                <div className="auth-feature-icon">
                  <CalendarCheck size={18} />
                </div>
                <div className="auth-feature-text">
                  <strong>Rendez-vous simplifiés</strong>
                  <span>Planification et suivi en temps réel</span>
                </div>
              </div>
              <div className="auth-feature">
                <div className="auth-feature-icon">
                  <Stethoscope size={18} />
                </div>
                <div className="auth-feature-text">
                  <strong>Dossiers médicaux</strong>
                  <span>Accès rapide à l'historique complet des patients</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="auth-form-side">
          <div className="auth-card">
            <div className="auth-card-inner">
              <div className="auth-logo">
                <div className="auth-logo-icon">
                  <HeartPulse size={26} color="white" />
                </div>
                <h1 className="auth-title">HealthCare+</h1>
                <p className="auth-subtitle">Connectez-vous à votre espace médical</p>
              </div>

              <form className="auth-form" onSubmit={handleSubmit(onSubmit)}>
                <div className="form-group">
                  <label className="form-label">Email</label>
                  <input
                    {...register('email')}
                    className={`form-input ${errors.email ? 'input-error' : ''}`}
                    placeholder="votre@email.com"
                    type="email"
                    autoComplete="email"
                  />
                  {errors.email && <p className="form-error">{errors.email.message}</p>}
                </div>
                <div className="form-group">
                  <label className="form-label">Mot de passe</label>
                  <div className="password-wrapper">
                    <input
                      {...register('password')}
                      className={`form-input ${errors.password ? 'input-error' : ''}`}
                      placeholder="••••••••"
                      type={showPassword ? 'text' : 'password'}
                      autoComplete="current-password"
                    />
                    <button
                      type="button"
                      className="password-toggle"
                      onClick={() => setShowPassword(!showPassword)}
                      tabIndex={-1}
                    >
                      {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                    </button>
                  </div>
                  {errors.password && <p className="form-error">{errors.password.message}</p>}
                </div>
                <button
                  className={`btn btn-primary w-full btn-lg ${isSubmitting ? 'btn-loading' : ''}`}
                  type="submit"
                  disabled={isSubmitting}
                  onClick={(e) => { if (!isSubmitting) createRipple(e); }}
                  style={{ marginTop: 8 }}
                >
                  <LogIn size={16} />
                  {isSubmitting ? 'Connexion...' : 'Se connecter'}
                </button>
              </form>

              <p className="auth-link">
                Pas encore de compte ?{' '}
                <Link to="/register">S'inscrire</Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
