package sistema.documentos;

import usuario.*;

import java.time.*;

public class Atestado extends DocumentoMedico {
    private LocalDate dataTermino;
    private int qtdDiasDeAfastamento;

    public Atestado(String nomePaciente, String nomeMedico, String cpfPaciente, String cpfMedico, LocalDate dataCriacao, int qtdDiasDeAfastamento) {
        super(nomePaciente, nomeMedico, cpfPaciente, cpfMedico, dataCriacao);
        this.qtdDiasDeAfastamento = qtdDiasDeAfastamento;
        dataTermino = dataCriacao.plusDays(qtdDiasDeAfastamento);
    }

    @Override
    public String gerarConteudo() {
        conteudo = """
                ATESTADO MÉDICO

                Atesto, para os devidos fins, que o(a) paciente
                Nome: %s
                CPF: %s

                foi atendido(a) nesta instituição na data de %s,
                necessitando de afastamento de suas atividades habituais
                por um período de %d dia(s), a contar desta data.


                Data: %s

                Médico Responsável:
                Nome: Dr(a). %s
                CRM: %s
                Assinatura: ___________________________
                """
                .formatted(
                        nomePaciente,
                        cpfPaciente,
                        dataCriacao.toString(),
                        qtdDiasDeAfastamento,
                        dataCriacao.toString(),
                        nomeMedico,
                        cpfMedico
                );
        return conteudo;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }
}
