package br.ufrpe.consultorio.servico;
//SEM USO POR ENQUANTO
import java.util.ArrayList;
import java.util.List;
import br.ufrpe.consultorio.modelo.Consultadados;

public class ControladorConsulta {
    private static ControladorConsulta instance;
    private List<Consultadados> consultas;

    private ControladorConsulta() {
        consultas = new ArrayList<>();
    }

    public static ControladorConsulta getInstance() {
        if (instance == null) {
            instance = new ControladorConsulta();
        }
        return instance;
    }

    public void agendarConsulta(Consultadados consulta) {
        consultas.add(consulta);
    }

    public List<Consultadados> getConsultas() {
        return consultas;
    }
}
