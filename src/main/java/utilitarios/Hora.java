package utilitarios;

public class Hora implements Comparable<Hora> {
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

    public Hora adicionarMinutos(int minutos) {
        int totalMinutos = hora * 60 + minuto + minutos;

        if (totalMinutos < 0 || totalMinutos >= 24 * 60)
            throw new IllegalArgumentException("Hora resultante inválida");

        int novaHora = totalMinutos / 60;
        int novoMinuto = totalMinutos % 60;

        return new Hora(novaHora, novoMinuto);
    }

    @Override
    public int compareTo(Hora outra) {
        if (this.hora != outra.hora)
            return Integer.compare(this.hora, outra.hora);

        return Integer.compare(this.minuto, outra.minuto);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Hora))
            return false;

        Hora h = (Hora) obj;
        return hora == h.hora && minuto == h.minuto;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hora, minuto);
    }

    public int getHora() {
        return hora;
    }

    public int getMinuto() {
        return minuto;
    }
}
