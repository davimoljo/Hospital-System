package utilitarios;

public class Hora {
    private final int hora;
    private final int minuto;

    public Hora(int hora, int minuto) {
        if (!horaValida(hora, minuto))
                throw new IllegalArgumentException("Hora inválida");

        this.hora = hora;
        this.minuto = minuto;
    }

    public boolean horaValida(int hora, int minuto) {
        return hora >= 0 && hora <= 23 && minuto >= 0 && minuto <= 59;
    }

    @Override
    public String toString() {
        return hora + ":" + minuto;
    }

    public boolean equals(Hora h) {
        return  hora == h.hora && minuto == h.minuto;
    }
}
