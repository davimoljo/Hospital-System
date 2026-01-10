package usuario.validacoes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.Hospital;
import usuario.Especialidade;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class SecretariaServiceTest {

    private Hospital hospital;
    private final String CPF_MED = "999.999.999-99";
    private final String CRM_MED = "CRM-TESTE";
    private final String CPF_PAC = "888.888.888-88";

    @BeforeEach
    void setUp() {
        hospital = new Hospital();

        // Tenta cadastrar, ignorando erro se já existir (pois salva em arquivo)
        try {
            hospital.cadastrarMedico("Dr Teste", CPF_MED, "1234", "med@test.com", CRM_MED,
                    Especialidade.GERAL, LocalTime.of(8, 0), LocalTime.of(18, 0));
        } catch (Exception ignored) {}

        try {
            hospital.cadastrarPaciente("Pac Teste", CPF_PAC, "1234", "pac@test.com", "Particular");
        } catch (Exception ignored) {}
    }

    @Test
    void deveAgendarConsultaComSucesso() {
        // Data futura para garantir
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
        // Médico trabalha até as 18:00
        assertThrows(RuntimeException.class, () -> {
            SecretariaService.agendarConsulta(hospital, CPF_PAC, CRM_MED, "01/01/2030", "20:00");
        });
    }

    @Test
    void deveFalharComDataInvalida() {
        // Testando formato inválido (hífen em vez de barra)
        Exception e = assertThrows(RuntimeException.class, () -> {
            SecretariaService.agendarConsulta(hospital, CPF_PAC, CRM_MED, "2030-01-01", "10:00");
        });
        assertTrue(e.getMessage().contains("Data inválida"));
    }
}