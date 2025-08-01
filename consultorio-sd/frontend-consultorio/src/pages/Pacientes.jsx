import { useEffect, useState } from 'react';
import axios from 'axios';

export default function Pacientes() {
  const [pacientes, setPacientes] = useState([]);
  const [nome, setNome] = useState('');
  const [idade, setIdade] = useState('');
  const [cpf, setCpf] = useState(''); 
  const [telefone, setTelefone] = useState(''); 
  const [email, setEmail] = useState('');
  
  const clearForm = () => {
    setNome('');
    setIdade('');
    setCpf('');
    setTelefone('');
    setEmail('');
  };

  const fetchPacientes = async () => {
    try {
      const res = await axios.get('http://localhost:8083/api/pacientes'); 
      setPacientes(res.data);
    } catch (error) {
      console.error('Erro ao buscar pacientes:', error);
    }
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8083/api/pacientes', { 
        nome,
        idade,
        cpf,
        telefone,
        email,
      });
      clearForm(); //limpa o formulário após o sucesso
      fetchPacientes();
    } catch (error) {
      console.error('Erro ao cadastrar paciente:', error.response.data);
      alert(`Erro: ${error.response.data.message || 'Verifique os dados e tente novamente.'}`);
    }
  };

  useEffect(() => { 
    fetchPacientes(); 
  }, []);

  return (
    <div>
      <h2>Gerenciar Pacientes</h2>
      <form onSubmit={handleSubmit}>
        <input value={nome} onChange={e => setNome(e.target.value)} placeholder="Nome" required />
        <input value={idade} onChange={e => setIdade(e.target.value)} placeholder="Idade" type="number" required />
        <input value={cpf} onChange={e => setCpf(e.target.value)} placeholder="CPF" required />
        <input value={telefone} onChange={e => setTelefone(e.target.value)} placeholder="Telefone" required />
        <input value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" required />
        <button type="submit">Cadastrar</button>
      </form>
      <h3>Pacientes Cadastrados</h3>
      <ul>
        {pacientes.length > 0 ? (
          pacientes.map(p => <li key={p.id}>{p.nome} - {p.telefone} (CPF: {p.cpf})</li>)
        ) : (
          <li>Nenhum paciente cadastrado.</li>
        )}
      </ul>
    </div>
  );
}