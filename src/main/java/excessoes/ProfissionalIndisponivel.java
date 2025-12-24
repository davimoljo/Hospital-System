package excessoes;

public class ProfissionalIndisponivel extends IllegalArgumentException{
    public ProfissionalIndisponivel(){
        super("O profissional solicitado está indisponível");
    }
}
