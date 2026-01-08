package utilitarios;

import excessoes.HoraInvalida;

public class Hora implements Comparable<Hora>{
    private int hora;
    private int minuto;

    public Hora(int hora, int minuto) {
        if (!horaValida(hora, minuto))
            throw new IllegalArgumentException("Hora inválida");

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

    public Hora adicionarMinutos(int minutos) {
        int totalMinutos = hora * 60 + minuto + minutos;

        if (totalMinutos < 0 || totalMinutos >= 24 * 60)
            throw new IllegalArgumentException("Hora resultante inválida");

        int novaHora = totalMinutos / 60;
        int novoMinuto = totalMinutos % 60;

        return new Hora(novaHora, novoMinuto);
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

    public int compareTo(Hora outra) {
        if (this.hora != outra.hora)
            return Integer.compare(this.hora, outra.hora);

        return Integer.compare(this.minuto, outra.minuto);

    }

    public int getHora() {
        return hora;
    }
    public int getMinuto() {
        return minuto;
    }
}
