package utilitarios;

public class Data {
    private final int dia;
    private final int mes;
    private final int ano;

    public Data(int dia, int mes, int ano) {
        if (isDataValida())
            throw new IllegalArgumentException("Erro: Data inválida");
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public boolean isDataValida() {
        if (dia < 1 || dia > 31 || mes > 12 || mes < 1)
            return true;
        return false;

    }

    @Override
    public String toString() {
        return "%s/%s/%s".formatted(dia, mes, ano);
    }

    public boolean equals(Data d){
        return dia == d.dia && mes == d.mes && ano == d.ano;
    }

    public int getDia() {return dia;}
    public int getMes() {return mes;}
    public int getAno() {return ano;}

    public int compareTo(Data outra) {
        if (this.ano != outra.ano) return Integer.compare(this.ano, outra.ano);
        if (this.mes != outra.mes) return Integer.compare(this.mes, outra.mes);
        return Integer.compare(this.dia, outra.dia);
    }
}
