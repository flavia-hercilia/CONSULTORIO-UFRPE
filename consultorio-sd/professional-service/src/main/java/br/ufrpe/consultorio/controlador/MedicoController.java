package br.ufrpe.consultorio.controlador;

import br.ufrpe.consultorio.entidade.Medico;
import br.ufrpe.consultorio.servico.MedicoService; 
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
import java.util.List;
import java.util.Optional;

@RestController 
@RequestMapping("/api/medicos") //define a URL base para todos os endpoints neste controlador
@RequiredArgsConstructor 
public class MedicoController {

    private final MedicoService medicoService; 

    //endpoint para adicionar um novo médico
    //URL: POST /api/medicos
    @PostMapping
    public ResponseEntity<Medico> adicionarMedico(@Valid @RequestBody Medico medico) {
        try {
            Medico novoMedico = medicoService.adicionarMedico(medico);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoMedico);
        } catch (IllegalArgumentException e) {
            //se o CRM ou Email já existirem, retorna 400 Bad Request com a mensagem de erro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
        }
    }

    //endpoint para buscar médico por ID
    //URL: GET /api/medicos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarMedicoPorId(@PathVariable Long id) {
        Optional<Medico> medico = medicoService.buscarMedicoPorId(id);
        return medico.map(ResponseEntity::ok) 
                .orElseGet(() -> ResponseEntity.notFound().build()); 
    }

    //endpoint para buscar médico por CRM
    //URL: GET /api/medicos/crm/{crm}
    @GetMapping("/crm/{crm}")
    public ResponseEntity<Medico> buscarMedicoPorCrm(@PathVariable String crm) {
        Optional<Medico> medico = medicoService.buscarMedicoPorCrm(crm);
        return medico.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //endpoint para buscar médico por nome
    //URL: GET /api/medicos/nome/{nome}
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Medico>> buscarMedicoPorNome(@PathVariable String nome) {
        List<Medico> medicos = medicoService.buscarMedicoPorNome(nome);
        if (medicos.isEmpty()) {
            return ResponseEntity.notFound().build(); 
        }
        return ResponseEntity.ok(medicos); 
    }

    //endpoint para buscar médico por especialidade
    //URL: GET /api/medicos/especialidade/{especialidade}
    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<Medico>> buscarMedicoPorEspecialidade(@PathVariable String especialidade) {
        List<Medico> medicos = medicoService.buscarMedicoPorEspecialidade(especialidade);
        if (medicos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicos);
    }

    //endpoint para listar todos os médicos
    //URL: GET /api/medicos
    @GetMapping
    public ResponseEntity<List<Medico>> listarTodosMedicos() {
        List<Medico> medicos = medicoService.listarTodosMedicos();
        return ResponseEntity.ok(medicos); 
    }

    //endpoint para atualizar um médico por ID
    //URL: PUT /api/medicos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Medico> atualizarMedico(@PathVariable Long id, @Valid @RequestBody Medico medicoAtualizado) {
        try {
            Medico medico = medicoService.atualizarMedico(id, medicoAtualizado);
            return ResponseEntity.ok(medico); 
        } catch (IllegalArgumentException e) {
            //se o médico não encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        }
    }

    //endpoint para deletar um médico por ID
    //URL: DELETE /api/medicos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMedico(@PathVariable Long id) {
        try {
            medicoService.deletarMedico(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            //se o médico não for encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }
}
