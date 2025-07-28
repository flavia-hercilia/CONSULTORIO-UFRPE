package br.ufrpe.consultorio.config;

import br.ufrpe.consultorio.repositorio.UserRepository; 
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; 
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.web.SecurityFilterChain; 

@Configuration //indica que é configurações para o Spring
@EnableWebSecurity 
@RequiredArgsConstructor 
public class SecurityConfig {

    private final UserRepository userRepository; //carrega usuários do DB

    // Define o codificador de senhas (BCrypt)
    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //define o serviço de detalhes do usuário
    //o spring security usa para carregar os detalhes do usuário
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //configura a cadeia de filtros de segurança HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) 
            .authorizeHttpRequests(authorize -> authorize
                //permite acesso público aos endpoints de registro e login (sem autenticação)
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                //as outras requisições devem ser autenticadas
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //sem sessão no servidor 
            );
        return http.build();
    }
}