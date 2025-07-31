import { useState } from 'react';
import axios from 'axios';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/login', { username, password });
      alert('Login bem-sucedido');
    } catch (error) {
      alert('Falha no login');
    }
  };

  return (
    <form onSubmit={handleLogin}>
      <input type="text" value={username} onChange={e => setUsername(e.target.value)} placeholder="Usuário" />
      <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Senha" />
      <button type="submit">Entrar</button>
    </form>
  );
}
