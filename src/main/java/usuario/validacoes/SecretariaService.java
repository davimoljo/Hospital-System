package usuario.validacoes;

import sistema.Hospital;
import sistema.Consulta;
import usuario.Medico;
import usuario.Paciente;
import usuario.Usuario;
import utilitarios.Data;
import utilitarios.Hora;
import excessoes.*;

public class SecretariaService {

    // Tenta agendar uma consulta validando se os CPFs e CRMs existem.
    public static String agendarConsulta(Hospital hospital, String cpfPaciente, String crmMedico, String diaStr, String horaStr) {
        try {
            // 1. Buscar o Paciente pelo CPF
            Usuario uPaciente = hospital.procurarUsuarioPorCPF(cpfPaciente);
            if (uPaciente == null || !(uPaciente instanceof Paciente)) {
                // Se não achar, lança erro (o catch lá embaixo vai pegar)
                throw new UsuarioInexistente("Erro: Paciente com CPF " + cpfPaciente + " não encontrado.");
            }
            Paciente paciente = (Paciente) uPaciente;

            // 2. Buscar o Médico pelo CRM
            Medico medico = buscarMedicoPorCrm(hospital, crmMedico);
            if (medico == null) {
                throw new UsuarioInexistente("Erro: Médico com CRM " + crmMedico + " não encontrado.");
            }

            // 3. Converter Data e Hora (pode gerar erro de formato)
            Data data = converterData(diaStr);
            Hora hora = converterHora(horaStr);

            // 4. Mandar o Hospital marcar (valida disponibilidade)
            hospital.marcarConsulta(paciente, medico, data, hora);

            return "Sucesso! Consulta agendada para " + data + " às " + hora + " com Dr. " + medico.getNome();

        } catch (Exception e) {
            // Esse catch pega TUDO (Checked e Unchecked) e repassa como erro de execução
            // Isso resolve o problema do sublinhado vermelho no try
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void cadastrarPaciente(Hospital hospital, String nome, String cpf, String senha, String email, String convenio) {
        hospital.cadastrarPaciente(nome, cpf, senha, email, convenio);
    }

    // --- Métodos Auxiliares Privados ---

    private static Medico buscarMedicoPorCrm(Hospital hospital, String crm) {
        for (Usuario u : hospital.getUsuarios()) {
            if (u instanceof Medico) {
                Medico m = (Medico) u;
                if (m.getCrm().equals(crm)) {
                    return m;
                }
            }
        }
        return null;
    }

    private static Data converterData(String dataStr) {
        try {
            String[] partes = dataStr.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            return new Data(dia, mes, ano);
        } catch (Exception e) {
            throw new IllegalArgumentException("Data inválida. Use o formato DD/MM/AAAA");
        }
    }

    private static Hora converterHora(String horaStr) {
        try {
            String[] partes = horaStr.split(":");
            int h = Integer.parseInt(partes[0]);
            int m = Integer.parseInt(partes[1]);
            return new Hora(h, m);
        } catch (Exception e) {
            throw new IllegalArgumentException("Hora inválida. Use o formato HH:MM");
        }
    }
}