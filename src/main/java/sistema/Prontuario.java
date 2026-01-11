package sistema;

import usuario.*;
import utilitarios.*;
import java.util.*;

public class Prontuario {
    private String nomePaciente;
    private String cpfPaciente;
    private String doenca;
    private StatusDoenca status;

    public Prontuario() {

    }

    public Prontuario(String nomePaciente, String cpfPaciente, String doenca, StatusDoenca status) {
        this.nomePaciente = nomePaciente;
        this.cpfPaciente = cpfPaciente;
        this.doenca = doenca;
        this.status = status;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public String getDoenca() {
        return doenca;
    }

    public StatusDoenca getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return ("""
                Prontuário de: %s

                Doença: %s

                Condição: %s
                """
                .formatted(nomePaciente, doenca, status.toString()));
    }

    public void setStatus(StatusDoenca status) {
        this.status = status;
    }
}
