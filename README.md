# Sistema de Gerenciamento Hospitalar

Este projeto é um sistema desenvolvido em **Java** para o gerenciamento de atividades hospitalares. O software permite o cadastro e autenticação de diferentes tipos de usuários (Pacientes, Médicos e Secretários), agendamento de consultas, controle de prontuários e emissão de documentos médicos.

## 📋 Funcionalidades Principais

O sistema atende a três perfis de usuários com permissões específicas:

### 1. Pacientes

- **Cadastro e Login:** Acesso seguro com validação de CPF e senha.
- **Agendamento:** Solicitação de consultas médicas.
- **Histórico:** Visualização de consultas anteriores e agendadas.
- **Documentos:** Acesso a atestados e documentos médicos gerados.

### 2. Médicos

- **Gestão de Agenda:** Configuração de horários de expediente por dia da semana.
- **Consultas:** Visualização da agenda diária e realização de atendimentos.
- **Validação:** O sistema impede agendamentos fora do expediente ou em horários já ocupados.
- **Especialidades:** Suporte a diversas especialidades (Cardiologia, Dermatologia, Geral, etc.).

### 3. Secretária / Administração

- **Agendamento Centralizado:** Capacidade de marcar consultas para pacientes.
- **Gestão de Usuários:** Cadastro de novos pacientes e médicos.

## 🛠 Tecnologias Utilizadas

O projeto utiliza **Maven** para gerenciamento de dependências. As principais tecnologias incluem:

- **Java 17:** Linguagem base do projeto.
- **Swing:** Biblioteca gráfica (GUI) para as telas de Login e Cadastro.
- **Jackson (2.16+):** Framework para serialização e persistência de dados em JSON, incluindo suporte para datas (JavaTimeModule).
- **Gson:** Biblioteca auxiliar para manipulação de JSON.
- **JUnit 5:** Framework para testes unitários automatizados.

## 📂 Estrutura do Projeto

A arquitetura segue uma divisão lógica de responsabilidades:

- `src/main/java`
  - **`usuario`**: Contém as entidades principais (`Paciente`, `Medico`, `Secretaria`) herdando de `Usuario`.
  - **`usuario.userDB`**: Camada de persistência (`RepositorioDeUsuario`) que salva os dados em um arquivo JSON local (`userDatabase.json`).
  - **`usuario.validacoes`**: Serviços de regras de negócios, como `LoginService` (autenticação) e `SecretariaService` (regras de agendamento).
  - **`sistema`**: Classes de domínio do sistema hospitalar como `Hospital`, `Consulta` e `Prontuario`.
  - **`view`**: Interfaces gráficas (Telas) construídas com Java Swing.
  - **`excessoes`**: Tratamento de erros personalizados (ex: `SenhaIncorretaException`, `MedicoDesativado`).

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java JDK 17 ou superior.
- Maven instalado.

### Passos

1.  **Clone o repositório:**

    ```bash
    git clone [https://github.com/davimoljo/Hospital-System.git](https://github.com/davimoljo/Hospital-System.git)
    cd Hospital-System
    ```

2.  **Compile o projeto e baixe as dependências:**

    ```bash
    mvn clean install
    ```

3.  **Execute a aplicação:**
    Você pode executar diretamente pela classe `Main` ou via Maven:
    ```bash
    mvn exec:java -Dexec.mainClass="Main"
    ```
    _A aplicação iniciará pela tela de Login (`TelaLogin`)_.

## 🧪 Testes

O projeto conta com uma suíte de testes unitários cobrindo as principais regras de negócio, como validação de CPF, verificação de horários de médicos e lógica de agendamento.

Para rodar os testes:

```bash
mvn test
```
