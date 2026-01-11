package view.telasDeUsuario.medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import sistema.Consulta;
import sistema.Prontuario;
import sistema.StatusDoenca;
import sistema.Hospital;
import usuario.Medico;
import usuario.Paciente;
import utilitarios.Medicamento;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelaMedico extends JFrame {

    private Medico medico;
    private Hospital hospital;
    private JFrame telaLogin;

    // --- LISTAS E TABELAS ---
    private JTable tabelaAgenda;
    private DefaultTableModel modelAgenda;

    private List<Consulta> consultasExibidas;

    public TelaMedico(Hospital hospital, Medico medico, JFrame telaLogin) {
        this.medico = medico;
        this.hospital = hospital;
        this.telaLogin = telaLogin;
        this.consultasExibidas = new ArrayList<>();

        // Configuração da Janela
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                telaLogin.setVisible(true);
            }
        });

        setTitle("Sistema Hospitalar - Médico: " + medico.getNome());
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOPO =====
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topo.setBackground(new Color(240, 248, 255));

        JLabel lblMedico = new JLabel("Dr(a). " + medico.getNome() + " | CRM: " + medico.getCrm());
        lblMedico.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topo.add(lblMedico, BorderLayout.WEST);

        // ===== ABAS =====
        JTabbedPane abas = new JTabbedPane();

        JPanel abaAgenda = new JPanel(new BorderLayout());

        JPanel painelBotoesAgenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAtualizar = new JButton("Atualizar Agenda");
        JButton btnCancelar = new JButton("Cancelar / Registrar Falta");

        painelBotoesAgenda.add(btnAtualizar);
        painelBotoesAgenda.add(btnCancelar);

        // Modelo da tabela: Data, Hora, Paciente, Status
        modelAgenda = new DefaultTableModel(
                new String[] { "Data", "Hora", "Paciente", "Status Atual" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAgenda = new JTable(modelAgenda);
        tabelaAgenda.setRowHeight(25);

        JScrollPane scrollAgenda = new JScrollPane(tabelaAgenda);

        abaAgenda.add(painelBotoesAgenda, BorderLayout.NORTH);
        abaAgenda.add(scrollAgenda, BorderLayout.CENTER);

        JPanel abaPaciente = new JPanel();
        abaPaciente.setLayout(new BoxLayout(abaPaciente, BoxLayout.Y_AXIS));
        abaPaciente.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnProntuario = new JButton("Realizar Atendimento (Prontuário)");
        JButton btnHistorico = new JButton("Ver Histórico do Paciente");
        JButton btnInternacao = new JButton("Gerenciar Internação");

        btnProntuario.setEnabled(false);
        btnHistorico.setEnabled(false);
        btnInternacao.setEnabled(false);

        // Estilização básica dos botões
        Dimension btnSize = new Dimension(250, 40);
        btnProntuario.setMaximumSize(btnSize);
        btnHistorico.setMaximumSize(btnSize);
        btnInternacao.setMaximumSize(btnSize);

        abaPaciente.add(new JLabel("Ações Clínicas:"));
        abaPaciente.add(Box.createVerticalStrut(10));
        abaPaciente.add(btnProntuario);
        abaPaciente.add(Box.createVerticalStrut(10));
        abaPaciente.add(btnHistorico);
        abaPaciente.add(Box.createVerticalStrut(10));

        abaPaciente.add(btnInternacao);
        JPanel abaDocumentos = new JPanel();
        abaDocumentos.setLayout(new BoxLayout(abaDocumentos, BoxLayout.Y_AXIS));
        abaDocumentos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAtestado = new JButton("Emitir Atestado");
        JButton btnReceita = new JButton("Emitir Receita");
        JButton btnExame = new JButton("Solicitar Exame");

        btnAtestado.setEnabled(false);
        btnReceita.setEnabled(false);
        btnExame.setEnabled(false);

        btnAtestado.setMaximumSize(btnSize);
        btnReceita.setMaximumSize(btnSize);
        btnExame.setMaximumSize(btnSize);

        abaDocumentos.add(new JLabel("Emissão de Documentos:"));
        abaDocumentos.add(Box.createVerticalStrut(10));
        abaDocumentos.add(btnAtestado);
        abaDocumentos.add(Box.createVerticalStrut(10));
        abaDocumentos.add(btnReceita);
        abaDocumentos.add(Box.createVerticalStrut(10));
        abaDocumentos.add(btnExame);

        // Adiciona as abas
        abas.addTab("Minha Agenda", abaAgenda);
        abas.addTab("Atendimento", abaPaciente);
        abas.addTab("Documentos", abaDocumentos);

        add(topo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);

        atualizarTabelaAgenda();

        tabelaAgenda.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean temSelecao = tabelaAgenda.getSelectedRow() != -1;

                btnCancelar.setEnabled(temSelecao);
                btnProntuario.setEnabled(temSelecao);
                btnHistorico.setEnabled(temSelecao);
                btnInternacao.setEnabled(temSelecao);
                btnAtestado.setEnabled(temSelecao);
                btnReceita.setEnabled(temSelecao);
                btnExame.setEnabled(temSelecao);
            }
        });

        btnAtualizar.addActionListener(ev -> atualizarTabelaAgenda());

        btnCancelar.addActionListener(ev -> {
            Consulta c = getConsultaSelecionada();
            if (c == null)
                return;

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja cancelar a consulta de " + c.getNomePaciente()
                            + "?\nIsso liberará o horário para outros pacientes.",
                    "Cancelar Consulta", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                hospital.desmarcarConsulta(c); // Remove da lista global
                JOptionPane.showMessageDialog(this, "Consulta cancelada.");
                atualizarTabelaAgenda();
            }
        });

        btnProntuario.addActionListener(ev -> {
            Consulta c = getConsultaSelecionada();
            if (c == null)
                return;

            JTextField txtDoenca = new JTextField();
            JComboBox<StatusDoenca> comboStatus = new JComboBox<>(StatusDoenca.values());

            Object[] msg = {
                    "Paciente: " + c.getNomePaciente(),
                    "Doença / Diagnóstico:", txtDoenca,
                    "Status:", comboStatus
            };

            int op = JOptionPane.showConfirmDialog(this, msg, "Prontuário", JOptionPane.OK_CANCEL_OPTION);

            if (op == JOptionPane.OK_OPTION && !txtDoenca.getText().isBlank()) {
                c.gerarProntuario(txtDoenca.getText(), (StatusDoenca) comboStatus.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Prontuário registrado com sucesso!");
                atualizarTabelaAgenda();
            }
        });

        btnHistorico.addActionListener(ev -> {
            Consulta c = getConsultaSelecionada();
            if (c != null) {
                Paciente p = hospital.buscarPacientePorCPF(c.getCpfPaciente());
                mostrarHistoricoPaciente(p);
            }
        });

        btnInternacao.addActionListener(ev -> {
            Consulta c = getConsultaSelecionada();
            if (c != null) {
                if (c.getProntuario() == null) {
                    JOptionPane.showMessageDialog(this, "Erro: Crie um prontuário antes de gerenciar internação.");
                    return;
                }
                Paciente p = hospital.buscarPacientePorCPF(c.getCpfPaciente());
                gerenciarInternacao(p, c.getProntuario());
            }
        });

        btnReceita.addActionListener(ev -> {
            Consulta c = getConsultaSelecionada();
            if (c != null) {
                Paciente p = hospital.buscarPacientePorCPF(c.getCpfPaciente());
                emitirReceita(p);
            }
        });

        btnAtestado.addActionListener(ev -> {
            Consulta c = getConsultaSelecionada();
            if (c != null)
                emitirAtestado(hospital.buscarPacientePorCPF(c.getCpfPaciente()));
        });

        btnExame.addActionListener(ev -> {
            Consulta c = getConsultaSelecionada();
            if (c != null)
                emitirExame(hospital.buscarPacientePorCPF(c.getCpfPaciente()));
        });
    }

    private Consulta getConsultaSelecionada() {
        int linha = tabelaAgenda.getSelectedRow();
        if (linha == -1)
            return null;
        // Mapeia a linha visual para a lista de objetos
        return consultasExibidas.get(linha);
    }

    private void atualizarTabelaAgenda() {
        modelAgenda.setRowCount(0); // Limpa tabela visual
        consultasExibidas.clear(); // Limpa lista de controle

        DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm");
        List<Consulta> minhasConsultas = medico.getConsultasMarcadas().stream()
                .sorted((c1, c2) -> {
                    int dataComp = c1.getMarcacao().compareTo(c2.getMarcacao());
                    if (dataComp != 0)
                        return dataComp;
                    return c1.getHora().compareTo(c2.getHora());
                })
                .collect(Collectors.toList());

        for (Consulta c : minhasConsultas) {
            consultasExibidas.add(c); // Guarda na lista para podermos clicar depois

            String statusTexto = "Agendado";
            if (c.getProntuario() != null) {
                statusTexto = "Atendido (" + c.getProntuario().getStatus() + ")";
            }

            modelAgenda.addRow(new Object[] {
                    c.getMarcacao().format(fmtData),
                    c.getHora().format(fmtHora),
                    c.getNomePaciente(),
                    statusTexto
            });
        }
    }

    private void mostrarHistoricoPaciente(Paciente paciente) {
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

        JTable tableHist = new JTable(modelHist);
        JScrollPane scroll = new JScrollPane(tableHist);
        scroll.setPreferredSize(new Dimension(400, 200));

        if (modelHist.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nenhum histórico médico encontrado para este paciente.");
        } else {
            JOptionPane.showMessageDialog(this, scroll, "Histórico: " + paciente.getNome(), JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void gerenciarInternacao(Paciente paciente, Prontuario prontuario) {
        String estadoAtual = paciente.isInternado() ? "INTERNADO" : "NÃO INTERNADO";
        String msg = "Status Doença: " + prontuario.getStatus() + "\nEstado Atual: " + estadoAtual;

        String[] options = { paciente.isInternado() ? "Dar Alta" : "Solicitar Internação", "Cancelar" };

        int choice = JOptionPane.showOptionDialog(this, msg, "Gestão de Internação",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            paciente.alternarEstadoDeInternacao();
            JOptionPane.showMessageDialog(this, "Status de internação alterado com sucesso.");
        }
    }

    private void emitirReceita(Paciente p) {
        JTextArea txtReceita = new JTextArea(8, 30);
        txtReceita.setBorder(BorderFactory.createTitledBorder("Descreva os medicamentos e dosagem"));

        int op = JOptionPane.showConfirmDialog(this, new JScrollPane(txtReceita), "Emitir Receita",
                JOptionPane.OK_CANCEL_OPTION);

        if (op == JOptionPane.OK_OPTION && !txtReceita.getText().isBlank()) {
            String obs = txtReceita.getText();

            String recibo = hospital.gerarReceita(p, medico, LocalDate.now(), obs).gerarConteudo();
            JOptionPane.showMessageDialog(this, "Receita gerada:\n" + recibo);
        }
    }

    private void emitirAtestado(Paciente p) {
        String diasStr = JOptionPane.showInputDialog(this, "Atestado de quantos dias?");
        if (diasStr != null) {
            try {
                int dias = Integer.parseInt(diasStr);
                String recibo = hospital.gerarAtestado(p, medico, java.time.LocalDate.now().plusDays(dias), dias)
                        .gerarConteudo();
                JOptionPane.showMessageDialog(this, "Atestado gerado:\n" + recibo);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Digite um número válido.");
            }
        }
    }

    private void emitirExame(Paciente p) {
        String resultado = JOptionPane.showInputDialog(this, "Descreva o resultado/solicitação do exame:");
        if (resultado != null && !resultado.isBlank()) {
            String recibo = hospital.gerarExame(p, medico, LocalDate.now(), resultado).gerarConteudo();
            JOptionPane.showMessageDialog(this, "Exame registrado:\n" + recibo);
        }
    }
}