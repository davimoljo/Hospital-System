package sistema;

import excessoes.DataIndisponivel;
import excessoes.MedicoDesativado;
import excessoes.ProntuarioJaExistente;
import excessoes.UsuarioJaExistente;
import sistema.documentos.Atestado;
import sistema.documentos.DocumentoMedico;
import sistema.documentos.Exame;
import sistema.documentos.Receita;
import usuario.*;
import usuario.userDB.RepositorioDeUsuario;
import utilitarios.*;
import utilitarios.Medicamento;

import java.util.*;
import java.time.*;

public class Hospital {
    protected final String nomeHospital = "HU (Hospital Universitário)";
    private List<Usuario> usuarios;
    private List<DocumentoMedico> documentos;
    private List<Prontuario> prontuarios;
    private List<Consulta> consultasMarcadas;

    public Hospital() {
        usuarios = RepositorioDeUsuario.carregarUsuarios();
        documentos = RegistraDocumento.leDocumentos();
        prontuarios = RegistraDocumento.leProntuarios();
        consultasMarcadas = RegistraDocumento.leConsultas();
        for (Usuario u : usuarios) {
            if (u instanceof Medico m) {
                m.getConsultasMarcadas().clear();
            } else if (u instanceof Paciente p) {
                p.getConsultasMarcadas().clear();
                p.getConsultasAnteriores().clear();
            }
        }

        for (Consulta c : consultasMarcadas) {
            Usuario medicoOficial = procurarUsuarioPorCPF(c.getCpfMedicoResponsavel());
            Usuario pacienteOficial = procurarUsuarioPorCPF(c.getCpfPaciente());
            c.setMedico((Medico) medicoOficial);
            c.setPaciente((Paciente) pacienteOficial);

            ((Medico) medicoOficial).arquivarConsulta(c);
            if (c.consultaJaRealizada())
                ((Paciente) pacienteOficial).addConsultaAnterior(c);
            else
                ((Paciente) pacienteOficial).agendarConsulta(c);
        }
    }

    public void fechar() {
        RegistraDocumento.registrarConsultas(consultasMarcadas);
        RegistraDocumento.registrarDocumentos(documentos);
        RegistraDocumento.registrarProntuarios(prontuarios);
        RepositorioDeUsuario.registrarUsuarios(usuarios);
    }

    private void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    private boolean usuarioExiste(String cpf) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                return true;
            }
        }

        return false;
    }

    public void cadastrarMedico(String nome, String cpf, String senha, String email, String crm,
            Especialidade especialidade, LocalTime inicioExpediente, LocalTime fimExpediente) {
        if (usuarioExiste(cpf)) {
            throw new UsuarioJaExistente("Usuário com cpf " + cpf + " já existe");
        }
        cadastrarUsuario(new Medico(nome, cpf, senha, email, crm, especialidade, inicioExpediente, fimExpediente));
    }

    public void cadastrarPaciente(String nome, String cpf, String senha, String email, String convenio) {
        if (usuarioExiste(cpf)) {
            throw new UsuarioJaExistente("Usuário com cpf " + cpf + " já existe");
        }
        cadastrarUsuario(new Paciente(nome, cpf, senha, email, convenio));
    }

    public void cadastrarSecretaria(String nome, String cpf, String senha, String email, String matricula) {
        if (usuarioExiste(cpf)) {
            throw new UsuarioJaExistente("Usuário com cpf " + cpf + " já existe");
        }
        cadastrarUsuario(new Secretaria(nome, cpf, senha, email, matricula));
    }

    public List<Usuario> procurarUsuariosPorCPF(String cpf) {
        List<Usuario> achados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().contains(cpf)) {
                achados.add(usuario);
            }
        }

        return achados;
    }

    public List<Usuario> procurarUsuariosPorNome(String nome) {
        List<Usuario> achados = new ArrayList<>();
        String nomeF = nome.toLowerCase();
        for (Usuario usuario : usuarios) {
            if (usuario.getNome().toLowerCase().contains(nomeF)) {
                achados.add(usuario);
            }
        }

        return achados;
    }

    public Paciente buscarPacientePorCPF(String cpf) {
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u instanceof Paciente)
                return (Paciente) u;
        }
        return null;
    }

    public Usuario procurarUsuarioPorCPF(String cpf) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                return usuario;
            }
        }
        return null;
    }

    public DocumentoMedico gerarAtestado(Paciente p, Medico m, LocalDate dataCriacao, int diaDeAfastamento) {
        DocumentoMedico atestado = new Atestado(p.getNome(), m.getNome(), p.getCpf(), m.getCpf(), dataCriacao, diaDeAfastamento);
        p.addDocumento(atestado);
        documentos.add(atestado);
        return atestado;
    }

    public DocumentoMedico gerarRececita(Paciente p, Medico m, LocalDate dataCriacao, String observacoes) {
        DocumentoMedico receita = new Receita(p.getNome(), m.getNome(), p.getCpf(), m.getCpf(), dataCriacao, observacoes);
        p.addDocumento(receita);
        documentos.add(receita);
        return receita;
    }

    public DocumentoMedico gerarExame(Paciente p, Medico m, LocalDate dataCriacao, String resultado) {
        DocumentoMedico exame = new Exame(p.getNome(), m.getNome(), p.getCpf(), m.getCpf(), dataCriacao, resultado);
        p.addDocumento(exame);
        documentos.add(exame);
        return exame;
    }


    public Consulta marcarConsulta(Paciente p, Medico m, LocalDate marcacao, LocalTime hora)
            throws DataIndisponivel, MedicoDesativado {

        if (!m.isAtivo())
            throw new MedicoDesativado("O doutor " + m.getNome() + " não está mais disponível");

        if (!m.dentroDoExpediente(hora))
            throw new DataIndisponivel();

        Consulta consulta = new Consulta(p.getNome(), m.getNome(), p.getCpf(), m.getCpf(), marcacao, hora,
                m.getEspecialidade());
        consultasMarcadas.add(consulta);
        m.arquivarConsulta(consulta);
        p.agendarConsulta(consulta);
        return consulta;
    }

    public void desmarcarConsulta(Consulta consulta) {
        consultasMarcadas.remove(consulta);
        Medico medico = (Medico) procurarUsuarioPorCPF(consulta.getCpfMedicoResponsavel());
        Paciente paciente = (Paciente) procurarUsuarioPorCPF(consulta.getCpfPaciente());
        medico.removerConsulta(consulta);
        paciente.removerConsulta(consulta);
    }

    public void organizarConsultasPorData(List<Consulta> consultas) {
        consultas.sort((c1, c2) -> c1.getMarcacao().compareTo(c2.getMarcacao()));
    }

    public void organizarConsultasPorMedicos(List<Consulta> consultas) {
        consultas.sort((c1, c2) -> c1.getNomeMedico().compareTo(c2.getNomeMedico()));
    }

    public List<Consulta> filtrarConsultasPorUsuario(String usuarioCpf) {
        List<Consulta> consultas = new ArrayList<>();
        for (Consulta consulta : consultasMarcadas) {
            if (usuarioCpf.equals(consulta.getCpfPaciente()) || usuarioCpf.equals(consulta.getCpfMedicoResponsavel())) {
                consultas.add(consulta);
            }
        }

        return consultas;
    }

    public String getNomeHospital() {
        return nomeHospital;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
