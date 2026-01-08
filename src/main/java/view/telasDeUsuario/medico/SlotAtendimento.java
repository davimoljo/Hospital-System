package view.telasDeUsuario.medico;

import sistema.Consulta;
import utilitarios.Hora;

public class SlotAtendimento {

    private Hora inicio;
    private Hora fim;
    private Consulta consulta; // null = slot livre

    public SlotAtendimento(Hora inicio, Hora fim) {
        if (inicio.compareTo(fim) >= 0)
            throw new IllegalArgumentException("Horário inválido");

        this.inicio = inicio;
        this.fim = fim;
        this.consulta = null;
    }

    public Hora getInicio() {
        return inicio;
    }

    public Hora getFim() {
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
                : "Ocupado - " + consulta.getPaciente().getNome();

        return inicio + " - " + fim + " | " + status;
    }
}
