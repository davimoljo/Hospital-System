package sistema;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import usuario.*;
import utilitarios.Data;
import utilitarios.Hora;

public class Consulta {
    private Data marcacao;
    private Hora hora;
    private String id;
    private Prontuario prontuario;
    private String nomePaciente;
    private String nomeMedico;
    private String cpfPaciente;
    private String cpfMedicoResponsavel;
    private Especialidade especialidade;

    public Consulta(String nomePaciente, String nomeMedico, String cpfPaciente, String cpfMedico, Data marcacao, Hora hora, Especialidade  especialidade) {
        id = nomePaciente + marcacao.getDia() + marcacao.getMes() + marcacao.getAno();
        this.nomePaciente = nomePaciente;
        this.nomeMedico = nomeMedico;
        this.cpfPaciente = cpfPaciente;
        this.cpfMedicoResponsavel = cpfMedico;
        this.marcacao = marcacao;
        this.hora = hora;
        this.especialidade = especialidade;
    }

    public Consulta(){}

    @Override
    public String toString() {
        String detalhes = """
                Consulta marcada para %s, no horário de %s

                Médico responsável: %s

                Paciente: %s
                """
                .formatted(
                        marcacao.toString(),
                        hora.toString(),
                        nomeMedico,
                        nomePaciente);

        return detalhes;
    }

    public String getCpfMedicoResponsavel() {
        return cpfMedicoResponsavel;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public Hora getHora() {
        return hora;
    }

    public Data getMarcacao() {
        return marcacao;
    }

    public String getNomePaciente(){return nomePaciente;}

    public String getNomeMedico(){return nomeMedico;}

    public Especialidade getEspecialidade(){return especialidade;}

    @JsonIgnore
    protected void setMedico(Medico medico) {
        this.cpfMedicoResponsavel = medico.getCpf();
        this.nomeMedico = medico.getNome();
    }

    protected void setPaciente(Paciente paciente) {
        this.cpfPaciente = paciente.getCpf();
        this.nomePaciente = paciente.getNome();
    }

    public Prontuario gerarProntuario(String doenca, StatusDoenca status) {
        Prontuario prontuarioGerado = new Prontuario(nomePaciente, cpfPaciente, doenca, status);
        prontuario = prontuarioGerado;
        return prontuarioGerado;
    }

    public Prontuario getProntuario() {
        return prontuario;
    }
}
