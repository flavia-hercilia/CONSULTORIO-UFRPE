package repository;

import Entrada.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UsuarioRepository {
    private List<Usuario> usuarios;

    public UsuarioRepository() {
        this.usuarios = new ArrayList<>();

        usuarios.add(new Usuario("Rodrizezox", criptografarSenha("Palmeirasnaotemmundial")));
        usuarios.add(new Usuario("Viniciuscastelino", criptografarSenha("#carros2")));
        usuarios.add(new Usuario("123", criptografarSenha("123")));
    }

    public boolean validarLogin(String nome, String senha) {
        String senhaCriptografada = criptografarSenha(senha);
        return usuarios.stream()
                .anyMatch(u -> u.getNome().equals(nome) && u.getSenha().equals(senhaCriptografada));
    }

    public void adicionarUsuario(Usuario usuario) {
        usuario.setSenha(criptografarSenha(usuario.getSenha()));
        usuarios.add(usuario);
    }

    private String criptografarSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar a senha", e);
        }
    }
}

