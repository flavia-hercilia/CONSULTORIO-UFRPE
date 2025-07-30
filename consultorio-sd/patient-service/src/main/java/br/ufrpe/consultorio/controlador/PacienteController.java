package br.ufrpe.consultorio.controlador;

import br.ufrpe.consultorio.entidade.Paciente;
import br.ufrpe.consultorio.servico.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PacienteController {

    private final PacienteService pacienteService;

    // POST /api/pacientes
    @PostMapping
    public ResponseEntity<Paciente> adicionarPaciente(@Valid @RequestBody Paciente paciente) {
        try {
            Paciente novoPaciente = pacienteService.adicionarPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // GET /api/pacientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.buscarPacientePorId(id);
        return paciente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/pacientes/cpf/{cpf}
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Paciente> buscarPacientePorCpf(@PathVariable String cpf) {
        Optional<Paciente> paciente = pacienteService.buscarPacientePorCpf(cpf);
        return paciente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodosPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodosPacientes();
        return ResponseEntity.ok(pacientes);
    }

    // PUT /api/pacientes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizarPaciente(@PathVariable Long id, @Valid @RequestBody Paciente pacienteAtualizado) {
        try {
            Paciente paciente = pacienteService.atualizarPaciente(id, pacienteAtualizado);
            return ResponseEntity.ok(paciente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // DELETE /api/pacientes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        try {
            pacienteService.deletarPaciente(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}