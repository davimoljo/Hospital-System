package sistema;
import excessoes.DataIndisponivel;
import excessoes.ProntuarioJaExistente;
import sistema.documentos.Atestado;
import sistema.documentos.DocumentoMedico;
import sistema.documentos.Receita;
import usuario.*;
import utilitarios.*;
import utilitarios.Medicamento;

import java.util.*;


public class Hospital {
    protected final String nomeHospital = "HU (Hospital Universitário)";
    private List<Usuario> usuarios;
    private List<DocumentoMedico> documentos;
    private List<Prontuario> prontuarios;
    private List<Consulta> consultasMarcadas;

    public void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public List<Usuario> procurarUsuariosPorCPF(String cpf){
        List<Usuario> achados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().contains(cpf)) {
                achados.add(usuario);
            }
        }

        return achados;
    }

    public DocumentoMedico gerarAtestado(Paciente p, Medico m, Data termino){
        DocumentoMedico atestado = new Atestado(p, m, termino);
        documentos.add(atestado);
        return atestado;
    }

    public DocumentoMedico gerarRececita(Paciente p, Medico m, List<Medicamento> medicamentos){
        DocumentoMedico receita = new Receita(p, m, medicamentos);
        documentos.add(receita);
        return receita;
    }

    public Consulta marcarConsulta (Paciente p, Medico m, Data marcacao, Hora hora) throws DataIndisponivel{

        for (Consulta consulta : consultasMarcadas) {
            if (consulta.getHora().equals(hora) && consulta.getMarcacao().equals(marcacao) && consulta.getMedicoResposavel().equals(m)) {
                throw new DataIndisponivel();
            }
        }

        Consulta consulta = new Consulta(p, m, marcacao, hora);
        consultasMarcadas.add(consulta);
        return consulta;
    }

    public void organizarConsultasPorData() {
        for (int i = consultasMarcadas.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {

                Consulta c1 = consultasMarcadas.get(j);
                Consulta c2 = consultasMarcadas.get(j + 1);

                int ano1 = c1.getMarcacao().getAno();
                int ano2 = c2.getMarcacao().getAno();

                int mes1 = c1.getMarcacao().getMes();
                int mes2 = c2.getMarcacao().getMes();

                int dia1 = c1.getMarcacao().getDia();
                int dia2 = c2.getMarcacao().getDia();

                boolean trocar = false;

                if (ano1 > ano2) {
                    trocar = true;
                } else if (ano1 == ano2 && mes1 > mes2) {
                    trocar = true;
                } else if (ano1 == ano2 && mes1 == mes2 && dia1 > dia2) {
                    trocar = true;
                }

                if (trocar) {
                    Consulta holder = consultasMarcadas.get(j);
                    consultasMarcadas.set(j, consultasMarcadas.get(j + 1));
                    consultasMarcadas.set(j + 1, holder);
                }
            }
        }
    }

    public void organizarConsultasPorMedicos(){
        for (int i = 0; i < consultasMarcadas.size() - 1; i++){
            for (int j = 0; j < consultasMarcadas.size() - i -1; j++){
                if (consultasMarcadas.get(j).getMedicoResposavel().getNome().compareTo(consultasMarcadas.get(j+1).getMedicoResposavel().getNome()) > 0){
                    Consulta holder = consultasMarcadas.get(j);
                    consultasMarcadas.set(j, consultasMarcadas.get(j + 1));
                    consultasMarcadas.set(j + 1, holder);
                }
            }
        }
    }

    public Prontuario gerarProntuario(Paciente paciente, String doenca, StatusDoenca statusDoenca){
        if (!paciente.definirProntuario(doenca, statusDoenca)){
            throw new ProntuarioJaExistente("Prontuário de " + paciente.getNome() + " ja existente.");
        }
        prontuarios.add(paciente.getProntuario());
        return paciente.getProntuario();
    }
}
