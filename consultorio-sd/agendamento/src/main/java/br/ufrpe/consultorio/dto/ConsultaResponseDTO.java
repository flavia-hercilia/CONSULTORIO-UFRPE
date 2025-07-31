package br.ufrpe.consultorio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaResponseDTO {
    private Long id;
    private Long medicoId;
    private String nomeMedico;
    private Long pacienteId;
    private String nomePaciente; 
    private LocalDateTime dataHora;
    private String tipoConsulta;
    private String status;
}