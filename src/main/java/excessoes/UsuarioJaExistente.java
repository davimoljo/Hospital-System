package excessoes;

public class UsuarioJaExistente extends RuntimeException {
    public UsuarioJaExistente(String message) {
        super(message);
    }
}
