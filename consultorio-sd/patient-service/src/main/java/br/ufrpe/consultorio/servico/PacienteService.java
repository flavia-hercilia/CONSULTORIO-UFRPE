package br.ufrpe.consultorio.servico;

import br.ufrpe.consultorio.entidade.Paciente;
import br.ufrpe.consultorio.repositorio.PacienteRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepositorio pacienteRepositorio;

    public Paciente adicionarPaciente(Paciente paciente) {
        if (pacienteRepositorio.findByCpf(paciente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        if (pacienteRepositorio.findByEmail(paciente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        return pacienteRepositorio.save(paciente);
    }

    public Optional<Paciente> buscarPacientePorId(Long id) {
        return pacienteRepositorio.findById(id);
    }

    public Optional<Paciente> buscarPacientePorCpf(String cpf) {
        return pacienteRepositorio.findByCpf(cpf);
    }

    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepositorio.findByNomeContainingIgnoreCase(nome);
    }

    public List<Paciente> listarTodosPacientes() {
        return pacienteRepositorio.findAll();
    }

    public Paciente atualizarPaciente(Long id, Paciente pacienteAtualizado) {
    pacienteRepositorio.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
    pacienteAtualizado.setId(id);
    return pacienteRepositorio.save(pacienteAtualizado);

    }

    public void deletarPaciente(Long id) {
        Paciente existente = pacienteRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado."));
        pacienteRepositorio.delete(existente);
    }
}
