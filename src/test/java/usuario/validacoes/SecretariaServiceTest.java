package usuario.validacoes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.Hospital;
import usuario.Especialidade;
import usuario.Medico;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SecretariaServiceTest {
    private Hospital hospital;
    private final String CPF_MED = "999.999.999-99";
    private final String CRM_MED = "CRM-TESTE";
    private final String CPF_PAC = "888.888.888-88";

    @BeforeEach
    void setUp() {
        hospital = new Hospital();

        Map<DayOfWeek, Medico.HorarioExpediente> expediente = new HashMap<>();
        Medico.HorarioExpediente horarioComercial = new Medico.HorarioExpediente(
                LocalTime.of(8, 0),
                LocalTime.of(18, 0));

        for (DayOfWeek dia : DayOfWeek.values()) {
            expediente.put(dia, horarioComercial);
        }

        try {
            hospital.cadastrarMedico(
                    "Dr Teste",
                    CPF_MED,
                    "1234",
                    "med@test.com",
                    CRM_MED,
                    Especialidade.GERAL,
                    expediente,
                    30);
        } catch (Exception ignored) {
        }

        try {
            hospital.cadastrarPaciente("Pac Teste", CPF_PAC, "1234", "pac@test.com", "Particular");
        } catch (Exception ignored) {
        }
    }

    @Test
    void deveAgendarConsultaComSucesso() {
        String msg = SecretariaService.agendarConsulta(hospital, CPF_PAC, CRM_MED, "01/01/2030", "10:00");
        assertTrue(msg.contains("Agendamento realizado"));
    }

    @Test
    void deveFalharSeMedicoNaoExistir() {
        assertThrows(RuntimeException.class, () -> {
            SecretariaService.agendarConsulta(hospital, CPF_PAC, "CRM-INEXISTENTE", "01/01/2030", "10:00");
        });
    }

    @Test
    void deveFalharSeHorarioForaDoExpediente() {
        assertThrows(RuntimeException.class, () -> {
            SecretariaService.agendarConsulta(hospital, CPF_PAC, CRM_MED, "01/01/2030", "20:00");
        });
    }

    @Test
    void deveFalharComDataInvalida() {
        Exception e = assertThrows(RuntimeException.class, () -> {
            SecretariaService.agendarConsulta(hospital, CPF_PAC, CRM_MED, "2030-01-01", "10:00");
        });
        assertTrue(e.getMessage().contains("Data inválida") || e.getMessage().contains("formato"));
    }
}