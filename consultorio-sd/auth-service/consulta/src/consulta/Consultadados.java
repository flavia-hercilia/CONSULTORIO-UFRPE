package consulta;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consultadados {
    private Paciente paciente;
    private Medico medico;
    private LocalDate data;
    private LocalTime hora;

    public Consultadados(Paciente paciente, Medico medico, LocalDate data, LocalTime hora) {
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.hora = hora;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHora() {
        return hora;
    }

    @Override
    public String toString() {
        return "Consulta [paciente=" + paciente.getNome() + ", medico=" + medico.getNome() + ", data=" + data + ", hora=" + hora + "]";
    }
}