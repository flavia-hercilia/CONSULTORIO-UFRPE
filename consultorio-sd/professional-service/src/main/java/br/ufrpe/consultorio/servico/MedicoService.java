package br.ufrpe.consultorio.servico;

import br.ufrpe.consultorio.entidade.Medico;
import br.ufrpe.consultorio.repositorio.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository; // Injeção de dependência do repositório

    // Método para adicionar um novo médico
    public Medico adicionarMedico(Medico medico) {
        // Validações de negócio antes de salvar
        if (medicoRepository.findByCrm(medico.getCrm()).isPresent()) {
            throw new IllegalArgumentException("CRM já cadastrado.");
        }

        if (medicoRepository.findByEmail(medico.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        return medicoRepository.save(medico); 
    }

    //buscar um médico por ID, CRM, nome ou especialidade
    public Optional<Medico> buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id);
    }

    public Optional<Medico> buscarMedicoPorCrm(String crm) {
        return medicoRepository.findByCrm(crm); 
    }   

    public List<Medico> buscarMedicoPorNome(String nome) {
        return medicoRepository.findByNome(nome); 
    }

    public List<Medico> buscarMedicoPorEspecialidade(String especialidade) {
        return medicoRepository.findByEspecialidade(especialidade); 
    }

    //listar todos os médicos
    public List<Medico> listarTodosMedicos() {
        return medicoRepository.findAll();
    }

    //atualizar os dados de um médico
    public Medico atualizarMedico(Long id, Medico medicoAtualizado) {
        return medicoRepository.findById(id)
                .map(medicoExistente -> {
                    medicoExistente.setNome(medicoAtualizado.getNome());
                    medicoExistente.setEspecialidade(medicoAtualizado.getEspecialidade());
                    medicoExistente.setTelefone(medicoAtualizado.getTelefone());
                    medicoExistente.setEmail(medicoAtualizado.getEmail());
                    return medicoRepository.save(medicoExistente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado com ID: " + id));
    }

    //deletar um médico pelo ID
    public void deletarMedico(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new IllegalArgumentException("Médico não encontrado com ID: " + id);
        }
        medicoRepository.deleteById(id);
    }

}