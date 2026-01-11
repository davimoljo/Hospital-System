package view.telasDeUsuario.recepcionista;

import usuario.Medico;
import usuario.userDB.RepositorioDeUsuario;
import usuario.Especialidade;
import sistema.Hospital; // Ajuste o import conforme seu pacote

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class TelaEditarMedico extends JFrame {

    private Medico medico;
    private Hospital hospital;

    private Map<DayOfWeek, JTextField[]> camposHorarios = new HashMap<>();

    private JTextField txtNome;
    private JComboBox<Especialidade> comboEspecialidade;

    public TelaEditarMedico(Medico medico, Hospital hospital) {
        this.medico = medico;
        this.hospital = hospital;

        setTitle("Editar Médico: " + medico.getNome());
        setSize(500, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelDados = new JPanel(new GridLayout(3, 2, 5, 5));
        painelDados.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtNome = new JTextField(medico.getNome());
        JTextField txtCrm = new JTextField(medico.getCrm());
        txtCrm.setEditable(false);

        comboEspecialidade = new JComboBox<>(Especialidade.values());
        comboEspecialidade.setSelectedItem(medico.getEspecialidade());

        painelDados.add(new JLabel("Nome:"));
        painelDados.add(txtNome);
        painelDados.add(new JLabel("CRM:"));
        painelDados.add(txtCrm);
        painelDados.add(new JLabel("Especialidade:"));
        painelDados.add(comboEspecialidade);

        JPanel painelHorarios = new JPanel(new GridBagLayout());
        painelHorarios.setBorder(BorderFactory.createTitledBorder("Agenda Semanal (Deixe em branco para folga)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.gridx = 0;
        painelHorarios.add(new JLabel("Dia"), gbc);
        gbc.gridx = 1;
        painelHorarios.add(new JLabel("Início (HH:MM)"), gbc);
        gbc.gridx = 2;
        painelHorarios.add(new JLabel("Fim (HH:MM)"), gbc);

        int linha = 1;
        for (DayOfWeek dia : DayOfWeek.values()) {
            gbc.gridy = linha;
            gbc.gridx = 0;
            painelHorarios.add(new JLabel(traduzirDia(dia)), gbc);

            String horaInicio = "";
            String horaFim = "";

            if (medico.dentroDoExpediente(LocalTime.of(12, 0), dia) || medico.getHoraInicioExpediente(dia) != null) {
                LocalTime inicio = medico.getHoraInicioExpediente(dia);
                LocalTime fim = medico.getHoraFimExpediente(dia);

                if (inicio != null)
                    horaInicio = inicio.toString();
                if (fim != null)
                    horaFim = fim.toString();
            }

            JTextField txtInicio = new JTextField(horaInicio, 5);
            JTextField txtFim = new JTextField(horaFim, 5);

            gbc.gridx = 1;
            painelHorarios.add(txtInicio, gbc);
            gbc.gridx = 2;
            painelHorarios.add(txtFim, gbc);

            camposHorarios.put(dia, new JTextField[] { txtInicio, txtFim });
            linha++;
        }
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar Alterações");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.setBackground(new Color(34, 139, 34)); // Verde
        btnSalvar.setForeground(Color.WHITE);

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        btnCancelar.addActionListener(e -> dispose());

        btnSalvar.addActionListener(e -> salvarAlteracoes());

        add(painelDados, BorderLayout.NORTH);
        add(new JScrollPane(painelHorarios), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void salvarAlteracoes() {
        try {
            medico.setNome(txtNome.getText());
            medico.setEspecialidade((Especialidade) comboEspecialidade.getSelectedItem());

            Map<DayOfWeek, Medico.HorarioExpediente> novoExpediente = new HashMap<>();

            for (DayOfWeek dia : DayOfWeek.values()) {
                JTextField[] campos = camposHorarios.get(dia);
                String sInicio = campos[0].getText().trim();
                String sFim = campos[1].getText().trim();

                if (!sInicio.isEmpty() && !sFim.isEmpty()) {
                    LocalTime hInicio = LocalTime.parse(sInicio);
                    LocalTime hFim = LocalTime.parse(sFim);

                    if (hInicio.isAfter(hFim)) {
                        throw new Exception("No dia " + dia + ", o início é depois do fim.");
                    }

                    novoExpediente.put(dia, new Medico.HorarioExpediente(hInicio, hFim));
                }
                // Se estiver vazio, simplesmente não adiciona (o médico não trabalha nesse dia)
            }

            medico.setExpediente(novoExpediente);

            RepositorioDeUsuario.registrarUsuarios(hospital.getUsuarios());

            JOptionPane.showMessageDialog(this, "Médico atualizado com sucesso!");
            dispose();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato de hora. Use HH:MM (ex: 08:30).");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }

    }

    private String traduzirDia(DayOfWeek dia) {
        switch (dia) {
            case MONDAY:
                return "Segunda-feira";
            case TUESDAY:
                return "Terça-feira";
            case WEDNESDAY:
                return "Quarta-feira";
            case THURSDAY:
                return "Quinta-feira";
            case FRIDAY:
                return "Sexta-feira";
            case SATURDAY:
                return "Sábado";
            case SUNDAY:
                return "Domingo";
            default:
                return dia.name();
        }
    }
}