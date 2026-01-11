package view.telasDeUsuario.telaPaciente;

import usuario.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaConfiguracaoPaciente extends JFrame {
    JTextField campoEmail;
    JTextField campoNome;
    JTextField campoConvenio;
    JTextField campoTelefone;
    JTextField endereco;
    JPanel painelConfiguracao;
    Paciente paciente;
    JFrame telaPaciente;
    JButton botaoConfirmar;
    public TelaConfiguracaoPaciente(Paciente paciente, JFrame telaPaciente){
        setTitle("Configurações de Perfil");
        setSize(500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        this.telaPaciente = telaPaciente;
        this.paciente = paciente;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e){
                telaPaciente.setVisible(true);
            }
        });

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(Color.WHITE);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("Editar Seus Dados");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(33, 37, 41));
        JLabel subTitulo = new JLabel("Mantenha suas informações de contato atualizadas.");
        subTitulo.setFont(new Font("Segue UI", Font.PLAIN, 13));
        subTitulo.setForeground(Color.GRAY);

        topo.add(titulo, BorderLayout.NORTH);
        topo.add(subTitulo, BorderLayout.CENTER);
        topo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        int linhaAtual = 0;

        // Adicionando os campos manualmente para garantir o controle das linhas
        adicionarComponente(painelCampos, gbc, linhaAtual++, new JLabel("Nome Completo:"));
        adicionarComponente(painelCampos, gbc, linhaAtual++, campoNome = new JTextField(paciente.getNome()));

        adicionarComponente(painelCampos, gbc, linhaAtual++, new JLabel("E-mail:"));
        adicionarComponente(painelCampos, gbc, linhaAtual++, campoEmail = new JTextField(paciente.getEmail()));

        adicionarComponente(painelCampos, gbc, linhaAtual++, new JLabel("Telefone:"));
        adicionarComponente(painelCampos, gbc, linhaAtual++, campoTelefone = new JTextField(paciente.getTelefone()));

        adicionarComponente(painelCampos, gbc, linhaAtual++, new JLabel("Convênio:"));
        adicionarComponente(painelCampos, gbc, linhaAtual++, campoConvenio = new JTextField(paciente.getConvenio()));

        adicionarComponente(painelCampos, gbc, linhaAtual++, new JLabel("Endereço:"));
        adicionarComponente(painelCampos, gbc, linhaAtual++, endereco = new JTextField(paciente.getEndereco()));

        botaoConfirmar = new JButton("Salvar Alterações");
        botaoConfirmar.setFont(new Font("Segue UI", Font.BOLD, 14));
        botaoConfirmar.setForeground(Color.WHITE);
        botaoConfirmar.setBackground(new Color(0, 120, 215)); // Mesmo azul da segunda tela
        botaoConfirmar.setFocusPainted(false);
        botaoConfirmar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        botaoConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotao.setBackground(Color.WHITE);
        painelBotao.add(botaoConfirmar);

        // Adicionando ao Frame
        principal.add(topo, BorderLayout.NORTH);
        principal.add(painelCampos, BorderLayout.CENTER);
        principal.add(painelBotao, BorderLayout.SOUTH);

        add(principal);
        botaoConfirmar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarMudancas();
            }
        });
    }

    private void adicionarComponente(JPanel painel, GridBagConstraints gbc, int linha, JComponent comp) {
        gbc.gridy = linha;

        if (comp instanceof JLabel) {
            // Estilo para o Rótulo
            comp.setFont(new Font("Segoe UI", Font.BOLD, 13));
            gbc.insets = new Insets(10, 0, 2, 0); // Espaço em cima do label
        } else if (comp instanceof JTextField) {
            // Estilo para o Campo de Texto
            JTextField txt = (JTextField) comp;
            txt.setPreferredSize(new Dimension(0, 35));
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210, 210, 210)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            gbc.insets = new Insets(0, 0, 10, 0); // Espaço abaixo do campo
        }

        painel.add(comp, gbc);
    }

    private void confirmarMudancas(){
        paciente.setNome(campoNome.getText());
        paciente.setEmail(campoEmail.getText());
        paciente.setTelefone(campoTelefone.getText());
        paciente.setConvenio(campoConvenio.getText());
        paciente.setEndereco(endereco.getText());
        JOptionPane.showMessageDialog(this, "Yiippeeeee!");
        this.dispose();
        telaPaciente.setVisible(true);
    }
}


