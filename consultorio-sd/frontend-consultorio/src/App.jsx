import { Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Pacientes from './pages/Pacientes';
import Profissionais from './pages/Profissionais';
import Consultas from './pages/Consultas';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/pacientes" element={<Pacientes />} />
      <Route path="/profissionais" element={<Profissionais />} />
      <Route path="/consultas" element={<Consultas />} />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
}

export default App;
