package view.telasDeUsuario.recepcionista;

import usuario.Medico;
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
    private Hospital hospital; // Caso precise salvar no banco/arquivo ao fechar

    // Mapeia o dia da semana para os campos de texto (Inicio e Fim) correspondentes
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

        // --- PAINEL SUPERIOR (Dados Básicos) ---
        JPanel painelDados = new JPanel(new GridLayout(3, 2, 5, 5));
        painelDados.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtNome = new JTextField(medico.getNome());
        // CRM geralmente não se edita, então mostramos como Label ou desabilitado
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

        // --- PAINEL CENTRAL (Horários) ---
        JPanel painelHorarios = new JPanel(new GridBagLayout());
        painelHorarios.setBorder(BorderFactory.createTitledBorder("Agenda Semanal (Deixe em branco para folga)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cabeçalho da Tabela
        gbc.gridy = 0;
        gbc.gridx = 0;
        painelHorarios.add(new JLabel("Dia"), gbc);
        gbc.gridx = 1;
        painelHorarios.add(new JLabel("Início (HH:MM)"), gbc);
        gbc.gridx = 2;
        painelHorarios.add(new JLabel("Fim (HH:MM)"), gbc);

        // Gera uma linha para cada dia da semana
        int linha = 1;
        for (DayOfWeek dia : DayOfWeek.values()) {
            gbc.gridy = linha;
            gbc.gridx = 0;
            painelHorarios.add(new JLabel(traduzirDia(dia)), gbc);

            // Tenta pegar o horário atual desse dia (se existir)
            String horaInicio = "";
            String horaFim = "";

            // Verifica se o médico trabalha nesse dia atualmente
            if (medico.dentroDoExpediente(LocalTime.of(12, 0), dia) || medico.getHoraInicioExpediente(dia) != null) {
                // Obs: O ideal é ter um getter direto do mapa, mas vamos usar o getter que
                // criamos
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

            // Guarda referência para salvar depois
            camposHorarios.put(dia, new JTextField[] { txtInicio, txtFim });
            linha++;
        }

        // --- PAINEL INFERIOR (Botões) ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar Alterações");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.setBackground(new Color(34, 139, 34)); // Verde
        btnSalvar.setForeground(Color.WHITE);

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        // --- AÇÕES ---
        btnCancelar.addActionListener(e -> dispose());

        btnSalvar.addActionListener(e -> salvarAlteracoes());

        // Adiciona tudo ao Frame
        add(painelDados, BorderLayout.NORTH);
        add(new JScrollPane(painelHorarios), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void salvarAlteracoes() {
        try {
            // 1. Atualiza dados básicos
            // (Assumindo que sua classe Medico tem setters para isso, se não tiver, pule)
            // medico.setNome(txtNome.getText());
            // medico.setEspecialidade((Especialidade)
            // comboEspecialidade.getSelectedItem());

            // 2. Atualiza Horários
            // Precisamos acessar o mapa do médico. O ideal seria um método
            // 'limparHorarios()' no Medico
            // ou setar um novo mapa. Vamos supor que você crie um método 'setExpediente' no
            // Medico.

            Map<DayOfWeek, Medico.HorarioExpediente> novoExpediente = new HashMap<>();

            for (DayOfWeek dia : DayOfWeek.values()) {
                JTextField[] campos = camposHorarios.get(dia);
                String sInicio = campos[0].getText().trim();
                String sFim = campos[1].getText().trim();

                // Se ambos os campos tiverem texto, adiciona ao mapa
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

            // ATENÇÃO: Você precisa adicionar este método 'setExpediente' na classe Medico
            medico.setExpediente(novoExpediente);

            JOptionPane.showMessageDialog(this, "Médico atualizado com sucesso!");
            dispose();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro de formato de hora. Use HH:MM (ex: 08:30).");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    // Utilitário simples para traduzir ENUM para Português
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