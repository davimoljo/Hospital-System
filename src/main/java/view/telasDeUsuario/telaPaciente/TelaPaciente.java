package view.telasDeUsuario.telaPaciente;
import usuario.*;
import view.TelaLogin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import sistema.*;

public class TelaPaciente extends JFrame {
    private Paciente paciente;
    private Hospital hospital;
    private DefaultTableModel modelConsultas;
    JTable tabelaConsultas;

    public TelaPaciente(Paciente paciente, TelaLogin telaLogin, Hospital hospital) {
        this.paciente = paciente;
        this.hospital = hospital;
        setTitle("Sistema Hospitalar - Área do Paciente");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOPO =====
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topo.setBackground(new Color(245, 245, 245));

        JLabel lblPaciente = new JLabel("Paciente: " + paciente.getNome());
        lblPaciente.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblInfo = new JLabel("CPF: " + paciente.getCpf());
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        topo.add(lblPaciente, BorderLayout.WEST);
        topo.add(lblInfo, BorderLayout.EAST);

        // ===== ABAS =====
        JTabbedPane abas = new JTabbedPane();

        // ---- ABA GERENCIAR CONSULTAS ----
        JPanel abaConsultas = new JPanel(new BorderLayout());
        String[] colunasConsultas = {"Data/Hora", "Médico", "Especialidade", "Status"};
        modelConsultas = new DefaultTableModel(colunasConsultas, 0);
        this.tabelaConsultas = new JTable(modelConsultas);

        // Simulação de preenchimento (ajuste conforme seus métodos de Paciente)
        atualizarTabelaConsultas();

        JPanel painelBotoesConsultas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgendar = new JButton("Agendar Nova Consulta");
        JButton btnCancelar = new JButton("Cancelar Selecionada");
        btnCancelar.setEnabled(false);
        btnAgendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaAgendar();
            }
        });

        painelBotoesConsultas.add(btnAgendar);
        painelBotoesConsultas.add(btnCancelar);

        abaConsultas.add(new JScrollPane(tabelaConsultas), BorderLayout.CENTER);
        abaConsultas.add(painelBotoesConsultas, BorderLayout.SOUTH);

        // ---- ABA DOCUMENTOS (Receitas e Atestados) ----
        JPanel abaDocumentos = new JPanel(new GridLayout(1, 2, 10, 10));
        abaDocumentos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultListModel<String> modelDocs = new DefaultListModel<>();
        JList<String> listaDocs = new JList<>(modelDocs);

        JPanel painelAcoesDoc = new JPanel();
        painelAcoesDoc.setLayout(new BoxLayout(painelAcoesDoc, BoxLayout.Y_AXIS));
        JButton btnVisualizar = new JButton("Visualizar");
        btnVisualizar.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelAcoesDoc.add(btnVisualizar);

        abaDocumentos.add(new JScrollPane(listaDocs));
        abaDocumentos.add(painelAcoesDoc);

        // ---- ABA HISTÓRICO ----
        JPanel abaHistorico = new JPanel(new BorderLayout());
        abaHistorico.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelConsultas = new JPanel(new GridLayout());
        JPanel painelProntuario = new JPanel(new GridLayout());

        //Painel Esquerdo
        JPanel painelEsquerdo = new JPanel(new BorderLayout(5, 5));
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Consultas Anteriores",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));
        DefaultTableModel consultasAntModel = new DefaultTableModel(colunasConsultas, 0);
        JTable consultasAntList = new JTable(consultasAntModel);
        consultasAntList.setFillsViewportHeight(true);
        consultasAntList.setRowHeight(25); // Linhas mais altas para melhor leitura
        painelEsquerdo.add(new JScrollPane(consultasAntList), BorderLayout.CENTER);

        //Painel Direito
        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Resumo do Prontuário",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));
        JTextArea txtPreviewProntuario = new JTextArea("Selecione uma consulta para visualizar os detalhes...");
        txtPreviewProntuario.setEditable(false);
        txtPreviewProntuario.setLineWrap(true);
        txtPreviewProntuario.setWrapStyleWord(true);
        txtPreviewProntuario.setMargin(new Insets(10, 10, 10, 10));
        txtPreviewProntuario.setBackground(new Color(252, 252, 252));
        painelDireito.add(new JScrollPane(txtPreviewProntuario), BorderLayout.CENTER);

        //Divisor
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setDividerLocation(500); // Posição inicial do divisor
        splitPane.setBorder(null);

        //Rodapé
        JButton botaoVisualizarProntuario = new JButton("Visualizar Prontuário");
        botaoVisualizarProntuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botaoVisualizarProntuario.setEnabled(false);
        botaoVisualizarProntuario.setFocusPainted(false);
        botaoVisualizarProntuario.setBackground(new Color(0, 120, 215)); // Azul moderno
        botaoVisualizarProntuario.setForeground(Color.WHITE);
        JPanel painelBotaoViewProntuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotaoViewProntuario.add(botaoVisualizarProntuario);

        abaHistorico.add(splitPane, BorderLayout.CENTER);
        abaHistorico.add(painelBotaoViewProntuario, BorderLayout.SOUTH);

        // ---- ABA VISITAS (Para casos de internação) ----
        JPanel abaVisitas = new JPanel(new BorderLayout());
        String[] colunasVisitas = {"Data", "Visitante", "Parentesco"};
        DefaultTableModel modelVisitas = new DefaultTableModel(colunasVisitas, 0);
        JTable tabelaVisitas = new JTable(modelVisitas);

        abaVisitas.add(new JScrollPane(tabelaVisitas), BorderLayout.CENTER);
        abaVisitas.add(new JLabel(" Registro de visitas recebidas durante o período de internação.", SwingConstants.CENTER), BorderLayout.NORTH);

        // ===== ADD ABAS AO COMPONENTE PRINCIPAL =====
        abas.addTab("Gerenciar Consultas", abaConsultas);
        abas.addTab("Documentos", abaDocumentos);
        abas.addTab("Histórico", abaHistorico);
        abas.addTab("Visitas", abaVisitas);

        // ===== LISTENERS E LOGICA =====
        tabelaConsultas.getSelectionModel().addListSelectionListener(e -> {
            btnCancelar.setEnabled(tabelaConsultas.getSelectedRow() != -1);
        });

        consultasAntList.getSelectionModel().addListSelectionListener(e -> {
            botaoVisualizarProntuario.setEnabled(tabelaConsultas.getSelectedRow() != -1);
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarConsulta();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                telaLogin.setVisible(true);
            }
        });

        add(topo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
    }

    private void abrirTelaAgendar(){
        TelaAgendar telaAgendar = new TelaAgendar(hospital, paciente, this);
        telaAgendar.setVisible(true);
        this.setVisible(false);
    }

    private void cancelarConsulta(){
        Consulta consulta = paciente.getConsultasMarcadas().get(tabelaConsultas.getSelectedRow());
        hospital.desmarcarConsulta(consulta);
        atualizarTabelaConsultas();
    }

    protected void atualizarTabelaConsultas(){
        modelConsultas.setRowCount(0);

        if (paciente.getConsultasMarcadas().isEmpty()){
            return;
        }

        hospital.organizarConsultasPorData(paciente.getConsultasMarcadas());
        for (Consulta c : paciente.getConsultasMarcadas()) {
            modelConsultas.addRow(new Object[]{
                    c.getMarcacao().toString() + " || " + c.getHora().toString(),
                    c.getNomeMedico(),
                    c.getEspecialidade(),
                    "Pendente"
            });
        }
    }

    private void carregarConsultasAnteriores(DefaultTableModel modelConsultasAnt){
        hospital.organizarConsultasPorData(paciente.getConsultasAnteriores());
        for (Consulta c : paciente.getConsultasAnteriores()) {
            modelConsultasAnt.addRow(new Object[]{
                    c.getMarcacao().toString() + " || " + c.getHora().toString(),
                    c.getNomeMedico(),
                    c.getEspecialidade(),
                    ""
            });
        }
    }
}
