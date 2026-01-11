package usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MedicoTest {

    private Medico medico;

    @BeforeEach
    void setUp() {
        Map<DayOfWeek, Medico.HorarioExpediente> expediente = new HashMap<>();
        expediente.put(DayOfWeek.MONDAY, new Medico.HorarioExpediente(
                LocalTime.of(8, 0),
                LocalTime.of(18, 0)));
        medico = new Medico(
                "Dr. House", "111.222.333-44", "senha", "email", "CRM123",
                Especialidade.CARDIOLOGISTA,
                expediente,
                30);
    }

    @Test
    void deveValidarHorarioDentroDoExpediente() {

        assertTrue(medico.dentroDoExpediente(LocalTime.of(8, 0), DayOfWeek.MONDAY));

        assertTrue(medico.dentroDoExpediente(LocalTime.of(12, 30), DayOfWeek.MONDAY));

        assertTrue(medico.dentroDoExpediente(LocalTime.of(17, 59), DayOfWeek.MONDAY));
    }

    @Test
    void deveInvalidarHorarioForaDoExpedienteNoMesmoDia() {
        assertFalse(medico.dentroDoExpediente(LocalTime.of(7, 59), DayOfWeek.MONDAY));
        assertFalse(medico.dentroDoExpediente(LocalTime.of(18, 0), DayOfWeek.MONDAY));

        assertFalse(medico.dentroDoExpediente(LocalTime.of(18, 1), DayOfWeek.MONDAY));
    }

    @Test
    void deveInvalidarHorarioEmDiaQueNaoTrabalha() {
        assertFalse(medico.dentroDoExpediente(LocalTime.of(10, 0), DayOfWeek.TUESDAY));
    }
}