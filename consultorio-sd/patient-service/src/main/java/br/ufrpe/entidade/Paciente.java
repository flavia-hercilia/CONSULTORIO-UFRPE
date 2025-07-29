package br.ufrpe.consultorio.entidade;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @CPF
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, length = 15)
    private String telefone;

    @Email
    @Column(nullable = false, length = 100)
    private String email;
}
