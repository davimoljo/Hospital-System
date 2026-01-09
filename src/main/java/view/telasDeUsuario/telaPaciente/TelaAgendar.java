package view.telasDeUsuario.telaPaciente;

import excessoes.DataInvalida;
import excessoes.HoraInvalida;
import sistema.Consulta;
import sistema.Hospital;
import usuario.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class TelaAgendar extends JFrame {
    private JTextField data, horario;
    private JComboBox<Especialidade> especialidadeModel;
    private DefaultListModel<Medico> modelMedico = new DefaultListModel<Medico>();
    private JList<Medico> listaMedicosDisponiveis;

    private JButton botaoAgendar, procurarMedicos;
    private JPanel painelEsquerdo, painelDireito, painelBotoes;
    private Hospital hospital;
    private Paciente paciente;
    private TelaPaciente telaPaciente;
    LocalDate d;
    LocalTime h;

    public TelaAgendar(Hospital hospital, Paciente paciente, TelaPaciente telaPaciente) {
        this.hospital = hospital;
        this.paciente = paciente;
        this.telaPaciente = telaPaciente;
        setTitle("Agendar Nova Consulta");
        setSize(600, 450); // Aumentado para acomodar melhor os componentes
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(telaPaciente);

        // Cores e Fontes (seguindo o padrão da TelaPaciente)
        Color corFundo = new Color(245, 245, 245);
        Font fonteLabel = new Font("Segue UI", Font.BOLD, 14);

        // --- PAINEL ESQUERDO (Configurações) ---
        painelEsquerdo = new JPanel(new GridBagLayout());
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Detalhes da Consulta"));
        painelEsquerdo.setPreferredSize(new Dimension(250, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        data = new JTextField(10);
        horario = new JTextField(10);
        especialidadeModel = new JComboBox<>(Especialidade.values());

        // Adicionando componentes com Labels
        gbc.gridy = 0;
        painelEsquerdo.add(new JLabel("Data (AAAA/MM/DD):"), gbc);
        gbc.gridy = 1;
        painelEsquerdo.add(data, gbc);
        gbc.gridy = 2;
        painelEsquerdo.add(new JLabel("Horário (HH:MM):"), gbc);
        gbc.gridy = 3;
        painelEsquerdo.add(horario, gbc);
        gbc.gridy = 4;
        painelEsquerdo.add(new JLabel("Especialidade:"), gbc);
        gbc.gridy = 5;
        painelEsquerdo.add(especialidadeModel, gbc);

        procurarMedicos = new JButton("Procurar Médicos");
        procurarMedicos.addActionListener(new procurarMedicoEvento(this));
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 5, 10);
        painelEsquerdo.add(procurarMedicos, gbc);

        // --- PAINEL DIREITO (Seleção de Médico) ---
        painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Médicos Disponíveis"));

        listaMedicosDisponiveis = new JList<>(modelMedico);
        modelMedico.clear();
        listaMedicosDisponiveis.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollMedicos = new JScrollPane(listaMedicosDisponiveis);
        painelDireito.add(scrollMedicos, BorderLayout.CENTER);

        // --- PAINEL INFERIOR (Ações) ---
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

        // --- ADICIONANDO AO FRAME PRINCIPAL ---
        JPanel painelCentral = new JPanel(new GridLayout(1, 2, 10, 0));
        painelCentral.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelCentral.add(painelEsquerdo);
        painelCentral.add(painelDireito);

        this.add(painelCentral, BorderLayout.CENTER);
        this.add(painelBotoes, BorderLayout.SOUTH);

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
            String[] dataF = data.getText().split("/");
            String[] horarioF = horario.getText().split(":");
            Integer[] dataI = new Integer[3];
            Integer[] horarioI = new Integer[2];
            Especialidade esp = (Especialidade) especialidadeModel.getSelectedItem();
            if (dataF.length != 3 && horarioF.length != 2) {
                JOptionPane.showMessageDialog(telaAgendar, "Formato Data/Hora inválido", "Erro de formato",
                        JOptionPane.ERROR_MESSAGE);
            }
            try {
                int i = 0;
                for (String s : dataF) {
                    dataI[i++] = Integer.parseInt(s);
                }
                i = 0;
                for (String s : horarioF) {
                    horarioI[i++] = Integer.parseInt(s);
                }
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(telaAgendar, "Data/Hora inválido", "Erro de formato",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                h = LocalTime.of(horarioI[0], horarioI[1]);
                d = LocalDate.of(dataI[0], dataI[1], dataI[2]);
                if (LocalDate.now().isAfter(d) || (LocalDate.now().isEqual(d)) && LocalTime.now().isAfter(h)) {
                    JOptionPane.showMessageDialog(telaAgendar, "Impossível marcar uma consulta no passado!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (HoraInvalida | DataInvalida| DateTimeException invalida) {
                JOptionPane.showMessageDialog(telaAgendar, invalida.getMessage(), "Erro de formato",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Usuario usuario : hospital.getUsuarios()) {
                if (usuario instanceof Medico medico) {
                    if (medico.getEspecialidade().equals(esp) && medico.isDisponivel(h, d)) {
                        modelMedico.addElement(medico);
                    }
                }
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
            if (!listaMedicosDisponiveis.isSelectionEmpty()) {
                Medico medico = listaMedicosDisponiveis.getSelectedValue();
                Consulta consulta = hospital.marcarConsulta(paciente, medico, d, h);
                JOptionPane.showMessageDialog(this.telaAgendar, "Consulta marcada com sucesso!", "Consulta marcada",
                        JOptionPane.INFORMATION_MESSAGE);
                telaAgendar.setVisible(false);
                telaAgendar.telaPaciente.setVisible(true);
                telaAgendar.telaPaciente.atualizarTabelaConsultas();
            }
        }
    }

}
