package br.ufrpe.consultorio.entidade;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consulta")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private long pacienteId;

    @Column(nullable = false)
    private long medicoId;

    @Column(nullable = false)
    private LocalDateTime dataHora; 

    @Column(nullable = false)
    private String status; 

    @Column(nullable = false)
    private String tipoConsulta;

    public Consulta(Long medicoId, Long pacienteId, LocalDateTime dataHora, String tipoConsulta, String status) {
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
        this.dataHora = dataHora;
        this.tipoConsulta = tipoConsulta;
        this.status = status;
    }

}
