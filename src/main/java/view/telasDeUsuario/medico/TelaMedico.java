package view.telasDeUsuario.medico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import sistema.Consulta;
import sistema.Prontuario;

import java.awt.*;
import java.awt.event.*;

import usuario.Medico;
import usuario.Paciente;

import java.util.List;

public class TelaMedico extends JFrame {
    private Medico medico;
    private JTable tabelaAgenda;
    private List<Consulta> consultas;

    public TelaMedico(Medico medico) {
        this.medico = medico;
        setTitle("Sistema hospitalar - Médico");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOPO =====
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblMedico = new JLabel("Médico: " + medico.getNome());
        lblMedico.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblAlertas = new JLabel("🔔");
        lblAlertas.setToolTipText("Notificações");

        topo.add(lblMedico, BorderLayout.WEST);
        topo.add(lblAlertas, BorderLayout.EAST);

        // ===== ABAS =====
        JTabbedPane abas = new JTabbedPane();

        // ---- ABA AGENDA ----
        JPanel abaAgenda = new JPanel(new BorderLayout());
        DefaultTableModel modelAgenda = new DefaultTableModel(
                new String[] { "Hora", "Paciente" }, 0);
        tabelaAgenda = new JTable(modelAgenda);

        for (Consulta c : medico.getConsultasMarcadas()) {
            modelAgenda.addRow(new Object[] {
                    c.getHora().toString(),
                    c.getPaciente().getNome()
            });
        }

        JButton btnConfigAgenda = new JButton("Configurar agenda");
        btnConfigAgenda.setFocusPainted(false);
        abaAgenda.add(btnConfigAgenda, BorderLayout.SOUTH);

        // ---- ABA PACIENTE ----
        JPanel abaPaciente = new JPanel();
        abaPaciente.setLayout(new BoxLayout(abaPaciente, BoxLayout.Y_AXIS));
        abaPaciente.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnProntuario = new JButton("Prontuário");
        JButton btnHistorico = new JButton("Histórico clínico");
        JButton btnInternacao = new JButton("Status de internação");

        btnProntuario.setEnabled(false);
        btnHistorico.setEnabled(false);
        btnInternacao.setEnabled(false);

        abaPaciente.add(btnProntuario);
        abaPaciente.add(Box.createVerticalStrut(10));
        abaPaciente.add(btnHistorico);
        abaPaciente.add(Box.createVerticalStrut(10));
        abaPaciente.add(btnInternacao);

        // ---- ABA DOCUMENTOS ----
        JPanel abaDocumentos = new JPanel();
        abaDocumentos.setLayout(new BoxLayout(abaDocumentos, BoxLayout.Y_AXIS));
        abaDocumentos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAtestado = new JButton("Emitir atestado");
        JButton btnReceita = new JButton("Emitir receita");

        btnAtestado.setEnabled(false);
        btnReceita.setEnabled(false);

        abaDocumentos.add(btnAtestado);
        abaDocumentos.add(Box.createVerticalStrut(10));
        abaDocumentos.add(btnReceita);

        // ===== ADD ABAS =====
        abas.addTab("Agenda", abaAgenda);
        abas.addTab("Paciente", abaPaciente);
        abas.addTab("Documentos", abaDocumentos);

        // ===== FRAME =====
        add(topo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);

        tabelaAgenda.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean selecionado = tabelaAgenda.getSelectedRow() != -1;
                btnProntuario.setEnabled(selecionado);
                btnHistorico.setEnabled(selecionado);
                btnInternacao.setEnabled(selecionado);
                btnAtestado.setEnabled(selecionado);
                btnReceita.setEnabled(selecionado);
            }
        });

        btnProntuario.addActionListener(e -> {

            int linha = tabelaAgenda.getSelectedRow();

            if (linha != -1) {
                Consulta consulta = consultas.get(linha);
                Paciente paciente = consulta.getPaciente();

            }
        });

    }
}
