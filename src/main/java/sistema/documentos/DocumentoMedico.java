package sistema.documentos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import usuario.Paciente;
import usuario.Medico;
import java.time.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, // Identifica pelo nome
        include = JsonTypeInfo.As.PROPERTY, // Inclui uma propriedade no JSON
        property = "tipoDocumento" // Nome da propriedade que aparecerá no JSON
)
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

    public DocumentoMedico(Paciente paciente, Medico medico) {
        pacienteRelacionado = paciente;
        medicoRelacionado = medico;
        id = paciente.getCpf() + dataCriacao.toString();
    }

    public String getId() {
        return id;
    }

    public LocalDate getData() {
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
