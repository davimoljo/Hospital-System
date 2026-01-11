package view.telasDeUsuario.telaPaciente;

import sistema.Hospital;
import usuario.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class TelaAgendar extends JFrame {
    private JTextField data;
    private JComboBox<Especialidade> especialidadeModel;
    private DefaultListModel<Medico> modelMedico = new DefaultListModel<Medico>();
    private JList<Medico> listaMedicosDisponiveis;

    private JButton botaoAgendar, procurarMedicos;
    private JPanel painelEsquerdo, painelDireito, painelBotoes;
    private Hospital hospital;
    private Paciente paciente;
    private TelaPaciente telaPaciente;
    private JComboBox<LocalTime> comboHorarios;
    LocalDate d;

    public TelaAgendar(Hospital hospital, Paciente paciente, TelaPaciente telaPaciente) {
        this.hospital = hospital;
        this.paciente = paciente;
        this.telaPaciente = telaPaciente;
        setTitle("Agendar Nova Consulta");
        setSize(600, 450);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(telaPaciente);
        comboHorarios = new JComboBox<>();
        comboHorarios.setEnabled(false);

        Color corFundo = new Color(245, 245, 245);
        Font fonteLabel = new Font("Segue UI", Font.BOLD, 14);

        // configurações do painel esquerdo
        painelEsquerdo = new JPanel(new GridBagLayout());
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Detalhes da Consulta"));
        painelEsquerdo.setPreferredSize(new Dimension(250, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        data = new JTextField(10);
        especialidadeModel = new JComboBox<>(Especialidade.values());

        // Adicionando componentes com Labels
        gbc.gridy = 0;
        painelEsquerdo.add(new JLabel("Data (AAAA/MM/DD):"), gbc);
        gbc.gridy = 1;
        painelEsquerdo.add(data, gbc);
        gbc.gridy = 4;
        painelEsquerdo.add(new JLabel("Especialidade:"), gbc);
        gbc.gridy = 5;
        painelEsquerdo.add(especialidadeModel, gbc);

        procurarMedicos = new JButton("Procurar Médicos");
        procurarMedicos.addActionListener(new procurarMedicoEvento(this));
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 5, 10);
        painelEsquerdo.add(procurarMedicos, gbc);

        // painel da direita
        painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Médicos Disponíveis"));

        listaMedicosDisponiveis = new JList<>(modelMedico);
        modelMedico.clear();
        listaMedicosDisponiveis.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMedicosDisponiveis.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !listaMedicosDisponiveis.isSelectionEmpty()) {
                Medico medicoSelecionado = listaMedicosDisponiveis.getSelectedValue();
                atualizarHorariosDisponiveis(medicoSelecionado);

            }
        });
        JScrollPane scrollMedicos = new JScrollPane(listaMedicosDisponiveis);
        painelDireito.add(scrollMedicos, BorderLayout.CENTER);

        // painel inferior
        painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(corFundo);
        painelBotoes.setBorder(new EmptyBorder(10, 10, 10, 10));

        botaoAgendar = new JButton("Confirmar Agendamento");
        botaoAgendar.setPreferredSize(new Dimension(180, 35));
        botaoAgendar.setBackground(Color.BLUE);// Azul destaque
        botaoAgendar.setForeground(Color.WHITE);
        botaoAgendar.setFocusPainted(false);
        botaoAgendar.setFont(new Font("Segue UI", Font.BOLD, 12));
        botaoAgendar.addActionListener(new agendarEvento(this));

        painelBotoes.add(botaoAgendar);

        // adicionando painéis à janela
        JPanel painelCentral = new JPanel(new GridLayout(1, 2, 10, 0));
        painelCentral.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelCentral.add(painelEsquerdo);
        painelCentral.add(painelDireito);

        this.add(painelCentral, BorderLayout.CENTER);
        this.add(painelBotoes, BorderLayout.SOUTH);

        JPanel painelHorario = new JPanel(new BorderLayout());
        painelHorario.add(new JLabel("Horários Disponíveis:"), BorderLayout.NORTH);
        painelHorario.add(comboHorarios, BorderLayout.CENTER);
        painelDireito.add(painelHorario, BorderLayout.SOUTH);

        // Listener para fechar
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                telaPaciente.setVisible(true);
            }
        });
    }

    private class procurarMedicoEvento implements ActionListener {
        TelaAgendar telaAgendar;

        public procurarMedicoEvento(TelaAgendar telaAgendar) {
            this.telaAgendar = telaAgendar;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            modelMedico.clear();
            comboHorarios.removeAllItems(); // Limpa os horários antigos se houver

            String[] dataF = data.getText().split("/");
            Integer[] dataI = new Integer[3];
            Especialidade esp = (Especialidade) especialidadeModel.getSelectedItem();

            if (dataF.length != 3) {
                JOptionPane.showMessageDialog(telaAgendar, "Formato Data inválido", "Erro de formato",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int i = 0;
                for (String s : dataF) {
                    dataI[i++] = Integer.parseInt(s);
                }
                i = 0;
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(telaAgendar, "Data inválida", "Erro de formato",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                d = LocalDate.of(dataI[0], dataI[1], dataI[2]);

                if (LocalDate.now().isAfter(d)) {
                    JOptionPane.showMessageDialog(telaAgendar, "Impossível marcar uma consulta no passado!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (DateTimeException invalida) {
                JOptionPane.showMessageDialog(telaAgendar, "Data inválida", "Erro de formato",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Usuario usuario : hospital.getUsuarios()) {
                if (usuario instanceof Medico medico) {
                    if (medico.getEspecialidade().equals(esp) &&
                            medico.getHoraInicioExpediente(d.getDayOfWeek()) != null) {

                        modelMedico.addElement(medico);
                    }
                }
            }

            // Se nenhum médico encontrado
            if (modelMedico.isEmpty()) {
                JOptionPane.showMessageDialog(telaAgendar, "Nenhum médico atende nesta data/especialidade.");
            }
        }
    }

    private class agendarEvento implements ActionListener {
        TelaAgendar telaAgendar;

        public agendarEvento(TelaAgendar telaAgendar) {
            this.telaAgendar = telaAgendar;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (listaMedicosDisponiveis.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(telaAgendar, "Selecione um médico!");
                return;
            }

            LocalTime horaSelecionada = (LocalTime) comboHorarios.getSelectedItem();

            if (horaSelecionada == null) {
                JOptionPane.showMessageDialog(telaAgendar, "Selecione um horário disponível!");
                return;
            }

            Medico medico = listaMedicosDisponiveis.getSelectedValue();

            try {
                // Marca a consulta usando 'd' e 'horaSelecionada'
                hospital.marcarConsulta(paciente, medico, d, horaSelecionada);

                JOptionPane.showMessageDialog(telaAgendar, "Consulta marcada com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                telaAgendar.dispose();
                telaPaciente.atualizarTabelaConsultas();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(telaAgendar, "Erro: " + ex.getMessage());
            }
        }
    }

    private void atualizarHorariosDisponiveis(Medico medico) {
        comboHorarios.removeAllItems();
        LocalTime inicioExpediente = medico.getHoraInicioExpediente(d);
        LocalTime fimExpediente = medico.getHoraFimExpediente(d);

        if (inicioExpediente == null || fimExpediente == null) {
            return;
        }

        int duracaoConsulta = medico.getDuracaoDasConsultas();
        LocalTime horarioLoopAtual = inicioExpediente;

        while (horarioLoopAtual.isBefore(fimExpediente)) {
            boolean jaPassou = d.equals(LocalDate.now()) && horarioLoopAtual.isBefore(LocalTime.now());
            if (!jaPassou)
                if (medico.isHorarioLivre(d, horarioLoopAtual)) {
                    comboHorarios.addItem(horarioLoopAtual);
                }

            horarioLoopAtual = horarioLoopAtual.plusMinutes(duracaoConsulta);
        }
        comboHorarios.setEnabled(comboHorarios.getItemCount() > 0);
    }

}
