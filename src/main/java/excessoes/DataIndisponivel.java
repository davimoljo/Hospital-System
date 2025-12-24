package excessoes;

public class DataIndisponivel extends IllegalArgumentException{
    public DataIndisponivel(){
        super("A data solicitada está indisponível");
    }
}
