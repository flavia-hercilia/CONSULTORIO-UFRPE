package consulta;

public class Paciente {
    private int cod;
    private String nome;
    private int idade;

    public Paciente(int cod, String nome, int idade) {
        this.cod = cod;
        this.nome = nome;
        this.idade = idade;
    }

    public int getId() {
        return cod;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String toString() {
        return "Paciente [id=" + cod + ", nome=" + nome + ", idade=" + idade + "]";
    }
}