package view.telasDeUsuario.telaPaciente;

import sistema.documentos.Atestado;
import sistema.documentos.DocumentoMedico;
import sistema.documentos.Exame;
import sistema.documentos.Receita;
import usuario.*;
import view.TelaLogin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

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
        lblInfo.setFont(new Font("Segue UI", Font.ITALIC, 13));

        JButton configurarUsuario = new JButton("Configurar");
        configurarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaConfiguracao(paciente);
            }
        });

        topo.add(lblPaciente, BorderLayout.WEST);
        topo.add(lblInfo, BorderLayout.EAST);
        topo.add(configurarUsuario, BorderLayout.EAST);

        // ===== ABAS =====
        JTabbedPane abas = new JTabbedPane();

        // ---- ABA GERENCIAR CONSULTAS ----
        JPanel abaConsultas = new JPanel(new BorderLayout());
        String[] colunasConsultas = { "Data/Hora", "Médico", "Especialidade", "Status" };
        modelConsultas = new DefaultTableModel(colunasConsultas, 0);
        this.tabelaConsultas = new JTable(modelConsultas);

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
        JPanel abaDocumentos = new JPanel(new BorderLayout(5, 5));
        abaDocumentos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel esquerdo: lista e botões
        String[] colunaDocs = { "Tipo", "Data Emissão" };
        DefaultTableModel modelDocs = new DefaultTableModel(colunaDocs, 0);
        JTable tableDocs = new JTable(modelDocs);
        tableDocs.setRowHeight(25);
        tableDocs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel painelEsquerdoDocs = new JPanel(new BorderLayout(10, 10));
        painelEsquerdoDocs.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Meus Documentos",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        JPanel painelBotaoDoc = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVisualizar = new JButton("Carregar Documento");
        btnVisualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVisualizar.setPreferredSize(new Dimension(180, 30));
        painelBotaoDoc.add(btnVisualizar);

        painelEsquerdoDocs.add(new JScrollPane(tableDocs), BorderLayout.CENTER);
        painelEsquerdoDocs.add(painelBotaoDoc, BorderLayout.SOUTH);

        // Painel direito: visualização dos documentos
        JPanel painelDireitoVisualizacaoDocs = new JPanel(new BorderLayout(10, 10));
        painelDireitoVisualizacaoDocs.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Conteúdo do Documento",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        JTextArea textAreaDocs = new JTextArea();
        textAreaDocs.setEditable(false);
        textAreaDocs.setFont(new Font("Monospaced", Font.PLAIN, 13)); // Fonte de documento
        textAreaDocs.setLineWrap(true);
        textAreaDocs.setWrapStyleWord(true);
        textAreaDocs.setMargin(new Insets(15, 15, 15, 15)); // Margem interna do texto
        textAreaDocs.setBackground(new Color(250, 250, 250));
        JScrollPane scrollTexto = new JScrollPane(textAreaDocs);
        scrollTexto.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        painelDireitoVisualizacaoDocs.add(scrollTexto, BorderLayout.CENTER);

        JSplitPane divisorDocs = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdoDocs,
                painelDireitoVisualizacaoDocs);
        divisorDocs.setDividerLocation(450); // Largura inicial da lista
        divisorDocs.setBorder(null);

        abaDocumentos.add(divisorDocs, BorderLayout.CENTER);

        atualizarTabelaDocumento(modelDocs);
        btnVisualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizarDocumento(textAreaDocs, tableDocs);
            }
        });

        // ---- ABA HISTÓRICO ----
        JPanel abaHistorico = new JPanel(new BorderLayout());
        abaHistorico.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelConsultas = new JPanel(new GridLayout());
        JPanel painelProntuario = new JPanel(new GridLayout());

        // Painel Esquerdo
        JPanel painelEsquerdo = new JPanel(new BorderLayout(5, 5));
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Consultas Anteriores",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));
        DefaultTableModel consultasAntModel = new DefaultTableModel(colunasConsultas, 0);
        JTable consultasAntList = new JTable(consultasAntModel);
        consultasAntList.setFillsViewportHeight(true);
        consultasAntList.setRowHeight(25);
        painelEsquerdo.add(new JScrollPane(consultasAntList), BorderLayout.CENTER);
        carregarConsultasAnteriores(consultasAntModel);

        // Painel Direito
        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Resumo do Prontuário",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segue UI", Font.BOLD, 14)));
        JTextArea txtPreviewProntuario = new JTextArea("Selecione uma consulta para visualizar os detalhes...");
        txtPreviewProntuario.setEditable(false);
        txtPreviewProntuario.setLineWrap(true);
        txtPreviewProntuario.setWrapStyleWord(true);
        txtPreviewProntuario.setMargin(new Insets(10, 10, 10, 10));
        txtPreviewProntuario.setBackground(new Color(252, 252, 252));
        painelDireito.add(new JScrollPane(txtPreviewProntuario), BorderLayout.CENTER);

        // Divisor
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setDividerLocation(500); // Posição inicial do divisor
        splitPane.setBorder(null);

        // Rodapé
        JButton botaoVisualizarProntuario = new JButton("Visualizar Prontuário");
        botaoVisualizarProntuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botaoVisualizarProntuario.setEnabled(false);
        botaoVisualizarProntuario.setFocusPainted(false);
        botaoVisualizarProntuario.setBackground(new Color(0, 120, 215)); // Azul moderno
        botaoVisualizarProntuario.setForeground(Color.WHITE);
        JPanel painelBotaoViewProntuario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotaoViewProntuario.add(botaoVisualizarProntuario);
        botaoVisualizarProntuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerarProntuario(txtPreviewProntuario, consultasAntList);
            }
        });

        abaHistorico.add(splitPane, BorderLayout.CENTER);
        abaHistorico.add(painelBotaoViewProntuario, BorderLayout.SOUTH);

        // ---- ABA VISITAS ----
        JPanel abaVisitas = new JPanel(new BorderLayout());
        abaVisitas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] colunasVisitas = { "Paciente", "Telefone", "Visitação" };
        DefaultTableModel modelVisitas = new DefaultTableModel(colunasVisitas, 0);
        JTable tabelaVisitas = new JTable(modelVisitas);
        tabelaVisitas.setRowHeight(30);
        tabelaVisitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaVisitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaVisitas.setShowGrid(false);
        tabelaVisitas.setIntercellSpacing(new Dimension(0, 0));

        JScrollPane scrollTabela = new JScrollPane(tabelaVisitas);
        scrollTabela.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Painel Topo
        JPanel painelTopo = new JPanel(new GridLayout(2, 1, 5, 5));
        painelTopo.setOpaque(false);
        JLabel lblTitulo = new JLabel("Controle de Visitação", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segue UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 37, 41));

        JPanel painelBusca = new JPanel(new BorderLayout(10, 0));
        JLabel lblBusca = new JLabel("Buscar Paciente: ");
        lblBusca.setFont(new Font("Segue UI", Font.PLAIN, 14));

        JTextField fieldProcurarPaciente = new JTextField();
        fieldProcurarPaciente.setToolTipText("Digite o nome ou CPF para filtrar...");
        fieldProcurarPaciente.setPreferredSize(new Dimension(300, 30));
        fieldProcurarPaciente.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarPacientes(modelVisitas, fieldProcurarPaciente.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarPacientes(modelVisitas, fieldProcurarPaciente.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarPacientes(modelVisitas, fieldProcurarPaciente.getText());
            }
        });

        painelBusca.add(lblBusca, BorderLayout.WEST);
        painelBusca.add(fieldProcurarPaciente, BorderLayout.CENTER);

        painelTopo.add(lblTitulo);
        painelTopo.add(painelBusca);

        abaVisitas.add(painelTopo, BorderLayout.NORTH);
        abaVisitas.add(scrollTabela, BorderLayout.CENTER);

        abaVisitas.add(painelTopo, BorderLayout.NORTH);
        abaVisitas.add(new JScrollPane(tabelaVisitas), BorderLayout.CENTER);
        abaVisitas.add(
                new JLabel(" Registro de visitas recebidas durante o período de internação.", SwingConstants.CENTER),
                BorderLayout.NORTH);

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
            botaoVisualizarProntuario.setEnabled(consultasAntList.getSelectedRow() != -1);
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

    private void abrirTelaAgendar() {
        TelaAgendar telaAgendar = new TelaAgendar(hospital, paciente, this);
        telaAgendar.setVisible(true);
        this.setVisible(false);
    }

    private void cancelarConsulta() {
        Consulta consulta = paciente.getConsultasMarcadas().get(tabelaConsultas.getSelectedRow());
        hospital.desmarcarConsulta(consulta);
        atualizarTabelaConsultas();
    }

    protected void atualizarTabelaConsultas() {
        modelConsultas.setRowCount(0);

        if (paciente.getConsultasMarcadas().isEmpty()) {
            return;
        }

        System.out.println("Qtd consultas marcadas: " + paciente.getConsultasMarcadas().size());

        hospital.organizarConsultasPorData(paciente.getConsultasMarcadas());
        for (Consulta c : paciente.getConsultasMarcadas()) {
            modelConsultas.addRow(new Object[] {
                    c.getMarcacao().toString() + " || " + c.getHora().toString(),
                    c.getNomeMedico(),
                    c.getEspecialidade(),
                    "Pendente"
            });
        }
    }

    private void carregarConsultasAnteriores(DefaultTableModel modelConsultasAnt) {
        hospital.organizarConsultasPorData(paciente.getConsultasAnteriores());
        for (Consulta c : paciente.getConsultasAnteriores()) {
            String status;
            if (c.getProntuario() == null) {
                status = "Prontuário pendente";
            } else
                status = "Prontuário disponível";
            modelConsultasAnt.addRow(new Object[] {
                    c.getMarcacao().toString() + " || " + c.getHora().toString(),
                    c.getNomeMedico(),
                    c.getEspecialidade(),
                    status
            });
        }
    }

    // funções auxiliares da tela paciente

    private void gerarProntuario(JTextArea previewProntuario, JTable consultasAntList) {
        previewProntuario.removeAll();
        Prontuario prontuario = paciente.getConsultasAnteriores().get(consultasAntList.getSelectedRow())
                .getProntuario();
        if (prontuario == null) {
            JOptionPane.showMessageDialog(this, "Prontuário pendente", "Não foi possível gerar prontuario",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        previewProntuario.append(prontuario.toString());
    }

    private void filtrarPacientes(DefaultTableModel modelVisitas, String nomeOuCpf) {
        modelVisitas.setRowCount(0);
        if (nomeOuCpf.trim().isEmpty()) {
            return;
        }
        List<Usuario> lista;
        if (Character.isLetter(nomeOuCpf.charAt(0))) {
            lista = hospital.procurarUsuariosPorNome(nomeOuCpf);
        } else if (Character.isDigit(nomeOuCpf.charAt(0))) {
            lista = hospital.procurarUsuariosPorCPF(nomeOuCpf);
        } else {
            modelVisitas.setRowCount(0);
            return;
        }
        for (Usuario u : lista) {
            if (u instanceof Paciente p) {
                if (p.equals(paciente)) {
                    continue;
                }
                if (p.isInternado()) {
                    boolean aptoVisitas = p.isAptoAVisitas();
                    String status = aptoVisitas ? "Permitida" : "Indisponível";
                    modelVisitas.addRow(new Object[] {
                            p.getNome(),
                            p.getTelefone(),
                            status
                    });
                }

            }
        }
    }

    private void visualizarDocumento(JTextArea previewDocumento, JTable listaDocumento) {
        int index = listaDocumento.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Nenhum documento selecionado", "Nenhum documento",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        DocumentoMedico documento = paciente.getDocumentos().get(index);
        if (documento instanceof Atestado atestado)
            previewDocumento.append(atestado.toString());
        else if (documento instanceof Exame exame)
            previewDocumento.append(exame.toString());
        else if (documento instanceof Receita receita)
            previewDocumento.append(receita.toString());

    }

    private void atualizarTabelaDocumento(DefaultTableModel modelDocs) {
        modelDocs.setRowCount(0);
        List<DocumentoMedico> lista = paciente.getDocumentos();
        if (lista.isEmpty()) {
            return;
        }
        for (DocumentoMedico documento : lista) {
            String tipo;
            if (documento instanceof Atestado atestado) {
                tipo = "Atestado";
            } else if (documento instanceof Exame exame) {
                tipo = "Exame";
            } else if (documento instanceof Receita receita) {
                tipo = "Receita";
            } else
                tipo = "Error";
            modelDocs.addRow(new Object[] {
                    tipo,
                    documento.getDataCriacao(),
            });
        }
    }

    private void abrirTelaConfiguracao(Paciente paciente) {
        TelaConfiguracaoPaciente telaConfiguracaoPaciente = new TelaConfiguracaoPaciente(paciente, this);
        telaConfiguracaoPaciente.setVisible(true);
        this.setVisible(false);
    }
}
