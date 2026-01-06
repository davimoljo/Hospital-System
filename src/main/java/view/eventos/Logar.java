package view.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import excessoes.SenhaIncorretaException;
import excessoes.UsuarioInexistente;
import excessoes.UsuarioJaExistente;
import sistema.Hospital;
import usuario.*;
import usuario.validacoes.*;
import view.TelaLogin;

public class Logar implements ActionListener {
    private TelaLogin tela;

    private Hospital hospital;

    public Logar(TelaLogin tela, Hospital hospital) {
        this.tela = tela;
        this.hospital = hospital;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cpfRecebido = tela.getCPFDigitado();
        String senhaRecebida = tela.getSenhaDigitada();
        if (cpfRecebido.isEmpty() && senhaRecebida.isEmpty()) {
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
                Usuario usuarioLogado = LoginService.validarUsuario(hospital.getUsuarios(), cpfRecebido, senhaRecebida);

                JOptionPane.showMessageDialog(tela, "Bem-vindo(a), " + usuarioLogado.getNome());
                if (usuarioLogado instanceof Medico medico) {
                    // TODO: Criar tela do medico
                } else if (usuarioLogado instanceof Paciente paciente) {
                    // TODO: Criar tela do paciente
                }

                else if (usuarioLogado instanceof Secretaria secretaria) {
                    // TODO: Criar tela da secretaria
                }

            } catch (SenhaIncorretaException error) {
                JOptionPane.showMessageDialog(tela, "Senha incorreta!", "Erro de autenticação",
                        JOptionPane.WARNING_MESSAGE);

            } catch (UsuarioInexistente error) {
                JOptionPane.showMessageDialog(tela, "CPF fornecido não corresponde a nenhum usuário",
                        "Erro de autenticação", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
