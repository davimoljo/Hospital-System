package sistema.documentos;

import usuario.Paciente;
import utilitarios.Data;
import usuario.Medico;

public abstract class DocumentoMedico {
    private String id;
    protected Paciente pacienteRelacionado;
    protected Medico medicoRelacionado;
    protected String conteudo;
    protected Data dataCriacao;

    public DocumentoMedico(Paciente paciente, Medico medico) {
        pacienteRelacionado = paciente;
        medicoRelacionado = medico;
        id = paciente.getCpf() + dataCriacao.toString();
    }

    public String getId() {
        return id;
    }

    public Data getData() {
        return dataCriacao;
    }

    public Medico getMedicoResponsavel() {
        return medicoRelacionado;
    }

    public Paciente getPacienteRelacionado() {
        return pacienteRelacionado;
    }

    public abstract String gerarConteudo();

}
