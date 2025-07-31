import { useEffect, useState } from 'react';
import axios from 'axios';

export default function Profissionais() {
  const [dados, setDados] = useState([]);
  const [nome, setNome] = useState('');
  const [especialidade, setEspecialidade] = useState('');
  const [crm, setCrm] = useState(''); 
  const [telefone, setTelefone] = useState(''); 
  const [email, setEmail] = useState('');
  
  const clearForm = () => {
    setNome('');
    setEspecialidade('');
    setCrm('');
    setTelefone('');
    setEmail('');
  };
  

  const fetchDados = async () => {
     try {
      const res = await axios.get('http://localhost:8082/api/medicos'); 
      setDados(res.data);
    } catch (error) {
      console.error('Erro ao buscar dados:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8082/api/medicos', { 
        nome,
        especialidade,
        crm,
        telefone,
        email,
      });
      clearForm(); //limpa o formulário após o sucesso
      fetchDados(); //atualiza a lista
    } catch (error) {
      console.error('Erro ao cadastrar profissional:', error.response.data);
      alert(`Erro: ${error.response.data.message || 'Verifique os dados e tente novamente.'}`);
    }
  };

  useEffect(() => { fetchDados(); }, []);

  return (
    <div>
      <h2>Gerenciar Profissionais</h2>
      <form onSubmit={handleSubmit}>
        <input value={nome} onChange={e => setNome(e.target.value)} placeholder="Nome" required />
        <input value={especialidade} onChange={e => setEspecialidade(e.target.value)} placeholder="Especialidade" required />
        <input value={crm} onChange={e => setCrm(e.target.value)} placeholder="CRM" required />
        <input value={telefone} onChange={e => setTelefone(e.target.value)} placeholder="Telefone" required />
        <input value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" required />
        <button type="submit">Cadastrar</button>
      </form>
      <h3>Profissionais Cadastrados</h3>
      <ul>
        {dados.length > 0 ? (
          dados.map(p => <li key={p.id}>{p.nome} - {p.especialidade} (CRM: {p.crm})</li>)
        ) : (
          <li>Nenhum profissional cadastrado.</li>
        )}
      </ul>
    </div>
  );
}
