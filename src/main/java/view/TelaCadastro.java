package view;

import excessoes.UsuarioJaExistente;
import sistema.Hospital;
import usuario.Especialidade;
import usuario.TipoUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
    private JComboBox<Especialidade> comboEspec = new JComboBox<>(Especialidade.values());
    TipoUsuario tipoSelecionado;
    JButton salvar;

    public TelaCadastro(Hospital hospital, JFrame telaLogin) {

        salvar = new JButton("Finalizar Cadastro");
        salvar.addActionListener(e->{
            String nome = txtNome.getText();
            String cpf = txtCPF.getText();
            String senha = txtSenha.getText();
            String email = txtEmail.getText();

            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }
            if (senha.length() <= 3 || cpf.length() != 11) {
                JOptionPane.showMessageDialog(this, "Senha deve ter no mínimo 4 digitos e CPF deve ter 11 dígitos!");
                return;
            }
            try {
                if (tipoSelecionado.equals(TipoUsuario.MEDICO)) {
                    String crm = txtCRM.getText();
                    if (crm.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "CRM é obrigatório para Médicos!");
                        return;
                    }
                    Especialidade esp = (Especialidade) comboEspec.getSelectedItem();
                    hospital.cadastrarMedico(nome, cpf, senha, email, crm, esp);
                }
                else if (tipoSelecionado.equals(TipoUsuario.PACIENTE)) {
                    String plano = txtPlano.getText();
                    if (plano.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Informe o plano de saúde!");
                        return;
                    }
                    hospital.cadastrarPaciente(nome, cpf, senha, email, plano);
                }
                else if (tipoSelecionado.equals(TipoUsuario.SECRETARIA)) {
                    String matricula = txtMatricula.getText();
                    if (matricula.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "A matrícula é obrigatória!");
                        return;
                    }
                    hospital.cadastrarSecretaria(nome, cpf, senha, email, matricula);
                }

                // SUCESSO: Se chegou aqui, não lançou exceção
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                this.dispose(); // Fecha esta tela e aciona o WindowListener para abrir o Login

            } catch (UsuarioJaExistente er) {
                JOptionPane.showMessageDialog(this, er.getMessage(), "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
