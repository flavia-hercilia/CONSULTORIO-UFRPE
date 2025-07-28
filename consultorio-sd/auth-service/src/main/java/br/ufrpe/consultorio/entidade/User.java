package br.ufrpe.consultorio.entidade;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.authority.SimpleGrantedAuthority; 
import org.springframework.security.core.userdetails.UserDetails; 
import java.util.Collection; 
import java.util.List; 


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    //retorna as autoridades (roles) concedidas ao usuário
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    //retorna a senha usada para autenticar o usuário
    @Override
    public String getPassword() {
        return this.password;
    }

    //retorna o nome de usuário usado para autenticar o usuário
    @Override
    public String getUsername() {
        return this.username;
    }

    //indica se a conta do usuário não expirou
    //por enquanto os metodos abaixo retornam true 
    //porque não implementamos lógica de expiração/bloqueio/Credenciais expiradas/habilitação ainda
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //indica se a conta do usuário não está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //indica se a senha do usuário não expirou
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //indica se o usuário está habilitado ou desabilitado
    @Override
    public boolean isEnabled() {
        return true;
    }
}