package br.ufrpe.consultorio.servico;

import br.ufrpe.consultorio.entidade.Consulta;
import br.ufrpe.consultorio.repositorio.ConsultaRepositorio;
import br.ufrpe.consultorio.dto.MedicoDTO;
import br.ufrpe.consultorio.dto.PacienteDTO;
import br.ufrpe.consultorio.dto.ConsultaRequestDTO;
import br.ufrpe.consultorio.dto.ConsultaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient; 
import reactor.core.publisher.Mono;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor 
public class ConsultaService {

    private final ConsultaRepositorio consultaRepositorio;
    private final WebClient professionalServiceWebClient;
    private final WebClient patientServiceWebClient;

    //agendar uma consulta a partir de um DTO com nomes/especialidade
    @Transactional
    public Consulta agendarConsultaComDetalhes(ConsultaRequestDTO request) {
        Optional<MedicoDTO> medico = buscarMedicoPorNomeEEspecialidade(request.getNomeMedico(), request.getEspecialidadeMedico());
        if (medico.isEmpty()) {
            throw new IllegalArgumentException("Médico não encontrado com o nome e especialidade fornecidos.");
        }
        Long medicoId = medico.get().getId();

        Optional<PacienteDTO> paciente = buscarPacientePorNome(request.getNomePaciente());
        if (paciente.isEmpty()) {
            throw new IllegalArgumentException("Paciente não encontrado com o nome fornecido.");
        }
        Long pacienteId = paciente.get().getId();

        Consulta novaConsulta = new Consulta();
        novaConsulta.setMedicoId(medicoId);
        novaConsulta.setPacienteId(pacienteId);
        novaConsulta.setDataHora(request.getDataHora());
        novaConsulta.setTipoConsulta(request.getTipoConsulta());
        novaConsulta.setStatus(request.getStatus());

        return agendarConsulta(novaConsulta);
    }

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
                    .block(); 
        } catch (Exception e) {
            throw new IllegalArgumentException("Médico com ID " + consulta.getMedicoId() + " não encontrado ou serviço de profissional indisponível.");
        }

        try {
             patientServiceWebClient.get()
                     .uri("/api/pacientes/{id}", consulta.getPacienteId())
                     .retrieve()
                     .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                             clientResponse -> Mono.error(new RuntimeException("Erro ao verificar paciente: " + clientResponse.statusCode())))
                     .bodyToMono(PacienteDTO.class)
                     .block();
        } catch (Exception e) {
            throw new IllegalArgumentException("Paciente com ID " + consulta.getPacienteId() + " não encontrado ou serviço de paciente indisponível.");
        }

        // Salva a consulta no banco de dados
        consulta.setStatus("AGENDADA"); 
        return consultaRepositorio.save(consulta);
    }

    public Optional<Consulta> buscarConsultaPorId(Long id) {
        return consultaRepositorio.findById(id);
    }

    //buscar um médico por nome e especialidade
    public Optional<MedicoDTO> buscarMedicoPorNomeEEspecialidade(String nome, String especialidade) {
        List<MedicoDTO> medicos = professionalServiceWebClient.get()
                .uri("/api/medicos/nome/{nome}", nome)
                .retrieve()
                .bodyToFlux(MedicoDTO.class)
                .collectList()
                .block();

        return medicos.stream()
                .filter(m -> m.getEspecialidade().equalsIgnoreCase(especialidade))
                .findFirst();
    }

    //buscar um paciente por nome
    public Optional<PacienteDTO> buscarPacientePorNome(String nome) {
        // A chamada WebClient retorna uma lista de PacienteDTO
        List<PacienteDTO> pacientes = patientServiceWebClient.get()
                .uri("/api/pacientes/nome/{nome}", nome)
                .retrieve()
                .bodyToFlux(PacienteDTO.class)
                .collectList()
                .block();

        // Pegamos o primeiro paciente da lista e o retornamos como um Optional
        // Isso resolve o erro de 'Type mismatch'
        return pacientes.stream().findFirst();
    }

    public PacienteDTO buscarPacientePorId(Long pacienteId) {
        return patientServiceWebClient.get()
                .uri("/api/pacientes/{id}", pacienteId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Erro ao buscar paciente: " + clientResponse.statusCode())))
                .bodyToMono(PacienteDTO.class)
                .block();
    }

    public List<MedicoDTO> buscarMedicosPorEspecialidade(String especialidade) {
        return professionalServiceWebClient.get()
                .uri("/api/medicos/especialidade/{especialidade}", especialidade)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Erro ao buscar médicos por especialidade: " + clientResponse.statusCode())))
                .bodyToFlux(MedicoDTO.class) // bodyToFlux para uma lista de objetos
                .collectList()
                .block(); 
    }

    public List<MedicoDTO> buscarMedicosPorNome(String nome) {
        return professionalServiceWebClient.get()
                .uri("/api/medicos/nome/{nome}", nome)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Erro ao buscar médicos por nome: " + clientResponse.statusCode())))
                .bodyToFlux(MedicoDTO.class) // bodyToFlux para uma lista de objetos
                .collectList()
                .block();
    }

// NOVO: Método que lista todas as consultas e resolve os nomes de médico/paciente
    public List<ConsultaResponseDTO> listarTodasAsConsultasComNomes() {
        // 1. Busca todas as consultas do banco de dados local
        List<Consulta> consultas = consultaRepositorio.findAll();

        // 2. Converte cada Consulta em um ConsultaResponseDTO
        return consultas.stream().map(consulta -> {
            String nomeMedico = "Médico não encontrado";
            String nomePaciente = "Paciente não encontrado";

            // Busca o nome do médico
            try {
                MedicoDTO medico = professionalServiceWebClient.get()
                        .uri("/api/medicos/{id}", consulta.getMedicoId())
                        .retrieve()
                        .bodyToMono(MedicoDTO.class)
                        .block();
                if (medico != null) {
                    nomeMedico = medico.getNome();
                }
            } catch (WebClientResponseException.NotFound ex) {
                // Deixa o nome como "Médico não encontrado"
            }

            // Busca o nome do paciente
            try {
                PacienteDTO paciente = patientServiceWebClient.get()
                        .uri("/api/pacientes/{id}", consulta.getPacienteId())
                        .retrieve()
                        .bodyToMono(PacienteDTO.class)
                        .block();
                if (paciente != null) {
                    nomePaciente = paciente.getNome();
                }
            } catch (WebClientResponseException.NotFound ex) {
                // Deixa o nome como "Paciente não encontrado"
            }

            // Cria e retorna o DTO de resposta
            return new ConsultaResponseDTO(
                    consulta.getId(),
                    consulta.getMedicoId(),
                    nomeMedico,
                    consulta.getPacienteId(),
                    nomePaciente,
                    consulta.getDataHora(),
                    consulta.getTipoConsulta(),
                    consulta.getStatus()
            );
        }).collect(Collectors.toList());
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

    public List<MedicoDTO> listarTodosMedicos() {
        return professionalServiceWebClient.get()
                .uri("/api/medicos") 
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("Erro ao listar todos os médicos: " + clientResponse.statusCode())))
                .bodyToFlux(MedicoDTO.class)
                .collectList()
                .block();
    }

    public List<PacienteDTO> listarTodosPacientes() {
        return patientServiceWebClient.get()
                .uri("/api/pacientes") 
                .retrieve()
                .bodyToFlux(PacienteDTO.class)
                .collectList()
                .block();
    }

    @Transactional
    public Consulta atualizarConsulta(Long id, Consulta consultaAtualizada) {
        return consultaRepositorio.findById(id)
                .map(consulta -> {
                    consulta.setMedicoId(consultaAtualizada.getMedicoId());
                    consulta.setPacienteId(consultaAtualizada.getPacienteId());
                    consulta.setDataHora(consultaAtualizada.getDataHora());
                    consulta.setStatus(consultaAtualizada.getStatus());
                    consulta.setTipoConsulta(consultaAtualizada.getTipoConsulta());
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
