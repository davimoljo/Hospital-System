package usuario.validacoes;

import excessoes.SenhaIncorretaException;
import excessoes.UsuarioInexistente;
import org.junit.jupiter.api.Test;
import usuario.Paciente;
import usuario.Usuario;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    @Test
    void deveAutenticarFormatoCorreto() {
        // CPF 11 dígitos e Senha > 3
        assertTrue(LoginService.autenticarEntradas("12345678901", "1234"));
    }

    @Test
    void deveRejeitarCpfInvalido() {
        assertFalse(LoginService.autenticarEntradas("123", "1234"));
    }

    @Test
    void deveLogarUsuarioExistente() {
        Usuario u = new Paciente("Teste", "11111111111", "1234", "email", "Conv");
        List<Usuario> banco = List.of(u);

        Usuario logado = LoginService.validarUsuario(banco, "11111111111", "1234");
        assertNotNull(logado);
        assertEquals("Teste", logado.getNome());
    }

    @Test
    void deveLancarErroSenhaIncorreta() {
        Usuario u = new Paciente("Teste", "11111111111", "1234", "email", "Conv");
        List<Usuario> banco = List.of(u);

        assertThrows(SenhaIncorretaException.class, () -> {
            LoginService.validarUsuario(banco, "11111111111", "senhaErrada");
        });
    }
}