package excessoes;

public class DataInvalida extends IllegalArgumentException {
    public DataInvalida(String message) {
        super(message);
    }
}
