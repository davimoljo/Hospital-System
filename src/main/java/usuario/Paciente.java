package usuario;

/// MANTENHA O PACKAGE

public class Paciente extends Usuario {
    private String endereco;
    private String convenio;

    public Paciente(String nome, String cpf, String senha, String email, String endereco, String convenio) {
        super(nome, cpf, senha, email);
        this.endereco = endereco;
        this.convenio = convenio;
    }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getConvenio() { return convenio; }
    public void setConvenio(String convenio) { this.convenio = convenio; }

    @Override
    public String getTipoUsuario() {
        return "PACIENTE";
    }

    @Override
    public String toString() {
        return this.nome + " (Convênio: " + this.convenio + ")";
    }
}