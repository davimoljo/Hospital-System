package usuario;


import sistema.Consulta;
import utilitarios.Hora;

import java.util.ArrayList;
import java.util.List;

public class Medico extends Usuario {
    private String crm;
    private Especialidade especialidade;
    private boolean ativo;
    private List<Consulta> consultasMarcadas;

    // Configuração de horários
    private Hora horaInicioExpediente;
    private Hora horaFimExpediente;



    public Medico(String nome, String cpf, String senha, String email, String crm, Especialidade especialidade) {

        super(nome, cpf, senha, email);
        this.crm = crm;
        this.especialidade = especialidade;
        this.ativo = true;
        tipo = TipoUsuario.MEDICO;
        consultasMarcadas = new ArrayList<>();
    }

    public Medico(){}

    public String getCrm() { return crm; }
    public Especialidade getEspecialidade() { return especialidade; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public void configurarHorario(Hora inicio, Hora fim) {
        this.horaInicioExpediente = inicio;
        this.horaFimExpediente = fim;
    }

    public boolean dentroDoExpediente(Hora hora){
        return hora.getHora() >= this.horaInicioExpediente.getHora() && hora.getHora() <= this.horaFimExpediente.getHora();
    }

    public void arquivarConsulta(Consulta consulta){
        consultasMarcadas.add(consulta);
    }

    public List<Consulta> getConsultasMarcadas() {return  consultasMarcadas;}

    @Override
    public String getTipoUsuario() {
        return "MEDICO";
    }


}
