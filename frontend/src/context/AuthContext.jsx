import { createContext, useContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [profileId, setProfileId] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedToken = localStorage.getItem('hc_token');
    const storedUser = localStorage.getItem('hc_user');
    const storedProfileId = localStorage.getItem('hc_profileId');
    if (storedToken && storedUser) {
      try {
        const decoded = jwtDecode(storedToken);
        if (decoded.exp * 1000 > Date.now()) {
          setToken(storedToken);
          setUser(JSON.parse(storedUser));
          if (storedProfileId) setProfileId(Number(storedProfileId));
          else if (decoded.profileId != null) {
            localStorage.setItem('hc_profileId', String(decoded.profileId));
            setProfileId(decoded.profileId);
          }
        } else {
          localStorage.removeItem('hc_token');
          localStorage.removeItem('hc_user');
          localStorage.removeItem('hc_profileId');
        }
      } catch {
        localStorage.removeItem('hc_token');
        localStorage.removeItem('hc_user');
        localStorage.removeItem('hc_profileId');
      }
    }
    setLoading(false);
  }, []);

  const login = (resData) => {
    const decoded = jwtDecode(resData.token);
    const userData = { id: decoded.sub, email: decoded.sub, role: decoded.role };
    localStorage.setItem('hc_token', resData.token);
    localStorage.setItem('hc_user', JSON.stringify(userData));
    const pid = resData.profileId ?? decoded.profileId ?? null;
    if (pid != null) {
      localStorage.setItem('hc_profileId', String(pid));
      setProfileId(pid);
    }
    setToken(resData.token);
    setUser(userData);
    return userData;
  };

  const logout = () => {
    localStorage.removeItem('hc_token');
    localStorage.removeItem('hc_user');
    localStorage.removeItem('hc_profileId');
    setToken(null);
    setUser(null);
    setProfileId(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, profileId, login, logout, isAuthenticated: !!token, loading }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};
