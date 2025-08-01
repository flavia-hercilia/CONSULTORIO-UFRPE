package br.ufrpe.consultorio.repositorio;

import br.ufrpe.consultorio.entidade.Medico; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
 
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    //Optional pode ter 0 ou 1
    //se tiver mais de 1, o Spring Data JPA vai lançar uma exceção
    Optional<Medico> findByCrm(String crm); 
    Optional<Medico> findByEmail(String email); 
    //lista pode ter 0, 1 ou mais
    List<Medico> findByNome(String nome); 
    List<Medico> findByEspecialidade(String especialidade);
}
