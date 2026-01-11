package view;

import excessoes.HoraInvalida;
import excessoes.UsuarioJaExistente;
import sistema.Hospital;
import usuario.Especialidade;
import usuario.Medico;
import usuario.TipoUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

public class TelaCadastro extends JFrame {
    Hospital hospital;
    JFrame telaLogin;
    JPanel painelFormulario;
    JPanel painelSelecoes;
    ButtonGroup buttonGroup;
    JRadioButton cadastrarMedico;
    JRadioButton cadastrarPaciente;
    JRadioButton cadastrarSecretaria;
    private JTextField txtNome = new JTextField();
    private JTextField txtCPF = new JTextField();
    private JTextField txtSenha = new JTextField();
    private JTextField txtEmail = new JTextField();
    private JTextField txtCRM = new JTextField();
    private JTextField txtPlano = new JTextField();
    private JTextField txtMatricula = new JTextField();
    private JTextField inicioExpediente = new JTextField();
    private JTextField fimExpediente = new JTextField();
    private JComboBox<Especialidade> comboEspec = new JComboBox<>(Especialidade.values());
    private JTextField txtDuracao = new JTextField();
    TipoUsuario tipoSelecionado;
    JButton salvar;

    public TelaCadastro(Hospital hospital, JFrame telaLogin) {

        salvar = new JButton("Finalizar Cadastro");
        salvar.addActionListener(e -> {
            String nome = txtNome.getText();
            String cpf = txtCPF.getText();
            String senha = txtSenha.getText();
            String email = txtEmail.getText();

            // 1. Validação básica
            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos básicos!");
                return;
            }
            if (senha.length() < 4 || cpf.length() != 11) {
                JOptionPane.showMessageDialog(this, "Senha deve ter no mínimo 4 digitos e CPF deve ter 11 dígitos!");
                return;
            }

            try {
                // 2. Lógica separada por tipo
                if (tipoSelecionado.equals(TipoUsuario.MEDICO)) {

                    // --- VALIDAÇÕES E LEITURAS EXCLUSIVAS DE MÉDICO ---
                    String crm = txtCRM.getText();
                    String txtDuracaoStr = txtDuracao.getText();
                    String txtInicio = inicioExpediente.getText();
                    String txtFim = fimExpediente.getText();

                    if (crm.isEmpty() || txtDuracaoStr.isEmpty() || txtInicio.isEmpty() || txtFim.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Para médicos, preencha CRM, Duração e Horários!");
                        return;
                    }

                    // Agora é seguro converter, pois sabemos que não está vazio
                    int duracaoDasConsultas = Integer.parseInt(txtDuracaoStr);
                    Especialidade esp = (Especialidade) comboEspec.getSelectedItem();

                    // Tratamento seguro do horário
                    if (!txtInicio.contains(":") || !txtFim.contains(":")) {
                        JOptionPane.showMessageDialog(this, "Use o formato HH:MM para os horários (ex: 08:00)");
                        return;
                    }

                    String[] horaInicioF = txtInicio.split(":");
                    String[] horaFimF = txtFim.split(":");

                    int horaInicio = Integer.parseInt(horaInicioF[0]);
                    int minutoInicio = Integer.parseInt(horaInicioF[1]);
                    int horaFim = Integer.parseInt(horaFimF[0]);
                    int minutoFim = Integer.parseInt(horaFimF[1]);

                    LocalTime inicio = LocalTime.of(horaInicio, minutoInicio);
                    LocalTime fim = LocalTime.of(horaFim, minutoFim);

                    Map<DayOfWeek, Medico.HorarioExpediente> mapaHorarios = new HashMap<>();
                    Medico.HorarioExpediente horarioPadrao = new Medico.HorarioExpediente(inicio, fim); // Usa o
                                                                                                        // construtor
                                                                                                        // cheio

                    // Adiciona dias úteis
                    mapaHorarios.put(DayOfWeek.MONDAY, horarioPadrao);
                    mapaHorarios.put(DayOfWeek.TUESDAY, horarioPadrao);
                    mapaHorarios.put(DayOfWeek.WEDNESDAY, horarioPadrao);
                    mapaHorarios.put(DayOfWeek.THURSDAY, horarioPadrao);
                    mapaHorarios.put(DayOfWeek.FRIDAY, horarioPadrao);

                    hospital.cadastrarMedico(nome, cpf, senha, email, crm, esp, mapaHorarios, duracaoDasConsultas);

                } else if (tipoSelecionado.equals(TipoUsuario.PACIENTE)) {
                    String plano = txtPlano.getText();
                    if (plano.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Informe o plano de saúde!");
                        return;
                    }
                    hospital.cadastrarPaciente(nome, cpf, senha, email, plano);

                } else if (tipoSelecionado.equals(TipoUsuario.SECRETARIA)) {
                    String matricula = txtMatricula.getText();
                    if (matricula.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "A matrícula é obrigatória!");
                        return;
                    }
                    hospital.cadastrarSecretaria(nome, cpf, senha, email, matricula);
                }

                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                this.dispose();

            } catch (UsuarioJaExistente er) {
                JOptionPane.showMessageDialog(this, er.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException er) {
                JOptionPane.showMessageDialog(this,
                        "Verifique se digitou números válidos nos campos de Duração ou Horário.", "Erro de Formato",
                        JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeException | HoraInvalida er) {
                JOptionPane.showMessageDialog(this, "Horário inválido (ex: 25:00 ou minutos > 59).", "Erro de Hora",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace(); // Ajuda a ver erros no console
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        this.hospital = hospital;
        this.telaLogin = telaLogin;

        setTitle("Cadastro de Usuário");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Quando esta tela fechar, a de login reaparece
                telaLogin.setVisible(true);
            }
        });

        painelSelecoes = new JPanel();
        buttonGroup = new ButtonGroup();
        cadastrarMedico = new JRadioButton("Medico");
        cadastrarPaciente = new JRadioButton("Paciente");
        cadastrarSecretaria = new JRadioButton("Secretaria");
        buttonGroup.add(cadastrarMedico);
        buttonGroup.add(cadastrarPaciente);
        buttonGroup.add(cadastrarSecretaria);
        cadastrarMedico.setSelected(true);
        painelSelecoes.add(cadastrarMedico);
        painelSelecoes.add(cadastrarPaciente);
        painelSelecoes.add(cadastrarSecretaria);
        add(painelSelecoes, BorderLayout.NORTH);

        painelFormulario = new JPanel();
        add(painelFormulario, BorderLayout.CENTER);

        ActionListener trocaCampos = e -> atualizarCampos();
        cadastrarMedico.addActionListener(trocaCampos);
        cadastrarPaciente.addActionListener(trocaCampos);
        cadastrarSecretaria.addActionListener(trocaCampos);

        cadastrarMedico.setSelected(true);
        atualizarCampos();
    }

    private void atualizarCampos() {
        painelFormulario.removeAll(); // Limpa tudo o que tinha antes
        painelFormulario.setLayout(new GridLayout(0, 2, 5, 5));

        // Campos Comuns a todos (Nome, CPF, Senha)
        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(txtNome);
        painelFormulario.add(new JLabel("CPF:"));
        painelFormulario.add(txtCPF);
        painelFormulario.add(new JLabel("Senha:"));
        painelFormulario.add(txtSenha);
        painelFormulario.add(new JLabel("Email:"));
        painelFormulario.add(txtEmail);

        // Campos Específicos
        if (cadastrarMedico.isSelected()) {
            painelFormulario.add(new JLabel("CRM:"));
            painelFormulario.add(txtCRM);
            painelFormulario.add(new JLabel("Especialidade:"));
            painelFormulario.add(comboEspec);
            painelFormulario.add(new JLabel("Inicio Expediente (HH:MM):"));
            painelFormulario.add(inicioExpediente);
            painelFormulario.add(new JLabel("Fim Expediente (HH:MM):"));
            painelFormulario.add(fimExpediente);
            painelFormulario.add(new JLabel("Duração consulta (minutos): "));
            painelFormulario.add(txtDuracao);
            tipoSelecionado = TipoUsuario.MEDICO;
        } else if (cadastrarPaciente.isSelected()) {
            painelFormulario.add(new JLabel("Plano de Saúde:"));
            painelFormulario.add(txtPlano);
            tipoSelecionado = TipoUsuario.PACIENTE;
        } else if (cadastrarSecretaria.isSelected()) {
            painelFormulario.add(new JLabel("Matrícula:"));
            painelFormulario.add(txtMatricula);
            tipoSelecionado = TipoUsuario.SECRETARIA;
        }

        // Adiciona botão no fim
        painelFormulario.add(new JLabel("")); // Espaço vazio para alinhar
        painelFormulario.add(salvar);

        // Atualiza a interface gráfica
        painelFormulario.revalidate();
        painelFormulario.repaint();
    }

    protected String getNome() {
        return txtNome.getText();
    }

    protected String getCPF() {
        return txtCPF.getText();
    }

}
