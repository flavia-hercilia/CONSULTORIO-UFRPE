package br.ufrpe.consultorio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaRequestDTO {

    @NotBlank(message = "O nome do médico é obrigatório.")
    private String nomeMedico;

    @NotBlank(message = "A especialidade do médico é obrigatória.")
    private String especialidadeMedico;

    @NotBlank(message = "O nome do paciente é obrigatório.")
    private String nomePaciente;

    @NotNull(message = "A data e hora da consulta são obrigatórias.")
    private LocalDateTime dataHora;

    @NotBlank(message = "O tipo da consulta é obrigatório.")
    private String tipoConsulta;

    @NotBlank(message = "O status da consulta é obrigatório.")
    private String status;
}