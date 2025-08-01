import { Link } from 'react-router-dom';

export default function Home() {
  return (
    <div>
      <h1>Consultório UFRPE</h1>
      <nav>
        <ul>
          <li><Link to="/login">Login</Link></li>
          <li><Link to="/pacientes">Pacientes</Link></li>
          <li><Link to="/profissionais">Profissionais</Link></li>
          <li><Link to="/consultas">Consultas</Link></li>
        </ul>
      </nav>
    </div>
  );
}