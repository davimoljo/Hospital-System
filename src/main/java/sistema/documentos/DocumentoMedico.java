package sistema.documentos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import usuario.Paciente;
import usuario.Medico;
import java.time.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoDocumento")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Atestado.class, name = "atestado"),
        @JsonSubTypes.Type(value = Receita.class, name = "receita"),
        @JsonSubTypes.Type(value = Exame.class, name = "exame")
})

public abstract class DocumentoMedico {
    private String id;
    protected Paciente pacienteRelacionado;
    protected Medico medicoRelacionado;
    protected String conteudo;
    protected LocalDate dataCriacao;

    public DocumentoMedico() {

    }

    public DocumentoMedico(Paciente paciente, Medico medico) {
        pacienteRelacionado = paciente;
        medicoRelacionado = medico;
        dataCriacao = LocalDate.now();
        id = paciente.getCpf() + dataCriacao.toString();

    }

    public String getId() {
        return id;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public Medico getMedicoResponsavel() {
        return medicoRelacionado;
    }

    public Paciente getPacienteRelacionado() {
        return pacienteRelacionado;
    }

    public void setMedicoResponsavel(Medico m) {
        medicoRelacionado = m;
    }

    public abstract String gerarConteudo();

}
