package utilitarios;

import excessoes.HoraInvalida;

public class Hora {
    private int hora;
    private int minuto;

    public Hora(int hora, int minuto) {
        if (!horaValida(hora, minuto))
            throw new HoraInvalida("Hora inválida");

        this.hora = hora;
        this.minuto = minuto;
    }

    public Hora(){}

    public Hora (String hora, String minuto) {
        this.hora = (int) Integer.parseInt(hora);
        this.minuto = (int) Integer.parseInt(minuto);
        if (!horaValida(this.hora, this.minuto)){
            throw new HoraInvalida("Hora inválida");
        }
    }

    public boolean horaValida(int hora, int minuto) {
        return hora >= 0 && hora <= 23 && minuto >= 0 && minuto <= 59;
    }

    @Override
    public String toString() {
        if (minuto < 10)
            return hora + ":0" + minuto;

        return hora + ":" + minuto;
    }

    public boolean equals(Hora h) {
        return hora == h.hora && minuto == h.minuto;
    }

    public int getHora() {
        return hora;
    }

    public int getMinuto() {
        return minuto;
    }
}
