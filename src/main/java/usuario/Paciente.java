package usuario;

/// MANTENHA O PACKAGE
///

import sistema.Prontuario;
import excessoes.*;
import sistema.Consulta;
import sistema.documentos.DocumentoMedico;

import java.util.*;

public class Paciente extends Usuario {
    private String endereco;
    private String convenio;
    private Prontuario prontuario;
    private List<Consulta> consultasMarcadas;
    private List<Consulta> consultasAnteriores;
    private List<DocumentoMedico> documentos;
    private boolean internado;
    private boolean aptoAVisitas;

    public Paciente(String nome, String cpf, String senha, String email, String convenio) {
        super(nome, cpf, senha, email);

        if (nome == null || cpf == null || senha == null || email == null || convenio == null)
            throw new IllegalArgumentException("Nenhum argumento pode ser nulo na criação de usuário");

        this.convenio = convenio;
        tipo = TipoUsuario.PACIENTE;
        prontuario = null;
        consultasMarcadas = new ArrayList<>();
        consultasAnteriores = new ArrayList<>();
        documentos = new ArrayList<>();
        internado = false;
        aptoAVisitas = false;
    }

    public Paciente() {
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    @Override
    public String getTipoUsuario() {
        return "PACIENTE";
    }

    @Override
    public String toString() {
        return this.nome + " (Convênio: " + this.convenio + ")";
    }

    public Prontuario getProntuario() {
        return prontuario;
    }

    public void agendarConsulta(Consulta consulta) {
        consultasMarcadas.add(consulta);
    }

    public void removerConsulta(Consulta consulta) {
        consultasMarcadas.remove(consulta);
    }

    public List<Consulta> getConsultasMarcadas() {
        return consultasMarcadas;
    }

    public boolean isInternado() {
        return internado;
    }

    public void alternarEstadoDeInternacao() {
        internado = !internado;
    }

    public void alternarVisita() {
        aptoAVisitas = !aptoAVisitas;
    }

    public boolean isAptoAVisitas() {
        return aptoAVisitas;
    }

    public List<Consulta> getConsultasAnteriores() {
        return consultasAnteriores;
    }

    public void setConsultasAnteriores(List<Consulta> consultasAnteriores) {
        this.consultasAnteriores = consultasAnteriores;
    }

    public void addConsultaAnterior(Consulta consultaAnterior) {
        this.consultasAnteriores.add(consultaAnterior);
    }

    public List<DocumentoMedico> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoMedico> documentos) {
        this.documentos = documentos;
    }

    public void addDocumento(DocumentoMedico documentoMedico) {
        this.documentos.add(documentoMedico);
    }
}