package usuario.userDB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import usuario.Usuario;

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
}
