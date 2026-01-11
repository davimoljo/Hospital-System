package usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class MedicoTest {

    private Medico medico;

    @BeforeEach
    void setUp() {
        // Médico trabalha das 08:00 às 18:00
        medico = new Medico(
                "Dr. House", "111.222.333-44", "senha", "email", "CRM123",
                Especialidade.CARDIOLOGISTA,
                LocalTime.of(8, 0), LocalTime.of(18, 0)
        );
    }

    @Test
    void deveValidarHorarioDentroDoExpediente() {
        // Exatamente no início
        assertTrue(medico.dentroDoExpediente(LocalTime.of(8, 0)));
        // No meio do dia
        assertTrue(medico.dentroDoExpediente(LocalTime.of(12, 30)));
        // Exatamente no fim
        assertTrue(medico.dentroDoExpediente(LocalTime.of(18, 0)));
    }

    @Test
    void deveInvalidarHorarioForaDoExpediente() {
        // Um minuto antes
        assertFalse(medico.dentroDoExpediente(LocalTime.of(7, 59)));
        // Um minuto depois
        assertFalse(medico.dentroDoExpediente(LocalTime.of(18, 1)));
    }
}