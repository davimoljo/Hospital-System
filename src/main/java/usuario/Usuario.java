package usuario;

public abstract class Usuario {
    protected String nome;
    protected String cpf;
    protected String senha;
    protected String email;
    protected String telefone;
    protected TipoUsuario tipo;

    public Usuario(String nome, String cpf, String senha, String email) {
        validarDados(nome, cpf, senha);
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.email = email;
    }

    private void validarDados(String nome, String cpf, String senha) {
        if (nome == null || nome.trim().length() <= 1) {
            throw new IllegalArgumentException("Erro: O nome deve ter mais de 1 caractere.");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("Erro: CPF é obrigatório.");
        }
        if (senha == null || senha.length() < 4) {
            throw new IllegalArgumentException("Erro: A senha deve ter pelo menos 4 caracteres.");
        }
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    // Método abstrato
    public abstract String getTipoUsuario();

    public TipoUsuario getTipo() {return this.tipo;}
}
