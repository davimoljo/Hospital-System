package utilitarios;

public class Data {
    private final int dia;
    private final int mes;
    private final int ano;

    public Data(int dia, int mes, int ano) {
        if (!isDataValida(dia, mes, ano))
            throw new IllegalArgumentException("Erro: Data inválida");

        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    private boolean isDataValida(int dia, int mes, int ano) {
        if (ano < 1)
            return false;
        if (mes < 1 || mes > 12)
            return false;
        if (dia < 1 || dia > 31)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "%02d/%02d/%04d".formatted(dia, mes, ano);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Data))
            return false;
        Data d = (Data) obj;
        return dia == d.dia && mes == d.mes && ano == d.ano;
    }

    public int compareTo(Data outra) {
        if (this.ano != outra.ano)
            return this.ano - outra.ano;
        if (this.mes != outra.mes)
            return this.mes - outra.mes;
        return this.dia - outra.dia;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }
}
