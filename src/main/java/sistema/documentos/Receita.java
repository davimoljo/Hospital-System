package sistema.documentos;

import utilitarios.*;
import usuario.*;
import java.util.List;

public class Receita extends DocumentoMedico {
    private List<Medicamento> Medicamentos;

    public Receita(Paciente p, Medico m, List<Medicamento> Medicamentos) {
        super(p, m);
        this.Medicamentos = Medicamentos;
    }

    @Override
    public String gerarConteudo() {
        return "";
    }
}
