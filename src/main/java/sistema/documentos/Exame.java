package sistema.documentos;

import usuario.*;

import java.time.LocalDate;

public class Exame extends DocumentoMedico {
    private String resultado;

    public Exame(String nomePaciente, String nomeMedico, String cpfPaciente, String cpfMedico, LocalDate dataCriacao, String resultado) {
        super(nomePaciente, nomeMedico, cpfPaciente, cpfMedico, dataCriacao);
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
                """.formatted(this.nomePaciente, this.nomeMedico,
                resultado, dataCriacao.toString());

    }
}
