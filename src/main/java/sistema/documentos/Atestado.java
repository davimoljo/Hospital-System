package sistema.documentos;

import usuario.*;

import java.time.*;

public class Atestado extends DocumentoMedico {
    private LocalDate dataTermino;

    public Atestado(Paciente paciente, Medico medico, LocalDate termino) {
        super(paciente, medico);
        dataTermino = termino;
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


                Local e data: %s, %s

                Médico Responsável:
                Nome: Dr(a). %s
                CRM: %s
                Assinatura: ___________________________
                """
                .formatted(
                        pacienteRelacionado.getNome(),
                        pacienteRelacionado.getCpf(),
                        dataCriacao,
                        dataTermino,
                        medicoRelacionado.getNome(),
                        medicoRelacionado.getCrm());
        return conteudo;
    }

}
