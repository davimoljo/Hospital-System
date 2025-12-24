package excessoes;

public class ProntuarioJaExistente extends RuntimeException {
    public ProntuarioJaExistente(String message) {
        super(message);
    }
}
