package br.ufrpe.consultorio.entidade; 

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medico")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 15)
    private String crm;

    @Column(nullable = false, length = 50)
    private String especialidade;

    @Column(nullable = false, length = 15)
    private String telefone;

    @Column(nullable = false, length = 100)
    private String email;
}