package sistema.documentos;

import utilitarios.*;
import usuario.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receita extends DocumentoMedico {
    private List<Medicamento> medicamentos;
    private String observacoes;

    public Receita(String nomePaciente, String nomeMedico, String cpfPaciente, String cpfMedico, LocalDate dataCriacao,
            String observacoes) {
        super(nomePaciente, nomeMedico, cpfPaciente, cpfMedico, dataCriacao);
        this.observacoes = observacoes;
        medicamentos = new ArrayList<>();
    }

    @Override
    public String gerarConteudo() {
        StringBuilder medicamentosStr = new StringBuilder();
        for (Medicamento m : medicamentos) {
            medicamentosStr.append(m.getNome()).append("\n");
        }
        String conteudo = """
                RECEITA MÉDICA
                Nome do paciente: %s
                Data: %s
                Medicamentos: %s
                Observações: %s

                """.formatted(
                this.nomePaciente,
                this.dataCriacao.toString(),
                medicamentosStr.toString(),
                observacoes);
        return conteudo;
    }

    public void definirObservacao(String s) {
        observacoes = s;
    }

    public void adicionarMedicamento(Medicamento m) {
        medicamentos.add(m);
    }

}
