package consulta;

public class Medico {

    private int cod;
    private String nomeDoMedico;
    private String especialidade;

    public Medico(int cod, String nomeDoMedico, String especialidade) {
        this.cod = cod;
        this.nomeDoMedico = nomeDoMedico;
        this.especialidade = especialidade;
    }

    public int getId() {
        return cod;
    }

    public String getNome() {
        return nomeDoMedico;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public String toString() {
        return "Medico [Cod=" + cod + ", nome=" + nomeDoMedico + ", especialidade=" + especialidade + "]";
    }
}