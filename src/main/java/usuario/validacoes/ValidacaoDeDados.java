package usuario.validacoes;

public class ValidacaoDeDados {
    private static String[] dominiosValidos = {"gmail.com", "yahoo.com", "hotmail.com"};
    private static String[] conveniosValidos = {"unimed", "sabin sinai"};

    public static boolean validaEmail(String email) {
        boolean dominioValido = false;
        String[] validacao = email.split("@");
        if (validacao.length != 2) {
            return false;
        }
        for (String valida : dominiosValidos) {
            if (valida.equals(validacao[1])) {
                dominioValido = true;
                break;
            }
        }

        return dominioValido;
    }

    public static boolean validaTelefone(String telefone) {
        for (Character c : telefone.toCharArray()) {
            if (Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean validaConvenio(String convenio) {
        for (String conv : conveniosValidos){
            if (convenio.toLowerCase().equals(conv)) {
                return true;
            }
        }

        return false;
    }
}
