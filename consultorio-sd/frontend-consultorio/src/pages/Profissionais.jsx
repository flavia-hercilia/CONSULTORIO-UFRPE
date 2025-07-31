import { useEffect, useState } from 'react';
import axios from 'axios';

export default function Profissionais() {
  const [dados, setDados] = useState([]);
  const [nome, setNome] = useState('');
  const [especialidade, setEspecialidade] = useState('');

  const fetchDados = async () => {
    const res = await axios.get('http://localhost:8083/profissionais');
    setDados(res.data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await axios.post('http://localhost:8083/profissionais', { nome, especialidade });
    setNome(''); setEspecialidade('');
    fetchDados();
  };

  useEffect(() => { fetchDados(); }, []);

  return (
    <div>
      <h2>Profissionais</h2>
      <form onSubmit={handleSubmit}>
        <input value={nome} onChange={e => setNome(e.target.value)} placeholder="Nome" />
        <input value={especialidade} onChange={e => setEspecialidade(e.target.value)} placeholder="Especialidade" />
        <button type="submit">Cadastrar</button>
      </form>
      <ul>
        {dados.map(p => <li key={p.id}>{p.nome} - {p.especialidade}</li>)}
      </ul>
    </div>
  );
}
