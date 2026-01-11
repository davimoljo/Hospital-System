package usuario;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {

    @Test
    void deveCriarPacienteValido() {
        Paciente p = new Paciente("João Silva", "123.456.789-00", "senha123", "joao@email.com", "Unimed");
        assertEquals("João Silva", p.getNome());
        assertEquals("PACIENTE", p.getTipoUsuario());
    }

    @Test
    void naoDeveCriarPacienteComNomeCurto() {
        // Testa validação da classe mãe (Usuario)
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente("J", "123.456.789-00", "senha", "email", "convenio");
        });
        assertEquals("Erro: O nome deve ter mais de 1 caractere.", e.getMessage());
    }

    @Test
    void deveAlternarEstadoDeInternacao() {
        Paciente p = new Paciente();
        assertFalse(p.isInternado()); // Começa falso

        p.alternarEstadoDeInternacao();
        assertTrue(p.isInternado()); // Vira verdadeiro

        p.alternarEstadoDeInternacao();
        assertFalse(p.isInternado()); // Volta para falso
    }
}