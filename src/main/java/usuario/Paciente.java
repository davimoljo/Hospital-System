package usuario;

/// MANTENHA O PACKAGE
///

import sistema.Prontuario;
import sistema.StatusDoenca;
import excessoes.*;
import sistema.Consulta;
import java.util.*;

public class Paciente extends Usuario {
    private String endereco;
    private String convenio;
    private Prontuario prontuario;
    private List<Consulta> consultasMarcadas;
    private boolean internado;
    private boolean aptoAVisitas;

    public Paciente(String nome, String cpf, String senha, String email, String convenio) {
        super(nome, cpf, senha, email);
        this.convenio = convenio;
        tipo = TipoUsuario.PACIENTE;
        prontuario = null;
        consultasMarcadas = new ArrayList<>();
        internado = false;
        aptoAVisitas = false;
    }

    public Paciente() {
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    @Override
    public String getTipoUsuario() {
        return "PACIENTE";
    }

    @Override
    public String toString() {
        return this.nome + " (Convênio: " + this.convenio + ")";
    }

    public Prontuario getProntuario() {
        return prontuario;
    }

    public void agendarConsulta(Consulta consulta) {
        consultasMarcadas.add(consulta);
    }

    public List<Consulta> getConsultasMarcadas() {
        return consultasMarcadas;
    }

    public boolean isInternado() {
        return internado;
    }

    public void alternarEstadoDeInternacao() {
        internado = !internado;
    }

    public void alternarVisita() {
        aptoAVisitas = !aptoAVisitas;
    }

    public boolean isAptoAVisitas() {
        return aptoAVisitas;
    }
}