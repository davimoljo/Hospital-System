package sistema;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import usuario.*;
import utilitarios.Data;
import utilitarios.Hora;

public class Consulta {
    private Data marcacao;
    private Medico medico;
    private Paciente paciente;
    private Hora hora;
    private String id;
    private Prontuario prontuario;

    public Consulta(Paciente paciente, Medico medico, Data marcacao, Hora hora) {
        id = paciente.getNome() + marcacao.getDia() + marcacao.getMes() + marcacao.getAno();
        this.paciente = paciente;
        this.medico = medico;
        this.marcacao = marcacao;
        this.hora = hora;
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
                        medico.getNome(),
                        paciente.getNome());

        return detalhes;
    }

    public String Detalhes() {
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

    public void registrarConsulta() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/sistema/docsDB/consultaDB.json");

        ObjectNode root;

        if (file.exists()) {
            try {
                if (file.exists()) {
                    root = (ObjectNode) mapper.readTree(file);
                } else {
                    root = mapper.createObjectNode();
                }
            } catch (IOException e) {
                throw new RuntimeException("Erro ao ler JSON", e);
            }
        } else {
            root = mapper.createObjectNode();
            root.set("usuarios", mapper.createArrayNode());
        }
        ArrayNode consultas;
        if (root.has("consultas")) {
            consultas = (ArrayNode) root.get("consultas");
        } else {
            consultas = mapper.createArrayNode();
            root.set("consultas", consultas);
        }

        ObjectNode novaConsulta = mapper.createObjectNode();
        novaConsulta.put("id", id);
        novaConsulta.put("medicoId", medico.getCrm());

        ObjectNode objData = mapper.createObjectNode();
        objData.put("dia", marcacao.getDia());
        objData.put("mes", marcacao.getMes());
        objData.put("ano", marcacao.getAno());

        ObjectNode objHora = mapper.createObjectNode();
        objHora.put("hora", hora.getHora());
        objHora.put("minuto", hora.getMinuto());

        novaConsulta.set("marcacao", objData);
        novaConsulta.set("hora", objHora);

        consultas.add(novaConsulta);

    }

    protected void setMedico(Medico medico) {
        this.medico = medico;
    }

    protected void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Prontuario gerarProntuario(String doenca, StatusDoenca status) {
        Prontuario prontuarioGerado = new Prontuario(paciente, doenca, status);
        prontuario = prontuarioGerado;
        return prontuarioGerado;
    }

    public Prontuario getProntuario() {
        return prontuario;
    }
}
