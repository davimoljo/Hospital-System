package view;

import sistema.Hospital;
import view.eventos.Logar;
import view.eventos.abrirTelaCadastro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaLogin extends JFrame {
    private JTextField campoCPF;
    private JTextField campoSenha;
    private JButton botaoLogin;
    private JButton botaoNovoUsuario;
    private Hospital hospital;

    public TelaLogin(Hospital hospital) {
        this.hospital = hospital;

        // Configurações básicas da janela
        setTitle("Sistema Hospitalar - " + hospital.getNomeHospital());
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a tela
        setLayout(new BorderLayout());

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                hospital.fechar();
                System.exit(0);
            }
        });

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        painelCampos.add(new JLabel("CPF:"));
        campoCPF = new JTextField();
        painelCampos.add(campoCPF);

        painelCampos.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        painelCampos.add(campoSenha);

        // Painel do botão
        JPanel painelBotao = new JPanel();
        painelBotao.setLayout(new FlowLayout(FlowLayout.CENTER));
        botaoLogin = new JButton("Entrar");
        botaoNovoUsuario = new JButton("Novo Usuário");
        painelBotao.add(botaoLogin);
        painelBotao.add(botaoNovoUsuario);

        add(painelCampos, BorderLayout.NORTH);
        add(painelBotao, BorderLayout.SOUTH);

        botaoLogin.addActionListener(new Logar(this, hospital));
        botaoNovoUsuario.addActionListener(new abrirTelaCadastro(this, this.hospital));
    };

    public String getCPFDigitado() {
        return campoCPF.getText();
    }

    public String getSenhaDigitada() {
        return campoSenha.getText();
    }
}
