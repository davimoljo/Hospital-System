package usuario.userDB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import excessoes.UsuarioInexistente;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import usuario.Usuario;
import usuario.Paciente;

public class RepositorioDeUsuario {
    private static final File FILE = new File("src/main/java/usuario/userDB/userDatabase.json");
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Usuario> carregarUsuarios() {
        try {
            if (!FILE.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    FILE,
                    new TypeReference<List<Usuario>>() {
                    });

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar usuários", e);
        }
    }

    public static void registrarUsuarios(List<Usuario> usuarios) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(FILE, usuarios);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar usuários", e);
        }
    }

    public static Usuario buscarUsuario(String cpf) {
        List<Usuario> lista = carregarUsuarios();
        for (Usuario u : lista) {
            if (u.getCpf().equals(cpf))
                return u;
        }

        throw new UsuarioInexistente("O usuário não foi encontrado!");
    }

    public static Paciente buscarPaciente(String cpf) {
        List<Usuario> lista = carregarUsuarios();
        for (Usuario u : lista) {
            if (u.getCpf().equals(cpf) && u instanceof Paciente)
                return (Paciente) u;
        }

        throw new UsuarioInexistente("O usuário não foi encontrado!");
    }

}
