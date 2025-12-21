package usuario;

// MANTENHA O SEU PACKAGE AQUI

public class Secretaria extends Usuario {
    private String matricula;

    public Secretaria(String nome, String cpf, String senha, String email, String matricula) {
        super(nome, cpf, senha, email);
        this.matricula = matricula;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    @Override
    public String getTipoUsuario() {
        return "SECRETARIA";
    }
}