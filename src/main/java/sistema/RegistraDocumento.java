package sistema;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sistema.documentos.DocumentoMedico;
import usuario.Paciente;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegistraDocumento {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void registrarConsultas (List<Consulta> consultas){
        File file = new File("src/main/java/sistema/docsDB/consultaDB.json");


        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, consultas);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao manipular o arquivo de consultas: " + e.getMessage(), e);
        }
    }

    public static void registrarProntuarios (List<Prontuario>  prontuarios){
        File file = new File("src/main/java/sistema/docsDB/prontuarioDB.json");


        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, prontuarios);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao manipular o arquivo de prontuarios: " + e.getMessage(), e);
        }
    }

    public static void registrarDocumentos (List<DocumentoMedico> documentos){
        File file = new File("src/main/java/sistema/docsDB/documentosDB.json");


        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, documentos);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao manipular o arquivo de documentos: " + e.getMessage(), e);
        }
    }


    private static <T> List<T> lerEntidades(String caminho, TypeReference<List<T>> typeRef) {
        File file = new File(caminho);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(file, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo em " + caminho + ": " + e.getMessage());
        }
    }

    public static List<Consulta> leConsultas(){
        try {
            return lerEntidades("src/main/java/sistema/docsDB/consultaDB.json", new TypeReference<List<Consulta>>() {
            });
        }catch (RuntimeException e){
            System.out.println("Erro ao ler o arquivo de consultas: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }
        return null;
    }

    public static List<Prontuario> leProntuarios(){
        return lerEntidades("src/main/java/sistema/docsDB/prontuarioDB.json", new TypeReference<List<Prontuario>>() {});
    }

    public static List<DocumentoMedico> leDocumentos(){
        return lerEntidades("src/main/java/sistema/docsDB/documentosDB.json", new TypeReference<List<DocumentoMedico>>() {});
    }

}

