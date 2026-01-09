package usuario;

import sistema.Consulta;
import sistema.Hospital;
import java.util.ArrayList;
import java.util.List;

import java.time.*;

public class Medico extends Usuario {
    private String crm;
    private Especialidade especialidade;
    private boolean ativo;
    private List<Consulta> consultasMarcadas;
    private LocalTime horaInicioExpediente;
    private LocalTime horaFimExpediente;

    public Medico(String nome, String cpf, String senha, String email, String crm, Especialidade especialidade,
            LocalTime horaInicioExpediente, LocalTime horaFimExpediente) {

        super(nome, cpf, senha, email);
        this.crm = crm;
        this.especialidade = especialidade;
        this.ativo = true;
        tipo = TipoUsuario.MEDICO;
        consultasMarcadas = new ArrayList<>();
        this.horaInicioExpediente = horaInicioExpediente;
        this.horaFimExpediente = horaFimExpediente;
    }

    public Medico() {
    }

    public String getCrm() {
        return crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void configurarHorario(LocalTime inicio, LocalTime fim) {
        this.horaInicioExpediente = inicio;
        this.horaFimExpediente = fim;
    }

    public boolean dentroDoExpediente(LocalTime hora) {
        return hora.getHour() >= this.horaInicioExpediente.getHour()
                && hora.getHour() <= this.horaFimExpediente.getHour();
    }

    public void arquivarConsulta(Consulta consulta) {
        consultasMarcadas.add(consulta);
    }

    public void removerConsulta(Consulta consulta) {
        consultasMarcadas.remove(consulta);
    }

    public List<Consulta> getConsultasMarcadas() {
        return consultasMarcadas;
    }

    @Override
    public String getTipoUsuario() {
        return "MEDICO";
    }

    public boolean isDisponivel(LocalTime hora, LocalDate data) {
        for (Consulta c : consultasMarcadas) {
            if (c.getHora().equals(hora) && c.getMarcacao().equals(data)) {
                return false;
            }
        }
        return isAtivo() && dentroDoExpediente(hora);
    }

    public LocalTime getHoraInicioExpediente() {
        return horaInicioExpediente;
    }

    public LocalTime getHoraFimExpediente() {
        return horaFimExpediente;
    }

    @Override
    public String toString() {
        return "Nome: " + getNome() + " | Especialidade: " + getEspecialidade();
    }

}
