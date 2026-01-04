package excessoes;

public class MedicoDesativado extends RuntimeException {
    public MedicoDesativado(String message) {
        super(message);
    }
}
