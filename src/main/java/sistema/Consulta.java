package sistema;

import usuario.*;
import utilitarios.Data;
import utilitarios.Hora;

public class Consulta {
    private Data marcacao;
    private final Medico medico;
    private final Paciente paciente;
    private Hora hora;

    public Consulta(Paciente paciente, Medico medico, Data marcacao, Hora hora) {
        this.paciente = paciente;
        this.medico = medico;
        this.marcacao = marcacao;
        this.hora = hora;
    }

    @Override
    public String toString(){
        String detalhes = """
                Consulta marcada para %s, no horário de %s
                
                Médico responsável: %s
                
                Paciente: %s
                """
                .formatted(
                        marcacao.toString(),
                        hora.toString(),
                        medico.getNome(),
                        paciente.getNome()
                );

        return detalhes;
    }

    public String Detalhes(){
        return toString();
    }

    public Medico getMedicoResposavel() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Hora getHora() {
        return hora;
    }

    public Data getMarcacao() {
        return marcacao;
    }
}
