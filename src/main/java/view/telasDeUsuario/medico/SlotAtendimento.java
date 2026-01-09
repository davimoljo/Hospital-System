package view.telasDeUsuario.medico;

import sistema.Consulta;
import usuario.userDB.RepositorioDeUsuario;
import java.time.*;

public class SlotAtendimento {

    private LocalTime inicio;
    private LocalTime fim;
    private Consulta consulta; // null = slot livre

    public SlotAtendimento(LocalTime inicio, LocalTime fim) {
        if (inicio.compareTo(fim) >= 0)
            throw new IllegalArgumentException("Horário inválido");

        this.inicio = inicio;
        this.fim = fim;
        this.consulta = null;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public boolean estaLivre() {
        return consulta == null;
    }

    public boolean estaOcupado() {
        return consulta != null;
    }

    public void marcarConsulta(Consulta consulta) {
        if (!estaLivre())
            throw new IllegalStateException("Slot já ocupado");

        this.consulta = consulta;
    }

    public void cancelarConsulta() {
        this.consulta = null;
    }

    @Override
    public String toString() {
        String status = estaLivre()
                ? "Livre"
                : "Ocupado - " + RepositorioDeUsuario.buscarUsuario(consulta.getCpfPaciente());

        return inicio + " - " + fim + " | " + status;
    }
}
