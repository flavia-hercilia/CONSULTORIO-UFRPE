import { useEffect, useState } from 'react';
import axios from 'axios';

// A URL base para a API de Agendamentos
const API_URL_CONSULTAS = 'http://localhost:8084/api/consultas';

export default function Consultas() {
  const [consultas, setConsultas] = useState([]);
  const [medicos, setMedicos] = useState([]);
  const [pacientes, setPacientes] = useState([]);
  const [medicoSelecionado, setMedicoSelecionado] = useState(''); 
  const [pacienteSelecionado, setPacienteSelecionado] = useState(''); 
  const [dataHora, setDataHora] = useState('');
  const [tipoConsulta, setTipoConsulta] = useState('');
  const [status, setStatus] = useState('AGENDADA');

  const clearForm = () => {
    setMedicoSelecionado('');
    setPacienteSelecionado('');
    setDataHora('');
    setTipoConsulta('');
    setStatus('AGENDADA');
  };

  //pega lista de médicos
  const fetchMedicos = async () => {
    try {
      const res = await axios.get(`${API_URL_CONSULTAS}/medicos`); 
      setMedicos(res.data);
    } catch (error) {
      console.error('Erro ao buscar médicos:', error);
    }
  };

  //pega lista de pacientes
  const fetchPacientes = async () => {
    try {
      const res = await axios.get(`${API_URL_CONSULTAS}/pacientes`);
      setPacientes(res.data);
    } catch (error) {
      console.error('Erro ao buscar pacientes:', error);
    }
  };

  //pega lista de consultas
  const fetchConsultas = async () => {
    try {
      const res = await axios.get(API_URL_CONSULTAS);
      setConsultas(res.data);
    } catch (error) {
      console.error('Erro ao buscar consultas:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const medico = medicos.find(m => m.id === Number(medicoSelecionado));
      const paciente = pacientes.find(p => p.id === Number(pacienteSelecionado));

      if (!medico || !paciente) {
        alert('Por favor, selecione um médico e um paciente.');
        return;
      }

      await axios.post(API_URL_CONSULTAS, {
        nomeMedico: medico.nome,
        especialidadeMedico: medico.especialidade,
        nomePaciente: paciente.nome, 
        dataHora,
        tipoConsulta,
        status,
      });
      clearForm();
      fetchConsultas(); 

    } catch (error) {
      console.error('Erro ao agendar consulta:', error.response.data);
      alert(`Erro: ${error.response.data || 'Verifique os dados e tente novamente.'}`);
    }
  };

  useEffect(() => {
    fetchMedicos();
    fetchPacientes();
    fetchConsultas();
  }, []);

  return (
    <div>
      <h2>Agendar Nova Consulta</h2>
      <form onSubmit={handleSubmit}>
        <label htmlFor="medico-select">Médico:</label>
        <select id="medico-select" value={medicoSelecionado} onChange={e => setMedicoSelecionado(e.target.value)} required>
          <option value="">-- Selecione um médico --</option>
          {medicos.length > 0 ? (
            medicos.map(m => (
              <option key={m.id} value={m.id}>{m.nome} ({m.especialidade})</option>
            ))
          ) : (
            <option disabled>Nenhum médico disponível</option>
          )}
        </select>
        <label htmlFor="paciente-select">Paciente:</label>
        <select id="paciente-select" value={pacienteSelecionado} onChange={e => setPacienteSelecionado(e.target.value)} required>
          <option value="">-- Selecione um paciente --</option>
          {pacientes.length > 0 ? (
            pacientes.map(p => (
              <option key={p.id} value={p.id}>{p.nome} (CPF: {p.cpf})</option>
            ))
          ) : (
            <option disabled>Nenhum paciente disponível</option>
          )}
        </select>
        <input type="datetime-local" value={dataHora} onChange={e => setDataHora(e.target.value)} required />
        <input value={tipoConsulta} onChange={e => setTipoConsulta(e.target.value)} placeholder="Tipo de Consulta" required />
        <input value={status} onChange={e => setStatus(e.target.value)} placeholder="Status (AGENDADA)" required />
        <button type="submit">Agendar</button>
      </form>

      <h3>Consultas Agendadas</h3>
      <ul>
        {consultas.length > 0 ? (
          consultas.map(c => (
            <li key={c.id}>
              Médico: {c.nomeMedico} | Paciente: {c.nomePaciente} | Data: {new Date(c.dataHora).toLocaleString()}
            </li>
          ))
        ) : (
          <li>Nenhuma consulta agendada.</li>
        )}
      </ul>
    </div>
  );
}