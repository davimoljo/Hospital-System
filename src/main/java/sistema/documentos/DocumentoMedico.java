package sistema.documentos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoDocumento")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Atestado.class, name = "atestado"),
        @JsonSubTypes.Type(value = Receita.class, name = "receita"),
        @JsonSubTypes.Type(value = Exame.class, name = "exame")
})

// Classe abstrata para documentos médicos
public abstract class DocumentoMedico {
    private String id;
    protected String conteudo;
    protected String nomePaciente;
    protected String nomeMedico;
    protected String cpfPaciente;
    protected String cpfMedico;
    protected LocalDate dataCriacao;

    public DocumentoMedico(String nomePaciente, String nomeMedico, String cpfPaciente, String cpfMedico,
            LocalDate dataCriacao) {
        this.nomePaciente = nomePaciente;
        this.nomeMedico = nomeMedico;
        this.cpfPaciente = cpfPaciente;
        this.cpfMedico = cpfMedico;
        this.dataCriacao = dataCriacao;
    }

    public DocumentoMedico() {

    }

    public String getId() {
        return id;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public abstract String gerarConteudo();

}
