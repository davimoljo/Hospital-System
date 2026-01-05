package sistema;

import excessoes.DataIndisponivel;
import excessoes.MedicoDesativado;
import excessoes.ProntuarioJaExistente;
import excessoes.UsuarioJaExistente;
import sistema.documentos.Atestado;
import sistema.documentos.DocumentoMedico;
import sistema.documentos.Receita;
import usuario.*;
import usuario.userDB.RepositorioDeUsuario;
import utilitarios.*;
import utilitarios.Medicamento;

import java.util.*;

public class Hospital {
    protected final String nomeHospital = "HU (Hospital Universitário)";
    private List<Usuario> usuarios;
    private List<DocumentoMedico> documentos;
    private List<Prontuario> prontuarios;
    private List<Consulta> consultasMarcadas;

    public Hospital(){
        usuarios = RepositorioDeUsuario.carregarUsuarios();
        documentos = RegistraDocumento.leDocumentos();
        prontuarios = RegistraDocumento.leProntuarios();
        consultasMarcadas = RegistraDocumento.leConsultas();
        for (Usuario u : usuarios) {
            if (u instanceof Medico m){
                m.getConsultasMarcadas().clear();
            }
            else if (u instanceof Paciente p){
                p.getConsultasMarcadas().clear();
            }
        }

        for (Consulta c : consultasMarcadas) {
            Usuario medicoOficial = procurarUsuarioPorCPF(c.getMedicoResposavel().getCpf());
            Usuario pacienteOficial = procurarUsuarioPorCPF(c.getPaciente().getCpf());
            c.setMedico((Medico) medicoOficial);
            c.setPaciente((Paciente) pacienteOficial);

            ((Medico) medicoOficial).arquivarConsulta(c);
            ((Paciente) pacienteOficial).agendarConsulta(c);
        }
    }

    public void fechar(){
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
    public void cadastrarMedico(String nome, String cpf, String senha, String email, String crm, Especialidade especialidade){
        if(usuarioExiste(cpf)){
            throw new UsuarioJaExistente("Usuário com cpf " + cpf +" já existe");
        }
        cadastrarUsuario(new Medico(nome, cpf, senha, email, crm, especialidade));
    }

    public void cadastrarPaciente(String nome, String cpf, String senha, String email, String convenio){
        if (usuarioExiste(cpf)){
            throw new UsuarioJaExistente("Usuário com cpf " + cpf +" já existe");
        }
        cadastrarUsuario(new Paciente(nome, cpf, senha, email, convenio));
    }

    public void cadastrarSecretaria(String nome, String cpf, String senha, String email, String matricula){
        if (usuarioExiste(cpf)){
            throw new UsuarioJaExistente("Usuário com cpf " + cpf +" já existe");
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

    public Usuario procurarUsuarioPorCPF(String cpf) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                return usuario;
            }
        }
        return null;
    }

    public DocumentoMedico gerarAtestado(Paciente p, Medico m, Data termino) {
        DocumentoMedico atestado = new Atestado(p, m, termino);
        documentos.add(atestado);
        return atestado;
    }

    public DocumentoMedico gerarRececita(Paciente p, Medico m, List<Medicamento> medicamentos) {
        DocumentoMedico receita = new Receita(p, m);
        documentos.add(receita);
        return receita;
    }

    public Consulta marcarConsulta(Paciente p, Medico m, Data marcacao, Hora hora) throws DataIndisponivel, MedicoDesativado{

        if (!m.isAtivo())
            throw new MedicoDesativado("O doutor " + m.getNome() + " não está mais disponível");

        if (!m.dentroDoExpediente(hora))
            throw new DataIndisponivel();

        for (Consulta consulta : m.getConsultasMarcadas()) {
            if ((consulta.getHora().equals(hora) && consulta.getMarcacao().equals(marcacao)
                    && consulta.getMedicoResposavel().equals(m))){
                throw new DataIndisponivel();
            }
        }

        Consulta consulta = new Consulta(p, m, marcacao, hora);
        consultasMarcadas.add(consulta);
        m.arquivarConsulta(consulta);
        p.agendarConsulta(consulta);
        return consulta;
    }

    public void organizarConsultasPorData(List<Consulta> consultas) {
        consultas.sort((c1, c2) -> c1.getMarcacao().compareTo(c2.getMarcacao()));
    }

    public void organizarConsultasPorMedicos(List<Consulta> consultas) {
        consultas.sort((c1, c2) -> c1.getMedicoResposavel().getNome().compareTo(c2.getMedicoResposavel().getNome()));
    }

    public List<Consulta> filtrarConsultasPorUsuario(Usuario usuario) {
        List<Consulta> consultas = new ArrayList<>();
        for (Consulta consulta : consultasMarcadas) {
            if (usuario.equals(consulta.getMedicoResposavel()) || usuario.equals(consulta.getPaciente())) {
                consultas.add(consulta);
            }
        }

        return  consultas;
    }


    public Prontuario gerarProntuario(Paciente paciente, String doenca, StatusDoenca statusDoenca) {
        if (!paciente.definirProntuario(doenca, statusDoenca)) {
            throw new ProntuarioJaExistente("Prontuário de " + paciente.getNome() + " ja existente.");
        }
        prontuarios.add(paciente.getProntuario());
        return paciente.getProntuario();
    }

    public String getNomeHospital() {
        return nomeHospital;
    }

    public List<Usuario> getUsuarios() {return  usuarios;}
}
