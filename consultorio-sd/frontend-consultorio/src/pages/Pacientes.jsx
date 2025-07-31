import { useEffect, useState } from 'react';
import axios from 'axios';

export default function Pacientes() {
  const [pacientes, setPacientes] = useState([]);
  const [nome, setNome] = useState('');
  const [cpf, setCpf] = useState('');

  const fetchPacientes = async () => {
    const res = await axios.get('http://localhost:8081/pacientes');
    setPacientes(res.data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await axios.post('http://localhost:8081/pacientes', { nome, cpf });
    setNome(''); setCpf('');
    fetchPacientes();
  };

  useEffect(() => { fetchPacientes(); }, []);

  return (
    <div>
      <h2>Pacientes</h2>
      <form onSubmit={handleSubmit}>
        <input value={nome} onChange={e => setNome(e.target.value)} placeholder="Nome" />
        <input value={cpf} onChange={e => setCpf(e.target.value)} placeholder="CPF" />
        <button type="submit">Cadastrar</button>
      </form>
      <ul>
        {pacientes.map(p => <li key={p.id}>{p.nome} - {p.cpf}</li>)}
      </ul>
    </div>
  );
}
