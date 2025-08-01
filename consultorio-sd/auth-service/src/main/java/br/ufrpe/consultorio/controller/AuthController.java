package br.ufrpe.consultorio.controller;

import br.ufrpe.consultorio.dto.RegisterRequest;
import br.ufrpe.consultorio.dto.LoginRequest; 
import br.ufrpe.consultorio.entidade.User; 
import br.ufrpe.consultorio.servico.UserService; 
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController; //marca como controlador REST

@RestController 
@RequestMapping("/api/auth") //Define a URL base para todos os endpoints neste controlador
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") //permite requisições do frontend
public class AuthController {

    private final UserService userService; 

    //endpoint para registro de novo usuário
    //URL: POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            //chama o serviço para registrar o usuário
            User registeredUser = userService.registerUser(request.getUsername(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário " + registeredUser.getUsername() + " registrado com sucesso!");
        } catch (IllegalArgumentException e) {
            //caso user já existir, retorna um erro 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            //outras exceções inesperadas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar usuário.");
        }
    }

    //endpoint para login de usuário existente
    //URL: POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest request) {
        try {
            //chama serviço para autenticar o usuário
            String responseMessage = userService.authenticateUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
        }
    }
}
