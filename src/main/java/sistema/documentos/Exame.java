package sistema.documentos;

import usuario.*;

public class Exame extends DocumentoMedico {
    private String resultado;

    public Exame(Paciente p, Medico m, String resultado) {
        super(p, m);
        this.resultado = resultado;
    }

    @Override
    public String gerarConteudo() {
        return """
                RESULTADO DE EXAME
                Nome do paciente: %s
                Médico: %s
                Resultado: %s
                Data: %s
                """.formatted(this.getPacienteRelacionado().getNome(), this.getMedicoResponsavel().getNome(),
                resultado, this.getData().toString());

    }
}
