package usuario;

import sistema.Consulta;
import sistema.Hospital;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.time.*;

public class Medico extends Usuario {
    private String crm;
    private Especialidade especialidade;
    private boolean ativo;
    private List<Consulta> consultasMarcadas;
    private int duracaoDasConsultas;

    public static class HorarioExpediente {
        LocalTime inicio;
        LocalTime fim;

        public HorarioExpediente() {
        }

        public HorarioExpediente(LocalTime inicio, LocalTime fim) {
            this.inicio = inicio;
            this.fim = fim;
        }

        public LocalTime getInicio() {
            return inicio;
        }

        public void setInicio(LocalTime inicio) {
            this.inicio = inicio;
        }

        public LocalTime getFim() {
            return fim;
        }

        public void setFim(LocalTime fim) {
            this.fim = fim;
        }
    }

    private Map<DayOfWeek, HorarioExpediente> expediente;

    public Medico(String nome, String cpf, String senha, String email, String crm, Especialidade especialidade,
            Map<DayOfWeek, HorarioExpediente> expediente, int duracaoDasConsultas) {

        super(nome, cpf, senha, email);
        this.crm = crm;
        this.especialidade = especialidade;
        this.ativo = true;
        tipo = TipoUsuario.MEDICO;
        consultasMarcadas = new ArrayList<>();
        this.expediente = expediente;
        this.duracaoDasConsultas = duracaoDasConsultas;
    }

    public Medico() {
        super();
        this.expediente = new HashMap<>();
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

    public void configurarHorario(LocalTime inicio, LocalTime fim, DayOfWeek dia) {

        expediente.get(dia).inicio = inicio;
        expediente.get(dia).fim = fim;
    }

    public boolean dentroDoExpediente(LocalTime hora, DayOfWeek dia) {

        if (!expediente.containsKey(dia)) {
            return false;
        }
        HorarioExpediente exp = expediente.get(dia);

        boolean horarioValido = !hora.isBefore(exp.inicio) && hora.isBefore(exp.fim);

        return horarioValido;
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
        return isAtivo() && dentroDoExpediente(hora, data.getDayOfWeek());
    }

    public LocalTime getHoraInicioExpediente(DayOfWeek dia) {
        if (expediente.containsKey(dia)) {
            return expediente.get(dia).inicio;
        }
        return null;
    }

    public LocalTime getHoraFimExpediente(DayOfWeek dia) {
        if (expediente.containsKey(dia)) {
            return expediente.get(dia).fim;
        }
        return null;
    }

    public LocalTime getHoraInicioExpediente(LocalDate data) {
        return getHoraInicioExpediente(data.getDayOfWeek());
    }

    public LocalTime getHoraFimExpediente(LocalDate data) {
        return getHoraFimExpediente(data.getDayOfWeek());
    }

    @Override
    public String toString() {
        return "Nome: " + getNome() + " | Especialidade: " + getEspecialidade();
    }

    public HashMap<DayOfWeek, HorarioExpediente> getExpediente() {
        return new HashMap<>(expediente);
    }

    public void setExpediente(Map<DayOfWeek, HorarioExpediente> novoExpediente) {
        this.expediente = novoExpediente;
    }

    public int getDuracaoDasConsultas() {
        return duracaoDasConsultas;
    }

    public void setDuracaoDasConsultas(int d) {
        duracaoDasConsultas = d;
    }

    public boolean isHorarioLivre(LocalDate data, LocalTime hora) {
        if (!dentroDoExpediente(hora, data.getDayOfWeek()))
            return false;
        for (Consulta c : consultasMarcadas) {
            if (c.getMarcacao().equals(data) && c.getHora().equals(hora))
                return false;
        }
        return true;
    }

}
