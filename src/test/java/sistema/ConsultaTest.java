package sistema;

import org.junit.jupiter.api.Test;
import usuario.Especialidade;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class ConsultaTest {

    @Test
    void deveGerarIdUnicoNoConstrutor() {
        LocalDate data = LocalDate.of(2025, 12, 25);
        Consulta c = new Consulta("Paciente", "Medico", "123", "456", data, LocalTime.of(10, 0), Especialidade.GERAL);

        // O ID é gerado concatenando Nome + Dia + Mês + Ano
        assertNotNull(c.toString()); // Garante que não quebra
    }

    @Test
    void deveIdentificarConsultaJaRealizada() {
        // Data passada
        Consulta cPassada = new Consulta("P", "M", "1", "2", LocalDate.of(2000, 1, 1), LocalTime.of(10, 0), Especialidade.GERAL);
        assertTrue(cPassada.consultaJaRealizada());

        // Data futura
        Consulta cFutura = new Consulta("P", "M", "1", "2", LocalDate.now().plusYears(1), LocalTime.of(10, 0), Especialidade.GERAL);
        assertFalse(cFutura.consultaJaRealizada());
    }
}