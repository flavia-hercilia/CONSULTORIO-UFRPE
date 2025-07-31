import { useEffect, useState } from 'react';
import axios from 'axios';

export default function Consultas() {
  const [consultas, setConsultas] = useState([]);
  const [descricao, setDescricao] = useState('');
  const [data, setData] = useState('');

  const fetchConsultas = async () => {
    const res = await axios.get('http://localhost:8082/consultas');
    setConsultas(res.data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await axios.post('http://localhost:8082/consultas', { descricao, data });
    setDescricao(''); setData('');
    fetchConsultas();
  };

  useEffect(() => { fetchConsultas(); }, []);

  return (
    <div>
      <h2>Consultas</h2>
      <form onSubmit={handleSubmit}>
        <input value={descricao} onChange={e => setDescricao(e.target.value)} placeholder="Descrição" />
        <input type="datetime-local" value={data} onChange={e => setData(e.target.value)} />
        <button type="submit">Agendar</button>
      </form>
      <ul>
        {consultas.map(c => <li key={c.id}>{c.descricao} - {c.data}</li>)}
      </ul>
    </div>
  );
}

