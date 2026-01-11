package usuario;

import sistema.Hospital;

import java.time.*;

public class Secretaria extends Usuario {
    private String matricula;

    public Secretaria(String nome, String cpf, String senha, String email, String matricula) {
        super(nome, cpf, senha, email);
        this.matricula = matricula;
        tipo = TipoUsuario.SECRETARIA;
    }

    public Secretaria() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String getTipoUsuario() {
        return "SECRETARIA";
    }

    public void marcarConsulta(Hospital hospital, Paciente p, Medico m, LocalDate d, LocalTime h) {
        // Delega para o hospital, que é quem sabe as regras de agendamento
        hospital.marcarConsulta(p, m, d, h);
    }
}