package sistema.documentos;

import usuario.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Atestado extends DocumentoMedico {
    private LocalDate dataTermino;

    public Atestado() {

    }

    public Atestado(Paciente paciente, Medico medico, LocalDate termino) {
        super(paciente, medico);
        dataTermino = termino;
    }

    @Override
    public String gerarConteudo() {
        // 1. Configura a formatação da data para ficar "bonita" (DD/MM/AAAA)
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = this.dataCriacao.format(formatador);

        // 2. Calcula a diferença de dias entre o início e o fim (se tiver dataTermino)
        // Se dataTermino for null, assumimos 1 dia.
        long diasDeAfastamento = 1;
        if (this.dataTermino != null) {
            diasDeAfastamento = ChronoUnit.DAYS.between(this.dataCriacao, this.dataTermino);
            if (diasDeAfastamento == 0)
                diasDeAfastamento = 1; // Mínimo de 1 dia
        }

        // 3. Monta o texto preenchendo TODOS os campos na ordem certa
        this.conteudo = """
                ATESTADO MÉDICO

                Atesto, para os devidos fins, que o(a) paciente
                Nome: %s
                CPF: %s

                foi atendido(a) nesta instituição na data de %s,
                necessitando de afastamento de suas atividades habituais
                por um período de %d dia(s), a contar desta data.


                Local e data: Juiz de Fora, %s

                Médico Responsável:
                Nome: Dr(a). %s
                CRM: %s
                Assinatura: ___________________________
                """
                .formatted(
                        pacienteRelacionado.getNome(),
                        pacienteRelacionado.getCpf(),
                        dataFormatada,
                        diasDeAfastamento,
                        dataFormatada,
                        medicoRelacionado.getNome(),
                        medicoRelacionado.getCrm());

        return this.conteudo;
    }

}
