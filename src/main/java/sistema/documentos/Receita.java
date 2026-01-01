package sistema.documentos;

import utilitarios.*;
import usuario.*;

import java.util.ArrayList;
import java.util.List;

public class Receita extends DocumentoMedico {
    private List<Medicamento> medicamentos;
    private String observacoes;

    public Receita(Paciente p, Medico m) {
        super(p, m);
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
                this.getPacienteRelacionado().getNome(),
                this.getData().toString(),
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
