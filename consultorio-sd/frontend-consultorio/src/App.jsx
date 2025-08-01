import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import Home from './pages/Home';
import Login from './pages/Login';
import Profissionais from './pages/Profissionais'; 
import Pacientes from './pages/Pacientes';      
import Consultas from './pages/Consultas';     


const PrivateRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" />;
};

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/profissionais" element={<PrivateRoute><Profissionais /></PrivateRoute>} />
      <Route path="/pacientes" element={<PrivateRoute><Pacientes /></PrivateRoute>} />
      <Route path="/consultas" element={<PrivateRoute><Consultas /></PrivateRoute>} />
      <Route path="*" element={<h1>Página não encontrada</h1>} />
    </Routes>
  );
}

export default App;
