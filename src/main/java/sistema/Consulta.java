package sistema;

import com.fasterxml.jackson.annotation.JsonIgnore;

import usuario.*;
import utilitarios.StatusDoenca;

import java.time.*;

// Classe para consultas médicas

public class Consulta {
    private LocalDate marcacao;
    private LocalTime hora;
    private String id;
    private Prontuario prontuario;
    private String nomePaciente;
    private String nomeMedico;
    private String cpfPaciente;
    private String cpfMedicoResponsavel;
    private Especialidade especialidade;

    public Consulta(String nomePaciente, String nomeMedico, String cpfPaciente, String cpfMedico, LocalDate marcacao,
            LocalTime hora, Especialidade especialidade) {
        id = nomePaciente + marcacao.getDayOfMonth() + marcacao.getMonthValue() + marcacao.getYear();
        this.nomePaciente = nomePaciente;
        this.nomeMedico = nomeMedico;
        this.cpfPaciente = cpfPaciente;
        this.cpfMedicoResponsavel = cpfMedico;
        this.marcacao = marcacao;
        this.hora = hora;
        this.especialidade = especialidade;
    }

    public Consulta() {
    }

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

    // Funções Getters e Setters
    public String getCpfMedicoResponsavel() {
        return cpfMedicoResponsavel;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public LocalTime getHora() {
        return hora;
    }

    public LocalDate getMarcacao() {
        return marcacao;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

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

    public boolean consultaJaRealizada() {
        return LocalDate.now().isAfter(this.marcacao)
                || (LocalDate.now().isEqual(this.marcacao) && LocalTime.now().isAfter(this.hora));
    }
}
