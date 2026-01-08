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

public class TelaRecepcionista extends JFrame {

    private final Hospital hospital;

    public TelaRecepcionista(Hospital hospital) {
        this.hospital = hospital;

        // Configuração inicial da Janela
        setTitle("Módulo de Recepção");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Gerenciador de Abas para navegação entre funcionalidades
        JTabbedPane abas = new JTabbedPane();

        // Adição das abas funcionais
        abas.addTab("Cadastrar Paciente", criarPainelCadastroPaciente());
        abas.addTab("Gestão de Médicos", criarPainelGestaoMedicos());
        abas.addTab("Agendamento", criarPainelAgendamento());
        abas.addTab("Visitas & Monitoramento", criarPainelMonitoramento());

        add(abas);
    }


     // ABA 1: Formulário de Cadastro de Pacientes
     // Utiliza GridLayout para organização visual (Rótulo + Campo).

    private JPanel criarPainelCadastroPaciente() {
        JPanel painelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));

        // Componentes de entrada de dados
        JLabel lblNome = new JLabel("Nome Completo:");
        JTextField txtNome = new JTextField();

        JLabel lblCpf = new JLabel("CPF:");
        JTextField txtCpf = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        JPasswordField txtSenha = new JPasswordField();

        JLabel lblConvenio = new JLabel("Convênio:");
        JTextField txtConvenio = new JTextField();

        JButton btnSalvar = new JButton("Salvar Paciente");

        // Adição dos componentes ao painel
        painelFormulario.add(lblNome); painelFormulario.add(txtNome);
        painelFormulario.add(lblCpf); painelFormulario.add(txtCpf);
        painelFormulario.add(lblEmail); painelFormulario.add(txtEmail);
        painelFormulario.add(lblSenha); painelFormulario.add(txtSenha);
        painelFormulario.add(lblConvenio); painelFormulario.add(txtConvenio);
        painelFormulario.add(new JLabel("")); // Espaçador
        painelFormulario.add(btnSalvar);

        // Tratamento do evento de clique (ActionListener)
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Delega a validação e cadastro para a camada de Serviço
                    SecretariaService.cadastrarPaciente(
                            hospital,
                            txtNome.getText(),
                            txtCpf.getText(),
                            new String(txtSenha.getPassword()),
                            txtEmail.getText(),
                            txtConvenio.getText()
                    );
                    JOptionPane.showMessageDialog(null, "Paciente cadastrado com sucesso!");
                    limparCampos(painelFormulario);
                } catch (Exception erro) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + erro.getMessage());
                }
            }
        });

        // Painel wrapper para aplicar margens
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelPrincipal.add(painelFormulario, BorderLayout.NORTH);

        return painelPrincipal;
    }


     // ABA 2: Gestão do Corpo Clínico
     // Permite cadastro e alteração de status (Ativo/Inativo).

    private JPanel criarPainelGestaoMedicos() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // Sub-painel: Cadastro de novo médico
        JPanel painelCadastro = new JPanel(new GridLayout(5, 2, 5, 5));
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Novo Médico"));

        JTextField txtNome = new JTextField();
        JTextField txtCpf = new JTextField();
        JTextField txtCrm = new JTextField();
        JComboBox<Especialidade> comboEspecialidade = new JComboBox<>(Especialidade.values());
        JButton btnCadastrar = new JButton("Cadastrar");

        painelCadastro.add(new JLabel("Nome:")); painelCadastro.add(txtNome);
        painelCadastro.add(new JLabel("CPF:")); painelCadastro.add(txtCpf);
        painelCadastro.add(new JLabel("CRM:")); painelCadastro.add(txtCrm);
        painelCadastro.add(new JLabel("Especialidade:")); painelCadastro.add(comboEspecialidade);
        painelCadastro.add(new JLabel("")); painelCadastro.add(btnCadastrar);

        btnCadastrar.addActionListener(e -> {
            try {
                hospital.cadastrarMedico(
                        txtNome.getText(), txtCpf.getText(), "1234", "padrao@hospital.com",
                        txtCrm.getText(), (Especialidade) comboEspecialidade.getSelectedItem()
                );
                JOptionPane.showMessageDialog(null, "Médico cadastrado!");
                limparCampos(painelCadastro);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        // Sub-painel: Controle de Status
        JPanel painelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelStatus.setBorder(BorderFactory.createTitledBorder("Gerenciar Status"));

        JTextField txtBuscaCrm = new JTextField(15);
        JButton btnAlterarStatus = new JButton("Alternar Status (Ativo/Inativo)");

        painelStatus.add(new JLabel("CRM:"));
        painelStatus.add(txtBuscaCrm);
        painelStatus.add(btnAlterarStatus);

        btnAlterarStatus.addActionListener(e -> {
            boolean encontrado = false;
            // Busca linear simples por CRM
            for (Usuario u : hospital.getUsuarios()) {
                if (u instanceof Medico) {
                    Medico m = (Medico) u;
                    if (m.getCrm().equals(txtBuscaCrm.getText())) {
                        m.setAtivo(!m.isAtivo()); // Inverte o booleano
                        JOptionPane.showMessageDialog(null, "Status alterado para: " + (m.isAtivo() ? "ATIVO" : "INATIVO"));
                        encontrado = true;
                        break;
                    }
                }
            }
            if (!encontrado) JOptionPane.showMessageDialog(null, "Médico não encontrado.");
        });

        painelPrincipal.add(painelCadastro, BorderLayout.NORTH);
        painelPrincipal.add(painelStatus, BorderLayout.CENTER);

        return painelPrincipal;
    }


     //ABA 3: Agendamento de Consultas
     //Integração com SecretariaService para validação de regras de negócio.

    private JPanel criarPainelAgendamento() {
        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtCpfPac = new JTextField();
        JTextField txtCrmMed = new JTextField();
        JTextField txtData = new JTextField();
        JTextField txtHora = new JTextField();
        JButton btnAgendar = new JButton("Confirmar Agendamento");

        painelCampos.add(new JLabel("CPF Paciente:")); painelCampos.add(txtCpfPac);
        painelCampos.add(new JLabel("CRM Médico:")); painelCampos.add(txtCrmMed);
        painelCampos.add(new JLabel("Data (DD/MM/AAAA):")); painelCampos.add(txtData);
        painelCampos.add(new JLabel("Hora (HH:MM):")); painelCampos.add(txtHora);
        painelCampos.add(new JLabel("")); painelCampos.add(btnAgendar);

        btnAgendar.addActionListener(e -> {
            try {
                // Chama método estático de serviço para processar o agendamento
                String resultado = SecretariaService.agendarConsulta(
                        hospital,
                        txtCpfPac.getText(),
                        txtCrmMed.getText(),
                        txtData.getText(),
                        txtHora.getText()
                );
                JOptionPane.showMessageDialog(null, resultado);
            } catch (Exception ex) {
                // Exibe exceções de negócio (Data inválida, Médico ocupado, etc)
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(painelCampos, BorderLayout.NORTH);
        return wrapper;
    }


     //ABA 4: Visitas e Monitoramento
     //Exibe informações em área de texto (Log).

    private JPanel criarPainelMonitoramento() {
        JPanel painel = new JPanel(new BorderLayout());

        JTextArea areaLog = new JTextArea();
        areaLog.setEditable(false); // Impede edição manual
        JScrollPane scroll = new JScrollPane(areaLog); // Adiciona barra de rolagem

        JPanel painelControles = new JPanel(new FlowLayout());
        JTextField txtCpfVisita = new JTextField(12);
        JButton btnVisita = new JButton("Checar Visita");
        JButton btnDisponibilidade = new JButton("Listar Médicos Ativos");

        painelControles.add(new JLabel("CPF Paciente:"));
        painelControles.add(txtCpfVisita);
        painelControles.add(btnVisita);
        painelControles.add(btnDisponibilidade);

        // Lógica de verificação de visita
        btnVisita.addActionListener(e -> {
            Usuario u = hospital.procurarUsuarioPorCPF(txtCpfVisita.getText());
            if (u instanceof Paciente) {
                Paciente p = (Paciente) u;
                // Regra simulada: Paciente com prontuário pode receber visita
                if (p.getProntuario() != null) {
                    areaLog.setText("STATUS: Visita AUTORIZADA para " + p.getNome());
                } else {
                    areaLog.setText("STATUS: Visita NÃO AUTORIZADA (Sem internação ativa).");
                }
            } else {
                areaLog.setText("ERRO: Paciente não encontrado.");
            }
        });

        // Lógica de listagem de médicos
        btnDisponibilidade.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("=== CORPO CLÍNICO DISPONÍVEL ===\n");
            for (Usuario u : hospital.getUsuarios()) {
                if (u instanceof Medico) {
                    Medico m = (Medico) u;
                    if (m.isAtivo()) {
                        sb.append(m.getNome()).append(" - ").append(m.getEspecialidade()).append("\n");
                    }
                }
            }
            areaLog.setText(sb.toString());
        });

        painel.add(painelControles, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    // Método utilitário para limpar campos de texto de um painel
    private void limparCampos(JPanel painel) {
        for (Component c : painel.getComponents()) {
            if (c instanceof JTextField) {
                ((JTextField) c).setText("");
            }
        }
    }
}