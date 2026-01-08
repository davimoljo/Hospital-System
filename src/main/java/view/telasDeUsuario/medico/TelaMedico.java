package view.telasDeUsuario.medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import sistema.Consulta;
import sistema.Prontuario;
import sistema.StatusDoenca;
import sistema.documentos.Atestado;
import sistema.documentos.Exame;
import sistema.documentos.Receita;

import java.awt.*;

import usuario.Medico;
import usuario.Paciente;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import utilitarios.Data;
import utilitarios.Medicamento;
import view.TelaLogin;

public class TelaMedico extends JFrame {

    private Medico medico;
    private JTable tabelaAgenda;
    private List<Consulta> consultas;
    private JFrame telaLogin;

    public TelaMedico(Medico medico, JFrame telaLogin) {
        this.medico = medico;
        this.consultas = medico.getConsultasMarcadas();
        this.telaLogin = telaLogin;

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                telaLogin.setVisible(true);
            }
        });
        setTitle("Sistema hospitalar - Médico");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOPO =====
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblMedico = new JLabel("Médico: " + medico.getNome());
        lblMedico.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblAlertas = new JLabel("🔔");
        lblAlertas.setToolTipText("Notificações");

        topo.add(lblMedico, BorderLayout.WEST);
        topo.add(lblAlertas, BorderLayout.EAST);

        // ===== ABAS =====
        JTabbedPane abas = new JTabbedPane();

        // ===== ABA AGENDA =====
        JPanel abaAgenda = new JPanel(new BorderLayout());

        DefaultTableModel modelAgenda = new DefaultTableModel(
                new String[] { "Hora", "Paciente" }, 0);
        tabelaAgenda = new JTable(modelAgenda);

        for (Consulta c : consultas) {
            modelAgenda.addRow(new Object[] {
                    c.getHora().toString(),
                    c.getNomePaciente()
            });
        }

        abaAgenda.add(new JScrollPane(tabelaAgenda), BorderLayout.CENTER);

        // ===== ABA PACIENTE =====
        JPanel abaPaciente = new JPanel();
        abaPaciente.setLayout(new BoxLayout(abaPaciente, BoxLayout.Y_AXIS));
        abaPaciente.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnProntuario = new JButton("Prontuário");
        JButton btnHistorico = new JButton("Histórico clínico");
        JButton btnInternacao = new JButton("Status de internação");

        btnProntuario.setEnabled(false);
        btnHistorico.setEnabled(false);
        btnInternacao.setEnabled(false);

        abaPaciente.add(btnProntuario);
        abaPaciente.add(Box.createVerticalStrut(10));
        abaPaciente.add(btnHistorico);
        abaPaciente.add(Box.createVerticalStrut(10));
        abaPaciente.add(btnInternacao);

        // ===== ABA DOCUMENTOS =====
        JPanel abaDocumentos = new JPanel();
        abaDocumentos.setLayout(new BoxLayout(abaDocumentos, BoxLayout.Y_AXIS));
        abaDocumentos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAtestado = new JButton("Emitir atestado");
        JButton btnReceita = new JButton("Emitir receita");
        JButton btnExame = new JButton("Emitir resultado de exame");

        btnAtestado.setEnabled(false);
        btnReceita.setEnabled(false);
        btnExame.setEnabled(false);

        abaDocumentos.add(btnAtestado);
        abaDocumentos.add(Box.createVerticalStrut(10));
        abaDocumentos.add(btnReceita);
        abaDocumentos.add(Box.createVerticalStrut(10));
        abaDocumentos.add(btnExame);

        // ===== ADD ABAS =====
        abas.addTab("Agenda", abaAgenda);
        abas.addTab("Paciente", abaPaciente);
        abas.addTab("Documentos", abaDocumentos);

        add(topo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);

        // ===== SELEÇÃO DA TABELA =====
        tabelaAgenda.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean selecionado = tabelaAgenda.getSelectedRow() != -1;
                btnProntuario.setEnabled(selecionado);
                btnHistorico.setEnabled(selecionado);
                btnInternacao.setEnabled(selecionado);
                btnAtestado.setEnabled(selecionado);
                btnReceita.setEnabled(selecionado);
                btnExame.setEnabled(selecionado);
            }
        });

        // ===== PRONTUÁRIO =====
        btnProntuario.addActionListener(e -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha == -1)
                return;

            Consulta consulta = consultas.get(linha);

            JTextField campoDoenca = new JTextField();
            ButtonGroup grupoStatus = new ButtonGroup();

            JPanel painel = new JPanel(new GridLayout(0, 1));
            painel.add(new JLabel("Doença:"));
            painel.add(campoDoenca);
            painel.add(new JLabel("Status da doença:"));

            for (StatusDoenca status : StatusDoenca.values()) {
                JRadioButton rb = new JRadioButton(status.name());
                rb.setActionCommand(status.name());
                grupoStatus.add(rb);
                painel.add(rb);
            }

            int result = JOptionPane.showConfirmDialog(
                    this, painel, "Prontuário",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                if (campoDoenca.getText().isBlank() || grupoStatus.getSelection() == null) {
                    JOptionPane.showMessageDialog(this,
                            "Preencha a doença e selecione o status!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StatusDoenca status = StatusDoenca.valueOf(
                        grupoStatus.getSelection().getActionCommand());

                consulta.gerarProntuario(campoDoenca.getText(), status);
            }
        });

        // ===== HISTÓRICO =====
        btnHistorico.addActionListener(e -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha == -1)
                return;

            Paciente paciente = consultas.get(linha).getPaciente();
            List<Consulta> consultasPaciente = paciente.getConsultasMarcadas();

            DefaultTableModel model = new DefaultTableModel(
                    new String[] { "Data", "Hora", "Doença", "Status", "Internado" }, 0);

            JTable tabela = new JTable(model);

            for (Consulta c : consultasPaciente) {

                Prontuario prontuario = c.getProntuario();

                // Só mostra consultas que têm prontuário
                if (prontuario != null) {
                    model.addRow(new Object[] {
                            c.getMarcacao().toString(),
                            c.getHora().toString(),
                            prontuario.getDoenca(),
                            prontuario.getStatus(),
                            c.getPaciente().isInternado() ? "Sim" : "Não"
                    });
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "O paciente ainda não possui histórico clínico.",
                        "Histórico clínico",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(tabela),
                    "Histórico clínico - " + paciente.getNome(),
                    JOptionPane.PLAIN_MESSAGE);
        });

        // ===== STATUS DE INTERNAÇÃO E VISITAÇÃO =====
        btnInternacao.addActionListener(e -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha == -1)
                return;

            Consulta consulta = consultas.get(linha);
            Paciente paciente = consulta.getPaciente();
            Prontuario prontuario = consulta.getProntuario();

            if (prontuario == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Paciente ainda não possui prontuário.",
                        "Internação",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String mensagem = "Status da doença: " + prontuario.getStatus() + "\n" +
                    "Internado: " + (paciente.isInternado() ? "Sim" : "Não") + "\n" +
                    "Apto a visitas: " + (paciente.isAptoAVisitas() ? "Sim" : "Não") + "\n\n" +
                    "O que deseja fazer?";

            String[] opcoes = {
                    paciente.isInternado() ? "Dar alta" : "Internar",
                    paciente.isAptoAVisitas() ? "Bloquear visitas" : "Permitir visitas",
                    "Cancelar"
            };

            int escolha = JOptionPane.showOptionDialog(
                    this,
                    mensagem,
                    "Status de internação",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]);

            if (escolha == 0) {
                paciente.alternarEstadoDeInternacao();
            } else if (escolha == 1) {
                paciente.alternarVisita();
            }
        });

        // ===== ATESTADO =====
        btnAtestado.addActionListener(e -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha == -1)
                return;

            Consulta consulta = consultas.get(linha);
            Paciente paciente = consulta.getPaciente();

            JTextField campoDia = new JTextField();
            JTextField campoMes = new JTextField();
            JTextField campoAno = new JTextField();

            JPanel painel = new JPanel(new GridLayout(0, 1));
            painel.add(new JLabel("Paciente: " + paciente.getNome()));
            painel.add(new JLabel("Data de término (DD / MM / AAAA):"));
            painel.add(campoDia);
            painel.add(campoMes);
            painel.add(campoAno);

            int result = JOptionPane.showConfirmDialog(
                    this, painel, "Emitir atestado",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Data termino = new Data(
                            Integer.parseInt(campoDia.getText()),
                            Integer.parseInt(campoMes.getText()),
                            Integer.parseInt(campoAno.getText()));

                    Atestado atestado = new Atestado(
                            paciente, medico, termino);

                    JOptionPane.showMessageDialog(
                            this,
                            atestado.gerarConteudo(),
                            "Atestado Médico",
                            JOptionPane.PLAIN_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Data inválida!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ===== RECEITA =====
        btnReceita.addActionListener(e -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha == -1)
                return;

            Consulta consulta = consultas.get(linha);
            Paciente paciente = consulta.getPaciente();
            Prontuario prontuario = consulta.getProntuario();

            if (prontuario == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "É necessário um prontuário para emitir receita.",
                        "Receita",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JTextArea areaMedicamentos = new JTextArea(6, 30);
            JTextArea areaObservacoes = new JTextArea(4, 30);

            JPanel painel = new JPanel(new GridLayout(0, 1));
            painel.add(new JLabel("Medicamentos (1 por linha):"));
            painel.add(new JScrollPane(areaMedicamentos));
            painel.add(new JLabel("Observações:"));
            painel.add(new JScrollPane(areaObservacoes));

            int result = JOptionPane.showConfirmDialog(
                    this,
                    painel,
                    "Emitir receita",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {

                if (areaMedicamentos.getText().isBlank()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Informe ao menos um medicamento.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Receita receita = new Receita(paciente, medico);

                String[] linhasMedicamentos = areaMedicamentos.getText().split("\\n");
                for (String linhaMed : linhasMedicamentos) {
                    if (!linhaMed.isBlank()) {
                        receita.adicionarMedicamento(new Medicamento(linhaMed.trim()));
                    }
                }

                receita.definirObservacao(areaObservacoes.getText());

                JOptionPane.showMessageDialog(
                        this,
                        receita.gerarConteudo(),
                        "Receita Médica",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        // ===== RESULTADO DE EXAME =====
        btnExame.addActionListener(e -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha == -1)
                return;

            Consulta consulta = consultas.get(linha);
            Paciente paciente = consulta.getPaciente();
            Prontuario prontuario = consulta.getProntuario();

            if (prontuario == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "É necessário um prontuário para registrar exame.",
                        "Exame",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JTextArea areaResultado = new JTextArea(6, 30);

            JPanel painel = new JPanel(new GridLayout(0, 1));
            painel.add(new JLabel("Resultado do exame:"));
            painel.add(new JScrollPane(areaResultado));

            int result = JOptionPane.showConfirmDialog(
                    this,
                    painel,
                    "Resultado de exame",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {

                if (areaResultado.getText().isBlank()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Informe o resultado do exame.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Exame exame = new Exame(
                        paciente,
                        medico,
                        areaResultado.getText());

                JOptionPane.showMessageDialog(
                        this,
                        exame.gerarConteudo(),
                        "Resultado de Exame",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

    }
}
