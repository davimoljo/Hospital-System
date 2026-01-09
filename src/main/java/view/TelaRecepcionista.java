package view;

import sistema.Hospital;
import usuario.Medico;
import usuario.Paciente;
import usuario.Usuario;
import usuario.Especialidade;
import usuario.validacoes.SecretariaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.*;

public class TelaRecepcionista extends JFrame {

    private final Hospital hospital;

    public TelaRecepcionista(Hospital hospital) {
        this.hospital = hospital;

        // Configuração do Container Principal (JFrame)
        setTitle("Sistema Hospitalar - Recepção");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela

        // Gerenciador de Abas (JTabbedPane) para alternar entre funcionalidades
        JTabbedPane abas = new JTabbedPane();

        abas.addTab("Cadastro de Paciente", criarPainelCadastroPaciente());
        abas.addTab("Gestão Médica", criarPainelGestaoMedicos());
        abas.addTab("Agendamento", criarPainelAgendamento());
        abas.addTab("Monitoramento & Visitas", criarPainelMonitoramento());

        // Adiciona as abas ao painel de conteúdo padrão da janela
        add(abas);
    }

    // Cria o formulário de cadastro de pacientes.
    // Utiliza GridLayout para alinhar rótulos e campos de texto.

    private JPanel criarPainelCadastroPaciente() {
        JPanel painelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));

        JTextField txtNome = new JTextField();
        JTextField txtCpf = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        JTextField txtConvenio = new JTextField();
        JButton btnSalvar = new JButton("Salvar Paciente");

        painelFormulario.add(new JLabel("Nome Completo:"));
        painelFormulario.add(txtNome);
        painelFormulario.add(new JLabel("CPF:"));
        painelFormulario.add(txtCpf);
        painelFormulario.add(new JLabel("Email:"));
        painelFormulario.add(txtEmail);
        painelFormulario.add(new JLabel("Senha:"));
        painelFormulario.add(txtSenha);
        painelFormulario.add(new JLabel("Convênio:"));
        painelFormulario.add(txtConvenio);
        painelFormulario.add(new JLabel("")); // Espaçador
        painelFormulario.add(btnSalvar);

        // Tratamento de evento do botão Salvar
        btnSalvar.addActionListener(e -> {
            try {
                SecretariaService.cadastrarPaciente(
                        hospital,
                        txtNome.getText(), txtCpf.getText(),
                        new String(txtSenha.getPassword()),
                        txtEmail.getText(), txtConvenio.getText());
                JOptionPane.showMessageDialog(null, "Paciente cadastrado com sucesso.");
                limparCampos(painelFormulario);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro no cadastro: " + ex.getMessage());
            }
        });

        // Painel wrapper para aplicar margens (BorderLayout)
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelPrincipal.add(painelFormulario, BorderLayout.NORTH);

        return painelPrincipal;
    }

    // Cria o formulário de gestão de médicos.
    // Inclui campos de Horário exigidos pelo método cadastrarMedico do Hospital.

    private JPanel criarPainelGestaoMedicos() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // Área de Cadastro (Superior)
        JPanel painelCadastro = new JPanel(new GridLayout(7, 2, 5, 5));
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Novo Médico"));

        JTextField txtNome = new JTextField();
        JTextField txtCpf = new JTextField();
        JTextField txtCrm = new JTextField();
        JComboBox<Especialidade> comboEspecialidade = new JComboBox<>(Especialidade.values());

        // Novos campos para atender à assinatura do método no Hospital
        JTextField txtInicio = new JTextField("08:00");
        JTextField txtFim = new JTextField("18:00");

        JButton btnCadastrar = new JButton("Cadastrar Médico");

        painelCadastro.add(new JLabel("Nome:"));
        painelCadastro.add(txtNome);
        painelCadastro.add(new JLabel("CPF:"));
        painelCadastro.add(txtCpf);
        painelCadastro.add(new JLabel("CRM:"));
        painelCadastro.add(txtCrm);
        painelCadastro.add(new JLabel("Especialidade:"));
        painelCadastro.add(comboEspecialidade);
        painelCadastro.add(new JLabel("Início Expediente (HH:MM):"));
        painelCadastro.add(txtInicio);
        painelCadastro.add(new JLabel("Fim Expediente (HH:MM):"));
        painelCadastro.add(txtFim);
        painelCadastro.add(new JLabel(""));
        painelCadastro.add(btnCadastrar);

        btnCadastrar.addActionListener(e -> {
            try {
                // Parse manual das horas (Formato HH:MM)
                String[] partesInicio = txtInicio.getText().split(":");
                String[] partesFim = txtFim.getText().split(":");

                LocalTime hInicio = LocalTime.of(Integer.parseInt(partesInicio[0]), Integer.parseInt(partesInicio[1]));
                LocalTime hFim = LocalTime.of(Integer.parseInt(partesFim[0]), Integer.parseInt(partesFim[1]));

                // Chamada com os 8 argumentos obrigatórios
                hospital.cadastrarMedico(
                        txtNome.getText(), txtCpf.getText(),
                        "senha123", "medico@hospital.com", // Dados padrão
                        txtCrm.getText(),
                        (Especialidade) comboEspecialidade.getSelectedItem(),
                        hInicio, hFim);
                JOptionPane.showMessageDialog(null, "Médico cadastrado com sucesso.");
                limparCampos(painelCadastro);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro: Verifique o formato das horas (HH:MM).");
            }
        });

        // Área de Status (Inferior)
        JPanel painelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelStatus.setBorder(BorderFactory.createTitledBorder("Alterar Status"));

        JTextField txtBuscaCrm = new JTextField(15);
        JButton btnAlterarStatus = new JButton("Ativar/Desativar");

        painelStatus.add(new JLabel("CRM:"));
        painelStatus.add(txtBuscaCrm);
        painelStatus.add(btnAlterarStatus);

        btnAlterarStatus.addActionListener(e -> {
            boolean encontrado = false;
            for (Usuario u : hospital.getUsuarios()) {
                if (u instanceof Medico && ((Medico) u).getCrm().equals(txtBuscaCrm.getText())) {
                    Medico m = (Medico) u;
                    m.setAtivo(!m.isAtivo());
                    JOptionPane.showMessageDialog(null,
                            "Status do Dr. " + m.getNome() + ": " + (m.isAtivo() ? "ATIVO" : "INATIVO"));
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado)
                JOptionPane.showMessageDialog(null, "CRM não encontrado.");
        });

        painelPrincipal.add(painelCadastro, BorderLayout.NORTH);
        painelPrincipal.add(painelStatus, BorderLayout.CENTER);

        return painelPrincipal;
    }

    // Interface de Agendamento.
    // Delega a validação e execução para o SecretariaService.

    private JPanel criarPainelAgendamento() {
        JPanel painel = new JPanel(new GridLayout(5, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtCpfPac = new JTextField();
        JTextField txtCrmMed = new JTextField();
        JTextField txtData = new JTextField("DD/MM/AAAA");
        JTextField txtHora = new JTextField("HH:MM");
        JButton btnAgendar = new JButton("Confirmar Agendamento");

        painel.add(new JLabel("CPF Paciente:"));
        painel.add(txtCpfPac);
        painel.add(new JLabel("CRM Médico:"));
        painel.add(txtCrmMed);
        painel.add(new JLabel("Data:"));
        painel.add(txtData);
        painel.add(new JLabel("Hora:"));
        painel.add(txtHora);
        painel.add(new JLabel(""));
        painel.add(btnAgendar);

        btnAgendar.addActionListener(e -> {
            try {
                String msg = SecretariaService.agendarConsulta(
                        hospital,
                        txtCpfPac.getText(), txtCrmMed.getText(),
                        txtData.getText(), txtHora.getText());
                JOptionPane.showMessageDialog(null, msg);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(painel, BorderLayout.NORTH);
        return wrapper;
    }

    // Painel de Monitoramento e Visitas.
    // Utiliza JTextArea dentro de JScrollPane para listagem de dados.

    private JPanel criarPainelMonitoramento() {
        JPanel painel = new JPanel(new BorderLayout());

        JTextArea areaLog = new JTextArea();
        areaLog.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaLog);

        JPanel painelControles = new JPanel(new FlowLayout());
        JTextField txtCpfVisita = new JTextField(12);
        JButton btnVisita = new JButton("Verificar Visita");
        JButton btnListar = new JButton("Médicos Disponíveis");

        painelControles.add(new JLabel("CPF Paciente:"));
        painelControles.add(txtCpfVisita);
        painelControles.add(btnVisita);
        painelControles.add(btnListar);

        btnVisita.addActionListener(e -> {
            Usuario u = hospital.procurarUsuarioPorCPF(txtCpfVisita.getText());
            if (u instanceof Paciente) {
                Paciente p = (Paciente) u;
                // Simulação de regra: Permitido se tiver prontuário (internado)
                if (p.getProntuario() != null) {
                    areaLog.setText(">> VISITA AUTORIZADA: " + p.getNome());
                } else {
                    areaLog.setText(">> VISITA NEGADA: Paciente sem internação ativa.");
                }
            } else {
                areaLog.setText("Erro: Paciente não localizado.");
            }
        });

        btnListar.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(" MÉDICOS ATIVOS NO SISTEMA \n");
            for (Usuario u : hospital.getUsuarios()) {
                if (u instanceof Medico) {
                    Medico m = (Medico) u;
                    if (m.isAtivo()) {
                        sb.append(m.getNome()).append(" (").append(m.getEspecialidade()).append(")\n");
                    }
                }
            }
            areaLog.setText(sb.toString());
        });

        painel.add(painelControles, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    // Utilitário para limpar campos de texto após submissão
    private void limparCampos(JPanel painel) {
        for (Component c : painel.getComponents()) {
            if (c instanceof JTextField) {
                ((JTextField) c).setText("");
            }
        }
    }
}