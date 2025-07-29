package br.ufrpe.consultorio.servico;

import br.ufrpe.consultorio.entidade.Consulta;
import br.ufrpe.consultorio.repositorio.ConsultaRepositorio;
import br.ufrpe.consultorio.dto.MedicoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient; 
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor 
public class ConsultaService {

    private final ConsultaRepositorio consultaRepositorio;
    private final WebClient professionalServiceWebClient;
    //private final WebClient patientServiceWebClient;

    //agendar uma nova consulta
    @Transactional //garante que a operação seja atômica (ou tudo acontece, ou nada acontece)
    public Consulta agendarConsulta(Consulta consulta) {
        //antes de agendar:
        //verifica se a data e hora da consulta não estão no passado
        if (consulta.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível agendar consultas no passado.");
        }

        //verifica se já existe uma consulta para o médico no mesmo horário
        Optional<Consulta> consultaExistente = consultaRepositorio.findByMedicoIdAndDataHora(
                consulta.getMedicoId(), consulta.getDataHora());

        if (consultaExistente.isPresent()) {
            throw new IllegalArgumentException("O médico já possui uma consulta agendada para este horário.");
        }

        try {
            professionalServiceWebClient.get()
                    .uri("/api/medicos/{id}", consulta.getMedicoId())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> Mono.error(new RuntimeException("Erro ao verificar médico: " + clientResponse.statusCode())))
                    .bodyToMono(MedicoDTO.class)
                    .block(); // .block() para tornar a chamada síncrona para este exemplo
        } catch (Exception e) {
            throw new IllegalArgumentException("Médico com ID " + consulta.getMedicoId() + " não encontrado ou serviço de profissional indisponível.");
        }

        //>>BLOCO COMENTADO TEMPORARIAMENTE ATÉ COMPLETAR MODULO PACIENTE<<
        /*
        try {
             patientServiceWebClient.get()
                     .uri("/api/pacientes/{id}", consulta.getPacienteId())
                     .retrieve()
                     .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                             clientResponse -> Mono.error(new RuntimeException("Erro ao verificar paciente: " + clientResponse.statusCode())))
                     .bodyToMono(Object.class)
                     .block();
        } catch (Exception e) {
            throw new IllegalArgumentException("Paciente com ID " + consulta.getPacienteId() + " não encontrado ou serviço de paciente indisponível.");
        }
        */
        // 

        // Salva a consulta no banco de dados
        consulta.setStatus("AGENDADA"); 
        return consultaRepositorio.save(consulta);
    }

    public Optional<Consulta> buscarConsultaPorId(Long id) {
        return consultaRepositorio.findById(id);
    }

    public List<MedicoDTO> buscarMedicosPorEspecialidade(String especialidade) {
        return professionalServiceWebClient.get()
                .uri("/api/medicos/especialidade/{especialidade}", especialidade)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Erro ao buscar médicos por especialidade: " + clientResponse.statusCode())))
                .bodyToFlux(MedicoDTO.class) // bodyToFlux para uma lista de objetos
                .collectList()
                .block(); // .block() para tornar a chamada síncrona
    }

    public List<MedicoDTO> buscarMedicosPorNome(String nome) {
        return professionalServiceWebClient.get()
                .uri("/api/medicos/nome/{nome}", nome)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Erro ao buscar médicos por nome: " + clientResponse.statusCode())))
                .bodyToFlux(MedicoDTO.class) // bodyToFlux para uma lista de objetos
                .collectList()
                .block(); // .block() para tornar a chamada síncrona
    }


    public List<Consulta> listarTodasAsConsultas() {
        return consultaRepositorio.findAll();
    }

    public List<Consulta> listarConsultasPorMedico(Long medicoId) {
        return consultaRepositorio.findByMedicoId(medicoId);
    }

    public List<Consulta> listarConsultasPorPaciente(Long pacienteId) {
        return consultaRepositorio.findByPacienteId(pacienteId);
    }

    public List<Consulta> listarConsultasPorMedicoEPeriodo(Long medicoId, LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepositorio.findByMedicoIdAndDataHoraBetween(medicoId, inicio, fim);
    }

    public List<Consulta> listarConsultasPorPacienteEPeriodo(Long pacienteId, LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepositorio.findByPacienteIdAndDataHoraBetween(pacienteId, inicio, fim);
    }

    @Transactional
    public Consulta atualizarConsulta(Long id, Consulta consultaAtualizada) {
        return consultaRepositorio.findById(id)
                .map(consulta -> {
                    // Atualiza os campos necessários
                    consulta.setMedicoId(consultaAtualizada.getMedicoId());
                    consulta.setPacienteId(consultaAtualizada.getPacienteId());
                    consulta.setDataHora(consultaAtualizada.getDataHora());
                    consulta.setStatus(consultaAtualizada.getStatus());
                    consulta.setTipoConsulta(consultaAtualizada.getTipoConsulta());
                    // Adicione mais campos para atualização conforme necessário
                    return consultaRepositorio.save(consulta);
                })
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com ID: " + id));
    }

    //cancelar uma consulta
    @Transactional
    public Consulta cancelarConsulta(Long id) {
        return consultaRepositorio.findById(id)
                .map(consulta -> {
                    consulta.setStatus("CANCELADA");
                    return consultaRepositorio.save(consulta);
                })
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com ID: " + id));
    }

    //deletar uma consulta
    @Transactional
    public void deletarConsulta(Long id) {
        if (!consultaRepositorio.existsById(id)) {
            throw new IllegalArgumentException("Consulta não encontrada com ID: " + id);
        }
        consultaRepositorio.deleteById(id);
    }
}
