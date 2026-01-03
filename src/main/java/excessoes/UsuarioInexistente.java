package excessoes;

public class UsuarioInexistente extends RuntimeException {
    public UsuarioInexistente(String message) {
        super(message);
    }

}
