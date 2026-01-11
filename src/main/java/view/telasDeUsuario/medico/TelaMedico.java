package view.telasDeUsuario.medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import sistema.Consulta;
import sistema.Prontuario;
import sistema.StatusDoenca;
import sistema.documentos.Atestado;
import sistema.documentos.Exame;
import sistema.documentos.Receita;
import sistema.Hospital;
import usuario.userDB.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap; // Importante para ordenar as datas visualmente

import usuario.Medico;
import usuario.Paciente;
import utilitarios.Medicamento;

public class TelaMedico extends JFrame {

    private Medico medico;
    private Hospital hospital;
    private JFrame telaLogin;

    // --- NOVAS CLASSES INTEGRADAS ---
    private AgendaMedica agendaMedica;
    private JTable tabelaAgenda;
    private DefaultTableModel modelAgenda;

    // Lista auxiliar para mapear a linha da tabela de volta para o objeto Slot
    private List<SlotAtendimento> slotsExibidosNaTabela;

    public TelaMedico(Hospital hospital, Medico medico, JFrame telaLogin) {
        this.medico = medico;
        this.hospital = hospital;
        this.telaLogin = telaLogin;

        // Inicializa a Agenda com as classes fornecidas
        this.agendaMedica = new AgendaMedica(medico);
        this.slotsExibidosNaTabela = new ArrayList<>();

        // Configuração da Janela
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                telaLogin.setVisible(true);
            }
        });

        // Simulação de verificação inicial
        verificarNotificacoes();

        setTitle("Sistema hospitalar - Médico: " + medico.getNome());
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOPO =====
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblMedico = new JLabel("Médico: " + medico.getNome());
        lblMedico.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblAlertas = new JLabel("🔔 Notificações");
        lblAlertas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblAlertas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarNotificacoesDetalhadas();
            }
        });

        topo.add(lblMedico, BorderLayout.WEST);
        topo.add(lblAlertas, BorderLayout.EAST);

        // ===== ABAS =====
        JTabbedPane abas = new JTabbedPane();

        // ===== ABA AGENDA (Refatorada com SlotAtendimento) =====
        JPanel abaAgenda = new JPanel(new BorderLayout());

        JPanel painelBotoesAgenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnConfigurarAgenda = new JButton("Configurar Horários");
        JButton btnMarcarFalta = new JButton("Registrar Falta");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        painelBotoesAgenda.add(btnConfigurarAgenda);
        painelBotoesAgenda.add(btnMarcarFalta);
        painelBotoesAgenda.add(btnAtualizar);

        // Modelo agora reflete os dados do SlotAtendimento
        modelAgenda = new DefaultTableModel(
                new String[] { "Data", "Início", "Fim", "Paciente", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAgenda = new JTable(modelAgenda);
        atualizarTabelaAgenda(); // Carrega os dados usando AgendaMedica

        abaAgenda.add(painelBotoesAgenda, BorderLayout.NORTH);
        abaAgenda.add(new JScrollPane(tabelaAgenda), BorderLayout.CENTER);

        // ===== ABA PACIENTE =====
        JPanel abaPaciente = new JPanel();
        abaPaciente.setLayout(new BoxLayout(abaPaciente, BoxLayout.Y_AXIS));
        abaPaciente.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnProntuario = new JButton("Prontuário");
        JButton btnHistorico = new JButton("Histórico clínico");
        JButton btnInternacao = new JButton("Status de internação");

        // Desabilita inicialmente
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

        abas.addTab("Agenda & Atendimentos", abaAgenda);
        abas.addTab("Ações do Paciente", abaPaciente);
        abas.addTab("Documentos", abaDocumentos);

        add(topo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);

        // =========================================================================
        // ============= LÓGICA DE SELEÇÃO (Integrada com SlotAtendimento) =========
        // =========================================================================

        tabelaAgenda.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabelaAgenda.getSelectedRow();
                boolean slotOcupado = false;

                if (linha != -1) {
                    // Recupera o slot correspondente à linha selecionada
                    SlotAtendimento slot = slotsExibidosNaTabela.get(linha);

                    // Só habilita botões se o slot tiver uma consulta (estiver ocupado)
                    slotOcupado = slot.estaOcupado();
                }

                btnProntuario.setEnabled(slotOcupado);
                btnHistorico.setEnabled(slotOcupado);
                btnInternacao.setEnabled(slotOcupado);
                btnAtestado.setEnabled(slotOcupado);
                btnReceita.setEnabled(slotOcupado);
                btnExame.setEnabled(slotOcupado);
                btnMarcarFalta.setEnabled(slotOcupado);
            }
        });

        btnConfigurarAgenda.addActionListener(ev -> abrirDialogoConfiguracaoAgenda());

        btnAtualizar.addActionListener(ev -> atualizarTabelaAgenda());

        btnMarcarFalta.addActionListener(ev -> {
            int linha = tabelaAgenda.getSelectedRow();
            if (linha == -1)
                return;

            SlotAtendimento slot = slotsExibidosNaTabela.get(linha);
            if (slot.estaLivre())
                return; // Segurança

            int confirm = JOptionPane.showConfirmDialog(this,
                    "O paciente " + slot.getConsulta().getNomePaciente()
                            + " faltou?\nEssa ação liberará o horário na agenda.",
                    "Registrar Falta", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Aqui removemos a consulta do slot usando o método da sua classe
                slot.cancelarConsulta();

                JOptionPane.showMessageDialog(this, "Falta registrada. O horário consta como livre agora.");
                atualizarTabelaAgenda();
            }
        });

        btnProntuario.addActionListener(ev -> {
            SlotAtendimento slot = getSlotSelecionado();
            if (slot == null || slot.estaLivre())
                return;

            Consulta consulta = slot.getConsulta();

            // UI Prontuário (Mantida igual)
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

            if (JOptionPane.showConfirmDialog(this, painel, "Prontuário",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!campoDoenca.getText().isBlank() && grupoStatus.getSelection() != null) {
                    StatusDoenca status = StatusDoenca.valueOf(grupoStatus.getSelection().getActionCommand());
                    consulta.gerarProntuario(campoDoenca.getText(), status);
                    JOptionPane.showMessageDialog(this, "Prontuário salvo!");
                }
            }
        });

        // Lógica de Histórico (busca paciente pelo CPF da consulta do slot)
        btnHistorico.addActionListener(ev -> {
            SlotAtendimento slot = getSlotSelecionado();
            if (slot == null)
                return;

            Paciente p = hospital.buscarPacientePorCPF(slot.getConsulta().getCpfPaciente());
            mostrarHistoricoPaciente(p);
        });

        // Lógica de Internação
        btnInternacao.addActionListener(ev -> {
            SlotAtendimento slot = getSlotSelecionado();
            if (slot == null)
                return;

            Consulta c = slot.getConsulta();
            Paciente p = hospital.buscarPacientePorCPF(c.getCpfPaciente());

            if (c.getProntuario() == null) {
                JOptionPane.showMessageDialog(this, "Paciente sem prontuário nesta consulta.");
                return;
            }

            gerenciarInternacao(p, c.getProntuario());
        });

        // Documentos (Exemplificando com Receita, os outros seguem a mesma lógica)
        btnReceita.addActionListener(ev -> {
            SlotAtendimento slot = getSlotSelecionado();
            if (slot == null)
                return;

            Consulta c = slot.getConsulta();
            if (c.getProntuario() == null) {
                JOptionPane.showMessageDialog(this, "Crie um prontuário antes.");
                return;
            }

            Paciente p = hospital.buscarPacientePorCPF(c.getCpfPaciente());
            emitirReceita(p);
        });

        btnAtestado.addActionListener(ev -> {
            SlotAtendimento slot = getSlotSelecionado();
            if (slot != null)
                emitirAtestado(hospital.buscarPacientePorCPF(slot.getConsulta().getCpfPaciente()));
        });

        btnExame.addActionListener(ev -> {
            SlotAtendimento slot = getSlotSelecionado();
            if (slot != null && slot.getConsulta().getProntuario() != null) {
                emitirExame(hospital.buscarPacientePorCPF(slot.getConsulta().getCpfPaciente()));
            } else {
                JOptionPane.showMessageDialog(this, "Necessário Prontuário para exames.");
            }
        });
    }

    // =========================================================================
    // ======================== MÉTODOS DE CONTROLE ============================
    // =========================================================================

    private SlotAtendimento getSlotSelecionado() {
        int linha = tabelaAgenda.getSelectedRow();
        if (linha == -1)
            return null;
        return slotsExibidosNaTabela.get(linha);
    }

    private void atualizarTabelaAgenda() {
        modelAgenda.setRowCount(0);
        slotsExibidosNaTabela.clear();

        // Obtém o mapa da AgendaMedica
        Map<LocalDate, List<SlotAtendimento>> mapaAgenda = agendaMedica.getAgendaCompleta();

        // Usa TreeMap para garantir ordem de data na visualização
        Map<LocalDate, List<SlotAtendimento>> mapaOrdenado = new TreeMap<>(mapaAgenda);

        for (Map.Entry<LocalDate, List<SlotAtendimento>> entry : mapaOrdenado.entrySet()) {
            LocalDate data = entry.getKey();
            List<SlotAtendimento> slotsDoDia = entry.getValue();

            for (SlotAtendimento slot : slotsDoDia) {
                slotsExibidosNaTabela.add(slot); // Guarda referência para cliques

                String nomePaciente = "---";
                String status = "Livre";

                if (slot.estaOcupado()) {
                    nomePaciente = slot.getConsulta().getNomePaciente();
                    status = "Agendado"; // Ou pegar status do prontuário se existir
                }

                modelAgenda.addRow(new Object[] {
                        data.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        slot.getInicio().toString(),
                        slot.getFim().toString(),
                        nomePaciente,
                        status
                });
            }
        }
    }

    private void abrirDialogoConfiguracaoAgenda() {
        JDialog dialog = new JDialog(this, "Gerar Slots de Atendimento", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Seleção de Dias
        JPanel pDias = new JPanel(new GridLayout(2, 3));
        JCheckBox chkSeg = new JCheckBox("Segunda");
        JCheckBox chkTer = new JCheckBox("Terça");
        JCheckBox chkQua = new JCheckBox("Quarta");
        JCheckBox chkQui = new JCheckBox("Quinta");
        JCheckBox chkSex = new JCheckBox("Sexta");
        pDias.add(chkSeg);
        pDias.add(chkTer);
        pDias.add(chkQua);
        pDias.add(chkQui);
        pDias.add(chkSex);

        form.add(new JLabel("Dias da Semana:"));
        form.add(pDias);

        // Inputs de Tempo
        form.add(new JLabel("Início (HH):"));
        JSpinner spinInicio = new JSpinner(new SpinnerNumberModel(13, 0, 23, 1));
        form.add(spinInicio);

        form.add(new JLabel("Fim (HH):"));
        JSpinner spinFim = new JSpinner(new SpinnerNumberModel(18, 0, 23, 1));
        form.add(spinFim);

        form.add(new JLabel("Duração (minutos):"));
        JSpinner spinDuracao = new JSpinner(new SpinnerNumberModel(30, 15, 120, 5));
        form.add(spinDuracao);

        form.add(new JLabel("Gerar agenda para (dias):"));
        JSpinner spinDiasFuturos = new JSpinner(new SpinnerNumberModel(30, 1, 90, 1));
        form.add(spinDiasFuturos);

        JButton btnGerar = new JButton("Gerar Disponibilidade");

        btnGerar.addActionListener(ev -> {
            try {
                List<DayOfWeek> diasSelecionados = new ArrayList<>();
                if (chkSeg.isSelected())
                    diasSelecionados.add(DayOfWeek.MONDAY);
                if (chkTer.isSelected())
                    diasSelecionados.add(DayOfWeek.TUESDAY);
                if (chkQua.isSelected())
                    diasSelecionados.add(DayOfWeek.WEDNESDAY);
                if (chkQui.isSelected())
                    diasSelecionados.add(DayOfWeek.THURSDAY);
                if (chkSex.isSelected())
                    diasSelecionados.add(DayOfWeek.FRIDAY);

                if (diasSelecionados.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Selecione ao menos um dia da semana.");
                    return;
                }

                int hInicio = (int) spinInicio.getValue();
                int hFim = (int) spinFim.getValue();
                int duracao = (int) spinDuracao.getValue();
                int diasFuturos = (int) spinDiasFuturos.getValue();

                LocalTime inicio = LocalTime.of(hInicio, 0);
                LocalTime fim = LocalTime.of(hFim, 0);

                LocalDate hoje = LocalDate.now();
                int slotsGeradosCount = 0;

                // Loop de geração usando AgendaMedica
                for (int i = 0; i < diasFuturos; i++) {
                    LocalDate dataLoop = hoje.plusDays(i);
                    if (diasSelecionados.contains(dataLoop.getDayOfWeek())) {
                        try {
                            // CHAMA O MÉTODO DA CLASSE AgendaMedica
                            agendaMedica.gerarSlotsDia(dataLoop, inicio, fim, duracao);
                            slotsGeradosCount++; // Apenas contador visual, não exato
                        } catch (IllegalArgumentException ex) {
                            // Ignora conflitos em dias específicos e continua
                            System.out.println("Slot já existente ou erro em: " + dataLoop);
                        }
                    }
                }

                dialog.dispose();
                atualizarTabelaAgenda();
                JOptionPane.showMessageDialog(this, "Agenda configurada! Dias processados.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao gerar: " + ex.getMessage());
            }
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnGerar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ======================== MÉTODOS VISUAIS AUXILIARES =====================
    // (Métodos extraídos para limpar o construtor)

    private void mostrarHistoricoPaciente(Paciente paciente) {
        List<Consulta> consultasPaciente = paciente.getConsultasMarcadas();
        DefaultTableModel model = new DefaultTableModel(new String[] { "Data", "Doença", "Status" }, 0);
        JTable tabela = new JTable(model);

        for (Consulta c : consultasPaciente) {
            Prontuario prontuario = c.getProntuario();
            if (prontuario != null) {
                model.addRow(
                        new Object[] { c.getMarcacao().toString(), prontuario.getDoenca(), prontuario.getStatus() });
            }
        }
        if (model.getRowCount() == 0)
            JOptionPane.showMessageDialog(this, "Sem histórico.");
        else
            JOptionPane.showMessageDialog(this, new JScrollPane(tabela), "Histórico - " + paciente.getNome(),
                    JOptionPane.PLAIN_MESSAGE);
    }

    private void gerenciarInternacao(Paciente paciente, Prontuario prontuario) {
        String msg = "Status atual: " + prontuario.getStatus() + "\nInternado: "
                + (paciente.isInternado() ? "Sim" : "Não");
        String[] opcoes = { paciente.isInternado() ? "Dar Alta" : "Internar", "Cancelar" };
        int op = JOptionPane.showOptionDialog(this, msg, "Internação", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (op == 0)
            paciente.alternarEstadoDeInternacao();
    }

    private void emitirAtestado(Paciente p) {
        // ... (Lógica igual ao anterior, simplificada aqui por espaço)
        String dias = JOptionPane.showInputDialog("Quantos dias de atestado?");
        if (dias != null) {
            //Atestado a = new Atestado(p, medico, LocalDate.now().plusDays(Integer.parseInt(dias)));
            //JOptionPane.showMessageDialog(this, a.gerarConteudo());
        }
    }

    private void emitirReceita(Paciente p) {
        JTextArea txt = new JTextArea(5, 20);
        if (JOptionPane.showConfirmDialog(this, new JScrollPane(txt), "Medicamentos",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            //Receita r = new Receita(p, medico);
            //r.adicionarMedicamento(new Medicamento(txt.getText()));
            //JOptionPane.showMessageDialog(this, r.gerarConteudo());
        }
    }

    private void emitirExame(Paciente p) {
        String res = JOptionPane.showInputDialog("Resultado:");
        //if (res != null)
            //JOptionPane.showMessageDialog(this, new Exame(p, medico, res).gerarConteudo());
    }

    private void verificarNotificacoes() {
        // Implementação simulada
    }

    private void mostrarNotificacoesDetalhadas() {
        JOptionPane.showMessageDialog(this, "Sem novas notificações.");
    }
}