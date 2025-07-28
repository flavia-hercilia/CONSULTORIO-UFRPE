package br.ufrpe.consultorio.dto;

import jakarta.validation.constraints.NotBlank; 
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username é obrigatório") //garante que o username não nulo ou vazio
    private String username;

    @NotBlank(message = "Password é obrigatório") //o mesmo para a senha
    private String password;
}