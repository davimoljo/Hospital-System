package view.telasDeUsuario.medico;

import java.awt.Component;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import sistema.Consulta;
import sistema.Hospital;
import sistema.Prontuario;
import usuario.Medico;
import usuario.Paciente;

public class AcoesMedico {

    private final Hospital hospital;
    private final Medico medico;
    private final Component viewParent;

    public AcoesMedico(Hospital hospital, Medico medico, Component viewParent) {
        this.hospital = hospital;
        this.medico = medico;
        this.viewParent = viewParent;
    }

    public Consulta getConsultaSelecionada(JTable tabelaAgenda, List<Consulta> consultasExibidas) {
        int linha = tabelaAgenda.getSelectedRow();
        if (linha == -1)
            return null;
        return consultasExibidas.get(linha);
    }

    public void atualizarTabelaAgenda(DefaultTableModel modelAgenda, List<Consulta> consultasExibidas) {
        modelAgenda.setRowCount(0);
        consultasExibidas.clear();

        DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm");

        List<Consulta> minhasConsultas = this.medico.getConsultasMarcadas().stream()
                .sorted((c1, c2) -> {
                    int dataComp = c1.getMarcacao().compareTo(c2.getMarcacao());
                    if (dataComp != 0)
                        return dataComp;
                    return c1.getHora().compareTo(c2.getHora());
                })
                .collect(Collectors.toList());

        for (Consulta c : minhasConsultas) {
            consultasExibidas.add(c);
            String statusTexto = (c.getProntuario() != null) ? "Atendido (" + c.getProntuario().getStatus() + ")"
                    : "Agendado";

            modelAgenda.addRow(new Object[] {
                    c.getMarcacao().format(fmtData),
                    c.getHora().format(fmtHora),
                    c.getNomePaciente(),
                    statusTexto
            });
        }
    }

    public void mostrarHistoricoPaciente(Paciente paciente) {
        if (paciente == null)
            return;

        DefaultTableModel modelHist = new DefaultTableModel(new String[] { "Data", "Doença", "Status" }, 0);

        for (Consulta c : paciente.getConsultasMarcadas()) {
            if (c.getProntuario() != null) {
                modelHist.addRow(new Object[] {
                        c.getMarcacao(),
                        c.getProntuario().getDoenca(),
                        c.getProntuario().getStatus()
                });
            }
        }

        JScrollPane scroll = new JScrollPane(new JTable(modelHist));
        scroll.setPreferredSize(new Dimension(400, 200));

        if (modelHist.getRowCount() == 0) {
            JOptionPane.showMessageDialog(viewParent, "Nenhum histórico médico encontrado.");
        } else {
            JOptionPane.showMessageDialog(viewParent, scroll, "Histórico: " + paciente.getNome(),
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void gerenciarInternacao(Paciente paciente, Prontuario prontuario) {
        String estadoAtual = paciente.isInternado() ? "INTERNADO" : "NÃO INTERNADO";
        String msg = "Status Doença: " + prontuario.getStatus() + "\nEstado Atual: " + estadoAtual;
        String[] options = { paciente.isInternado() ? "Dar Alta" : "Solicitar Internação", "Cancelar" };

        int choice = JOptionPane.showOptionDialog(viewParent, msg, "Gestão de Internação",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            paciente.alternarEstadoDeInternacao();
            JOptionPane.showMessageDialog(viewParent, "Status de internação alterado com sucesso.");
        }
    }

    public void emitirReceita(Paciente p) {
        JTextArea txtReceita = new JTextArea(8, 30);
        txtReceita.setBorder(BorderFactory.createTitledBorder("Descreva os medicamentos e dosagem"));

        int op = JOptionPane.showConfirmDialog(viewParent, new JScrollPane(txtReceita), "Emitir Receita",
                JOptionPane.OK_CANCEL_OPTION);

        if (op == JOptionPane.OK_OPTION && !txtReceita.getText().isBlank()) {
            String recibo = hospital.gerarReceita(p, medico, LocalDate.now(), txtReceita.getText()).gerarConteudo();
            JOptionPane.showMessageDialog(viewParent, "Receita gerada:\n" + recibo);
        }
    }

    public void emitirAtestado(Paciente p) {
        String diasStr = JOptionPane.showInputDialog(viewParent, "Atestado de quantos dias?");
        if (diasStr != null) {
            try {
                int dias = Integer.parseInt(diasStr);
                String recibo = hospital.gerarAtestado(p, medico, LocalDate.now().plusDays(dias), dias)
                        .gerarConteudo();
                JOptionPane.showMessageDialog(viewParent, "Atestado gerado:\n" + recibo);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(viewParent, "Digite um número válido.");
            }
        }
    }

    public void emitirExame(Paciente p) {
        String resultado = JOptionPane.showInputDialog(viewParent, "Descreva o resultado/solicitação do exame:");
        if (resultado != null && !resultado.isBlank()) {
            String recibo = hospital.gerarExame(p, medico, LocalDate.now(), resultado).gerarConteudo();
            JOptionPane.showMessageDialog(viewParent, "Exame registrado:\n" + recibo);
        }
    }
}