import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function ProtectedRoute({ children, allowedRoles }) {
  const { isAuthenticated, user, loading } = useAuth();

  if (loading) return (
    <div className="loading-container" style={{ height: '100vh' }}>
      <div className="spinner spinner-lg" />
      <p>Vérification de l'authentification...</p>
    </div>
  );

  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (allowedRoles && !allowedRoles.includes(user?.role)) return <Navigate to="/unauthorized" replace />;

  return children;
}
