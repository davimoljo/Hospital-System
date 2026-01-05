package view.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import usuario.Usuario;
import usuario.validacoes.*;
import view.TelaLogin;

public class Logar implements ActionListener {
    private TelaLogin tela;
    private String cpfRecebido;
    private String senhaRecebida;

    public Logar(TelaLogin tela) {
        this.tela = tela;
        cpfRecebido = tela.getCPFDigitado();
        senhaRecebida = tela.getSenhaDigitada();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (cpfRecebido.length() == 0 && senhaRecebida.length() == 0) {
            JOptionPane.showMessageDialog(
                    tela,
                    "CPF e Senha devem ser preenchidos!",
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
        } else if (!LoginService.autenticarEntradas(cpfRecebido, senhaRecebida)) {
            JOptionPane.showMessageDialog(
                    tela,
                    "Uusuário ou senha inválidos!",
                    "Erro de autenticação.",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Usuario usuarioLogado = LoginService.validarUsuario(null, cpfRecebido, senhaRecebida); // TODO: Colocar
                                                                                                       // a lista de
                                                                                                       // usuarios nessa
                                                                                                       // função
                JOptionPane.showMessageDialog(tela, "Bem-vindo(a), " + usuarioLogado.getNome());
            } catch (Exception exc) {
                // TODO: handle exception
            }
        }
    }
}
