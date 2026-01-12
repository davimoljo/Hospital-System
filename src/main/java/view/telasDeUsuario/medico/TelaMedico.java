package view.telasDeUsuario.medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import sistema.Consulta;
import sistema.Hospital;
import usuario.Medico;
import usuario.Paciente;
import utilitarios.StatusDoenca;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

// Tela principal do médico no sistema hospitalar

public class TelaMedico extends JFrame {

    private Medico medico;
    private Hospital hospital;
    private JFrame telaLogin;
    private AcoesMedico acoes;
    private JTable tabelaAgenda;
    private DefaultTableModel modelAgenda;

    private List<Consulta> consultasExibidas;

    public TelaMedico(Hospital hospital, Medico medico, JFrame telaLogin) {
        this.medico = medico;
        this.hospital = hospital;
        this.telaLogin = telaLogin;
        this.consultasExibidas = new ArrayList<>();
        acoes = new AcoesMedico(hospital, medico, this);

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

        // TOPO
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topo.setBackground(new Color(240, 248, 255));

        JLabel lblMedico = new JLabel("Dr(a). " + medico.getNome() + " | CRM: " + medico.getCrm());
        lblMedico.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topo.add(lblMedico, BorderLayout.WEST);

        // ABAS
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

        acoes.atualizarTabelaAgenda(modelAgenda, consultasExibidas);

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

        // botões de ação

        btnAtualizar.addActionListener(ev -> acoes.atualizarTabelaAgenda(modelAgenda, consultasExibidas));

        btnCancelar.addActionListener(ev -> {
            Consulta c = acoes.getConsultaSelecionada(tabelaAgenda, consultasExibidas);
            if (c == null)
                return;

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja cancelar a consulta de " + c.getNomePaciente()
                            + "?\nIsso liberará o horário para outros pacientes.",
                    "Cancelar Consulta", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                hospital.desmarcarConsulta(c); // Remove da lista global
                JOptionPane.showMessageDialog(this, "Consulta cancelada.");
                acoes.atualizarTabelaAgenda(modelAgenda, consultasExibidas);
            }
        });

        btnProntuario.addActionListener(ev -> {
            Consulta c = acoes.getConsultaSelecionada(tabelaAgenda, consultasExibidas);
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
                acoes.atualizarTabelaAgenda(modelAgenda, consultasExibidas);
            }
        });

        btnHistorico.addActionListener(ev -> {
            Consulta c = acoes.getConsultaSelecionada(tabelaAgenda, consultasExibidas);
            if (c != null) {
                Paciente p = hospital.buscarPacientePorCPF(c.getCpfPaciente());
                acoes.mostrarHistoricoPaciente(p);
            }
        });

        btnInternacao.addActionListener(ev -> {
            Consulta c = acoes.getConsultaSelecionada(tabelaAgenda, consultasExibidas);
            if (c != null) {
                if (c.getProntuario() == null) {
                    JOptionPane.showMessageDialog(this, "Erro: Crie um prontuário antes de gerenciar internação.");
                    return;
                }
                Paciente p = hospital.buscarPacientePorCPF(c.getCpfPaciente());
                acoes.gerenciarInternacao(p, c.getProntuario());
            }
        });

        btnReceita.addActionListener(ev -> {
            Consulta c = acoes.getConsultaSelecionada(tabelaAgenda, consultasExibidas);
            if (c != null) {
                Paciente p = hospital.buscarPacientePorCPF(c.getCpfPaciente());
                acoes.emitirReceita(p);
            }
        });

        btnAtestado.addActionListener(ev -> {
            Consulta c = acoes.getConsultaSelecionada(tabelaAgenda, consultasExibidas);
            if (c != null)
                acoes.emitirAtestado(hospital.buscarPacientePorCPF(c.getCpfPaciente()));
        });

        btnExame.addActionListener(ev -> {
            Consulta c = acoes.getConsultaSelecionada(tabelaAgenda, consultasExibidas);
            if (c != null)
                acoes.emitirExame(hospital.buscarPacientePorCPF(c.getCpfPaciente()));
        });
    }

}