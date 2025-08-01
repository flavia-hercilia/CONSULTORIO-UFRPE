import { createContext, useContext, useState } from 'react';

//cria o contexto de autenticação
const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null); //pra guardar nome do usuário, role, etc
  const [token, setToken] = useState(null);

  const login = (userData, userToken) => {
    setIsAuthenticated(true);
    setUser(userData);
    setToken(userToken);
    localStorage.setItem('userToken', userToken);
  };

  const logout = () => {
    setIsAuthenticated(false);
    setUser(null);
    setToken(null);
    localStorage.removeItem('userToken');
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);