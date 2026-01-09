package view.eventos;

import sistema.Hospital;
import view.telasDeUsuario.telaPaciente.TelaCadastro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class abrirTelaCadastro implements ActionListener {
    private JFrame telaLogin;
    private Hospital hospital;

    public abrirTelaCadastro(JFrame telaLogin, Hospital hospital) {
        this.telaLogin = telaLogin;
        this.hospital = hospital;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. Oculta a tela de login
        telaLogin.setVisible(false);

        // 2. Abre a tela de cadastro passando a referência da tela de login
        TelaCadastro telaCadastro = new TelaCadastro(hospital, telaLogin);
        telaCadastro.setVisible(true);
    }
}
