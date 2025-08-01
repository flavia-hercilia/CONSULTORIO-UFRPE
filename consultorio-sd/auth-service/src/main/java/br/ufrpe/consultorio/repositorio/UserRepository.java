package br.ufrpe.consultorio.repositorio;

import br.ufrpe.consultorio.entidade.User; //importa entiddade User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; //para lidar com resultados que podem ou não existir

@Repository //indica que essa interface é um repositório Spring Data
public interface UserRepository extends JpaRepository<User, Long> {
    //interface do springdata que herda metodos CRUD 
    //buscar um usuário pelo username
    Optional<User> findByUsername(String username);
}
