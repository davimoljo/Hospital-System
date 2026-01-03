package usuario.validacoes;

import usuario.Usuario;
import java.util.List;

import excessoes.SenhaIncorretaException;
import excessoes.UsuarioInexistente;

public class LoginService {
    public static Usuario validarUsuario(List<Usuario> usuarios, String cpf, String senha) {
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getSenha().equals(senha))
                return u;
            throw new SenhaIncorretaException("Erro! Senha incorreta.");
        }
        throw new UsuarioInexistente("Erro! Usuário inexistente.");
    }

}
