package br.ufrpe.consultorio.repositorio;

import br.ufrpe.consultorio.entidade.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepositorio extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCpf(String cpf);
    Optional<Paciente> findByEmail(String email);
    List<Paciente> findByNomeContainingIgnoreCase(String nome);
}

