package br.ufrpe.consultorio.dto;

import lombok.Data; 

@Data
public class PacienteDTO {
    private Long id;
    private String nome;
    private String cpf;
    private Integer idade;
    private String telefone;
    private String email;
}