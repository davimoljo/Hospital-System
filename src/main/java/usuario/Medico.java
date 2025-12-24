package usuario;


public class Medico extends Usuario {
    private String crm;
    private String especialidade;
    private boolean ativo;

    // Configuração de horários
    private int horaInicioExpediente;
    private int horaFimExpediente;

    public Medico(String nome, String cpf, String senha, String email, String crm, String especialidade) {
        super(nome, cpf, senha, email);
        this.crm = crm;
        this.especialidade = especialidade;
        this.ativo = true;
        tipo = TipoUsuario.MEDICO;
    }

    public String getCrm() { return crm; }
    public String getEspecialidade() { return especialidade; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public void configurarHorario(int inicio, int fim) {
        this.horaInicioExpediente = inicio;
        this.horaFimExpediente = fim;
    }

    @Override
    public String getTipoUsuario() {
        return "MEDICO";
    }
}
