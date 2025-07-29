package br.ufrpe.consultorio.controlador;

import br.ufrpe.consultorio.entidade.Consulta;
import br.ufrpe.consultorio.servico.ConsultaService;
import br.ufrpe.consultorio.dto.MedicoDTO;
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

    //endpoint para agendar uma nova consulta (POST)
    @PostMapping
    public ResponseEntity<?> agendarConsulta(@RequestBody Consulta consulta) {
        try {
            Consulta novaConsulta = consultaService.agendarConsulta(consulta);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConsulta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao agendar consulta: " + e.getMessage());
        }
    }

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

    //endpoint para listar todas as consultas (GET)
    @GetMapping
    public ResponseEntity<List<Consulta>> listarTodasAsConsultas() {
        List<Consulta> consultas = consultaService.listarTodasAsConsultas();
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