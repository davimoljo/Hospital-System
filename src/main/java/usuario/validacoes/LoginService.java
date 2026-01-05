package usuario.validacoes;

import usuario.Usuario;
import java.util.List;

import excessoes.SenhaIncorretaException;
import excessoes.UsuarioInexistente;

public class LoginService {
    public static boolean autenticarEntradas(String cpf, String senha) {
        if (cpf.length() == 11 && senha.length() > 3)
            return true;
        return false;

    }

    public static Usuario validarUsuario(List<Usuario> usuarios, String cpf, String senha) {
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getSenha().equals(senha))
                return u;
            else if (u.getCpf().equals(cpf))
                throw new SenhaIncorretaException("Erro! Senha inválida");
        }
        throw new UsuarioInexistente("Erro! Usuário inexistente.");
    }

}
