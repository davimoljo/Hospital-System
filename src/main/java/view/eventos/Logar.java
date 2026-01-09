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
import view.*;
import view.telasDeUsuario.medico.TelaMedico;
import view.telasDeUsuario.TelaPaciente;

public class Logar implements ActionListener {
    private TelaLogin telaLogin;

    private Hospital hospital;

    public Logar(TelaLogin tela, Hospital hospital) {
        this.telaLogin = tela;
        this.hospital = hospital;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cpfRecebido = telaLogin.getCPFDigitado();
        String senhaRecebida = telaLogin.getSenhaDigitada();
        if (cpfRecebido.isEmpty() && senhaRecebida.isEmpty()) {
            JOptionPane.showMessageDialog(
                    telaLogin,
                    "CPF e Senha devem ser preenchidos!",
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
        } else if (!LoginService.autenticarEntradas(cpfRecebido, senhaRecebida)) {
            JOptionPane.showMessageDialog(
                    telaLogin,
                    "Uusuário ou senha inválidos!",
                    "Erro de autenticação.",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Usuario usuarioLogado = LoginService.validarUsuario(hospital.getUsuarios(), cpfRecebido, senhaRecebida); // TODO:
                                                                                                                         // Colocar
                // a lista de
                // usuarios nessa
                // função
                JOptionPane.showMessageDialog(telaLogin, "Bem-vindo(a), " + usuarioLogado.getNome());
                if (usuarioLogado instanceof Medico medico) {
                    TelaMedico telaMedico = new TelaMedico(hospital, medico, telaLogin);
                    telaLogin.setVisible(false);
                    telaMedico.setVisible(true);
                } else if (usuarioLogado instanceof Paciente paciente) {
                    // TODO: Criar tela do paciente
                    TelaPaciente telaPaciente = new TelaPaciente(paciente, telaLogin, hospital);
                    telaLogin.setVisible(false);
                    telaPaciente.setVisible(true);
                }

                else if (usuarioLogado instanceof Secretaria secretaria) {
                    // TODO: Criar tela da secretaria
                }

            } catch (SenhaIncorretaException error) {
                JOptionPane.showMessageDialog(telaLogin, "Senha incorreta!", "Erro de autenticação",
                        JOptionPane.WARNING_MESSAGE);

            } catch (UsuarioInexistente error) {
                JOptionPane.showMessageDialog(telaLogin, "CPF fornecido não corresponde a nenhum usuário",
                        "Erro de autenticação", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
