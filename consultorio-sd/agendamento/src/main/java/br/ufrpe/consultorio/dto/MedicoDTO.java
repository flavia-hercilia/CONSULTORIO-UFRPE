package br.ufrpe.consultorio.dto;

import lombok.Data; 

@Data 
public class MedicoDTO {
    private Long id;
    private String nome;
    private String crm;
    private String especialidade;
    private String telefone;
    private String email;
}
