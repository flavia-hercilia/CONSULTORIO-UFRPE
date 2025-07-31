package br.ufrpe.consultorio.controlador;

import br.ufrpe.consultorio.entidade.Consulta;
import br.ufrpe.consultorio.servico.ConsultaService;
import jakarta.validation.Valid;
import br.ufrpe.consultorio.dto.MedicoDTO;
import br.ufrpe.consultorio.dto.PacienteDTO;
import br.ufrpe.consultorio.dto.ConsultaRequestDTO;
import br.ufrpe.consultorio.dto.ConsultaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController 
@RequestMapping("/api/consultas") 
@RequiredArgsConstructor 
@CrossOrigin(origins = "http://localhost:3000") //permite requisições do frontend
public class ConsultaController {

    private final ConsultaService consultaService;

    //endpoint para buscar uma consulta por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Consulta> buscarConsultaPorId(@PathVariable Long id) {
        return consultaService.buscarConsultaPorId(id)
                .map(ResponseEntity::ok) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    //endpoint para buscar médicos por especialidade
    @GetMapping("/medicos/especialidade/{especialidade}")
    public ResponseEntity<?> buscarMedicosPorEspecialidade(@PathVariable String especialidade) {
        try {
            List<MedicoDTO> medicos = consultaService.buscarMedicosPorEspecialidade(especialidade);
            if (medicos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum médico encontrado para a especialidade: " + especialidade);
            }
            return ResponseEntity.ok(medicos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar médicos por especialidade: " + e.getMessage());
        }
    }

    //endpoint para buscar médicos por nome
    @GetMapping("/medicos/nome/{nome}")
    public ResponseEntity<?> buscarMedicosPorNome(@PathVariable String nome) {
        try {
            List<MedicoDTO> medicos = consultaService.buscarMedicosPorNome(nome);
            if (medicos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum médico encontrado com o nome: " + nome);
            }
            return ResponseEntity.ok(medicos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar médicos por nome: " + e.getMessage());
        }
    }

    // NOVO: Endpoint para buscar paciente por ID (chamando o patient-service)
    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<?> buscarPacientePorId(@PathVariable Long pacienteId) {
        try {
            PacienteDTO paciente = consultaService.buscarPacientePorId(pacienteId);
            return ResponseEntity.ok(paciente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado com ID: " + pacienteId);
        }
    }

    @PostMapping
    public ResponseEntity<?> agendarConsulta(@Valid @RequestBody ConsultaRequestDTO request) {
        try {
            Consulta novaConsulta = consultaService.agendarConsultaComDetalhes(request); 
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConsulta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao agendar consulta: " + e.getMessage());
        }
    }

    //endpoint para listar todas as consultas com os nomes de médico e paciente
    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listarTodasAsConsultas() {
        List<ConsultaResponseDTO> consultas = consultaService.listarTodasAsConsultasComNomes();
        return ResponseEntity.ok(consultas);
    }

    //endpoint para listar consultas por ID do médico (GET)
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Consulta>> listarConsultasPorMedico(@PathVariable Long medicoId) {
        List<Consulta> consultas = consultaService.listarConsultasPorMedico(medicoId);
        return ResponseEntity.ok(consultas);
    }

    //endpoint para listar consultas por ID do paciente (GET)
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Consulta>> listarConsultasPorPaciente(@PathVariable Long pacienteId) {
        List<Consulta> consultas = consultaService.listarConsultasPorPaciente(pacienteId);
        return ResponseEntity.ok(consultas);
    }

    //endpoint para listar consultas de um médico em um período (GET)
    //ex: /api/consultas/medico/1/periodo?inicio=2025-07-29T09:00:00&fim=2025-07-29T18:00:00
    @GetMapping("/medico/{medicoId}/periodo")
    public ResponseEntity<?> listarConsultasPorMedicoEPeriodo(
            @PathVariable Long medicoId,
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDateTime dataInicio = LocalDateTime.parse(inicio);
            LocalDateTime dataFim = LocalDateTime.parse(fim);
            List<Consulta> consultas = consultaService.listarConsultasPorMedicoEPeriodo(medicoId, dataInicio, dataFim);
            return ResponseEntity.ok(consultas);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de data/hora inválido. Use YYYY-MM-DDTHH:MM:SS.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao buscar consultas: " + e.getMessage());
        }
    }

    //endpoint para listar consultas de um paciente em um período (GET)
    //ex: /api/consultas/paciente/1/periodo?inicio=2025-07-29T09:00:00&fim=2025-07-29T18:00:00
    @GetMapping("/paciente/{pacienteId}/periodo")
    public ResponseEntity<?> listarConsultasPorPacienteEPeriodo(
            @PathVariable Long pacienteId,
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDateTime dataInicio = LocalDateTime.parse(inicio);
            LocalDateTime dataFim = LocalDateTime.parse(fim);
            List<Consulta> consultas = consultaService.listarConsultasPorPacienteEPeriodo(pacienteId, dataInicio, dataFim);
            return ResponseEntity.ok(consultas);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de data/hora inválido. Use YYYY-MM-DDTHH:MM:SS.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao buscar consultas: " + e.getMessage());
        }
    }

    //endpoint para listar todos os médicos (chamando o professional-service)
    @GetMapping("/medicos")
    public ResponseEntity<?> listarTodosMedicos() {
        try {
            List<MedicoDTO> medicos = consultaService.listarTodosMedicos(); 
            if (medicos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum médico encontrado.");
            }
            return ResponseEntity.ok(medicos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar todos os médicos: " + e.getMessage());
        }
    }

    //endpoint para listar todos os pacientes (chamando o patient-service)
    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDTO>> listarTodosPacientes() {
        try {
            List<PacienteDTO> pacientes = consultaService.listarTodosPacientes();
            return ResponseEntity.ok(pacientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //endpoint para atualizar uma consulta (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarConsulta(@PathVariable Long id, @RequestBody Consulta consulta) {
        try {
            Consulta consultaAtualizada = consultaService.atualizarConsulta(id, consulta);
            return ResponseEntity.ok(consultaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 404 se não encontrar
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao atualizar consulta: " + e.getMessage());
        }
    }

    //endpoint para cancelar uma consulta (PUT ou PATCH, aqui PUT para simplicidade)
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarConsulta(@PathVariable Long id) {
        try {
            Consulta consultaCancelada = consultaService.cancelarConsulta(id);
            return ResponseEntity.ok(consultaCancelada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao cancelar consulta: " + e.getMessage());
        }
    }

    //endpoint para deletar uma consulta (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id) {
        try {
            consultaService.deletarConsulta(id);
            return ResponseEntity.noContent().build(); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }
}