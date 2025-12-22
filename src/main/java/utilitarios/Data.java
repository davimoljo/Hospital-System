package utilitarios;

public class Data {
    private int dia;
    private int mes;
    private int ano;

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
}
