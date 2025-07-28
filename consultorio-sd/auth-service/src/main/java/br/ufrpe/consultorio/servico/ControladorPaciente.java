package br.ufrpe.consultorio.servico;
//SEM USO POR ENQUANTO
import java.util.ArrayList;
import java.util.List;
import br.ufrpe.consultorio.modelo.Paciente;

public class ControladorPaciente {
    private static ControladorPaciente instance;
    private List<Paciente> pacientes;

    private ControladorPaciente() {
        pacientes = new ArrayList<>();
    }

    public static ControladorPaciente getInstance() {
        if (instance == null) {
            instance = new ControladorPaciente();
        }
        return instance;
    }

    public void adicionarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }
}