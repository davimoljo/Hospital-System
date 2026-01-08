package usuario.validacoes;

import sistema.Hospital;
import usuario.Medico;
import usuario.Paciente;
import usuario.Usuario;
import utilitarios.Data;
import utilitarios.Hora;
import excessoes.*;

public class SecretariaService {

    // Recebe os dados em texto da tela, valida se as pessoas existem e tenta agendar
    public static String agendarConsulta(Hospital hospital, String cpfPaciente, String crmMedico, String diaStr, String horaStr) {
        try {
            // Verifica se o paciente existe pelo CPF
            Usuario uPaciente = hospital.procurarUsuarioPorCPF(cpfPaciente);

            // Verifica se achou alguém e se essa pessoa é realmente um Paciente (e não um Médico/Secretária)
            if (uPaciente == null || !(uPaciente instanceof Paciente)) {
                throw new UsuarioInexistente("Paciente com CPF " + cpfPaciente + " não encontrado.");
            }
            Paciente paciente = (Paciente) uPaciente;

            // Verifica se o médico existe pelo CRM
            Medico medico = buscarMedicoPorCrm(hospital, crmMedico);
            if (medico == null) {
                throw new UsuarioInexistente("Médico com CRM " + crmMedico + " não encontrado.");
            }

            // Converte as Strings da tela para os objetos Data e Hora
            Data data = converterData(diaStr);
            Hora hora = converterHora(horaStr);

            // Se tudo estiver certo, manda o hospital realizar o agendamento
            hospital.marcarConsulta(paciente, medico, data, hora);

            return "Agendamento realizado para " + data + " às " + hora + " com Dr. " + medico.getNome();

        } catch (Exception e) {
            // Captura qualquer erro (formato inválido, médico ocupado, usuário inexistente)
            // e repassa para a tela mostrar o alerta
            throw new RuntimeException(e.getMessage());
        }
    }

    // Apenas repassa os dados para o cadastro do hospital
    public static void cadastrarPaciente(Hospital hospital, String nome, String cpf, String senha, String email, String convenio) {
        hospital.cadastrarPaciente(nome, cpf, senha, email, convenio);
    }

    // Métodos Auxiliares

    // Varre a lista de usuários procurando um médico com o CRM informado
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

    // Transforma a String DD/MM/AAA em um objeto Data, validando o formato com split
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

    // Transforma a String HH:MM em um objeto Hora
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