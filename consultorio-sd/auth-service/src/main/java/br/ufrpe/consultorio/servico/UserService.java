package br.ufrpe.consultorio.servico;

import br.ufrpe.consultorio.entidade.User; 
import br.ufrpe.consultorio.repositorio.UserRepository; 
import lombok.RequiredArgsConstructor; //Lombok para construtor com argumentos necessários
import org.springframework.stereotype.Service; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.context.SecurityContextHolder; 
import java.util.Optional; 

@Service //indica que é um serviço no Spring
@RequiredArgsConstructor 
public class UserService {

    private final UserRepository userRepository; 
    private final PasswordEncoder passwordEncoder; 
    private final AuthenticationManager authenticationManager; 

    //método para registrar um novo usuário
    public User registerUser(String username, String password) {
        //verifica se o username ja existe
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username já existe!");
        }

        //criptografia de senha
        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);

        //salva no banco de dados
        return userRepository.save(newUser);
    }

    //método para autenticar um usuário
    public String authenticateUser(String username, String password) {
        //cria token de autenticação com username e senha
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //se autenticaçao bem sucedida, define o usuario como autenticado no contexto de segurança
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "Autenticação bem-sucedida para o usuário: " + username;
    }

    //método para buscar um usuário por username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}