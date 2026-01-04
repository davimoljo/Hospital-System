package usuario;

public enum Especialidade {
    CARDIOLOGISTA("Cardiologista"), ENDOCRINOLOGISTA("Endocrinologista"), DERMATOLOGISTA("Dermatologista"), NEUROLOGISTA("Neurologista"), GERAL("Geral");

    private final String especialidade;
    Especialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString(){
        return this.especialidade;
    }
}
