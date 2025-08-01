package br.ufrpe.consultorio.repositorio;

import br.ufrpe.consultorio.entidade.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepositorio extends JpaRepository<Consulta, Long> {

    List<Consulta> findByMedicoId(Long medicoId);
    List<Consulta> findByPacienteId(Long pacienteId);
    List<Consulta> findByMedicoIdAndDataHoraBetween(Long medicoId, LocalDateTime start, LocalDateTime end);
    List<Consulta> findByPacienteIdAndDataHoraBetween(Long pacienteId, LocalDateTime start, LocalDateTime end);
    Optional<Consulta> findByMedicoIdAndDataHora(Long medicoId, LocalDateTime dataHora);
    
}
