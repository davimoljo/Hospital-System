# Hospital Management System 🏥

A robust desktop application for managing hospital routines, developed in **Java** using **Swing** for the interface and **JSON** for data persistence. This project was created as part of the Object-Oriented Programming (OOP) course at UFJF.

## 📋 About the Project

The system aims to facilitate the management of doctors, patients, and appointments. It implements a **Facade** architecture pattern (`Hospital` class) to centralize business logic and uses a custom **Service Layer** to handle data validation and bridge the gap between the UI and the backend.

### Key Features
* **User Management:** Registration of Doctors, Patients, and Secretaries with inheritance and polymorphism.
* **Dynamic Scheduling:** Appointment booking with automatic validation of medical shifts and conflict detection.
* **Electronic Medical Records (EMR):** Creation of medical records linked to consultations.
* **Medical Documents:** Generation of Prescriptions, Certificates, and Exam Results (Polymorphism applied).
* **Data Persistence:** Automatic saving and loading of data using JSON files (via Jackson library).
* **Access Control:** Login system with role-based features.

## 🚀 Technologies & Libraries

* **Java 17+**
* **Maven** (Dependency Management)
* **Java Swing** (Graphical User Interface)
* **Jackson** (`com.fasterxml.jackson`) - For JSON Serialization/Deserialization.
* **JUnit 5** (`org.junit.jupiter`) - For Unit and Integration Testing.

## 🏗️ Architecture & Design Patterns

* **MVC (Model-View-Controller/Service):** Clear separation between UI (`view`), Logic (`sistema`/`usuario`), and Validation (`validacoes`).
* **Facade Pattern:** The `Hospital` class acts as the single entry point for system operations.
* **Polymorphism:** Extensive use in `Usuario` (Doctor/Patient) and `DocumentoMedico` (Prescription/Certificate).
* **Strategy/State (Implicit):** Handling of medical shifts using `Map<DayOfWeek, HorarioExpediente>`.

## 👥 Authors

* **João Pedro Lemos Guadalupe** 
* **Davi Moljo Domingues** 
* **Carlos Roberto da Silva** 

---
**Disclaimer:** This is an academic project designed to demonstrate OOP concepts.

# Sistema de Gerenciamento Hospitalar 🏥

Uma aplicação desktop robusta para gerenciamento de rotinas hospitalares, desenvolvida em **Java** utilizando **Swing** para a interface e **JSON** para persistência de dados. Este projeto foi criado como parte da disciplina de Programação Orientada a Objetos (POO) da UFJF.

## 📋 Sobre o Projeto

O sistema visa facilitar a gestão de médicos, pacientes e consultas. Ele implementa o padrão de arquitetura **Facade** (classe `Hospital`) para centralizar a lógica de negócios e utiliza uma **Camada de Serviço** (Service Layer) personalizada para lidar com validação de dados e fazer a ponte entre a interface gráfica e o backend.

### Principais Funcionalidades
* **Gestão de Usuários:** Cadastro de Médicos, Pacientes e Secretárias utilizando herança e polimorfismo.
* **Agendamento Dinâmico:** Marcação de consultas com validação automática de horários de expediente e detecção de conflitos.
* **Prontuário Eletrônico:** Criação de registros médicos vinculados às consultas.
* **Documentos Médicos:** Geração de Receitas, Atestados e Resultados de Exames (Polimorfismo aplicado).
* **Persistência de Dados:** Salvamento e carregamento automático de dados usando arquivos JSON (via biblioteca Jackson).
* **Controle de Acesso:** Sistema de login com funcionalidades baseadas no tipo de usuário.

## 🚀 Tecnologias e Bibliotecas

* **Java 17+**
* **Maven** (Gerenciamento de Dependências)
* **Java Swing** (Interface Gráfica do Usuário)
* **Jackson** (`com.fasterxml.jackson`) - Para Serialização/Desserialização JSON.
* **JUnit 5** (`org.junit.jupiter`) - Para Testes Unitários e de Integração.

## 🏗️ Arquitetura e Padrões de Projeto

* **MVC (Model-View-Controller/Service):** Separação clara entre Interface (`view`), Lógica (`sistema`/`usuario`) e Validação (`validacoes`).
* **Padrão Facade:** A classe `Hospital` atua como o ponto de entrada único para as operações do sistema.
* **Polimorfismo:** Uso extensivo nas classes `Usuario` (Médico/Paciente) e `DocumentoMedico` (Receita/Atestado).
* **Tratamento de Exceções:** Exceções personalizadas para regras de negócio (ex: `MedicoDesativado`, `HoraInvalida`).

## 👥 Autores

* **João Pedro Lemos Guadalupe** 
* **Davi Moljo Domingues** 
* **Carlos Roberto da Silva** 

---
**Aviso:** Este é um projeto acadêmico projetado para demonstrar conceitos de POO.
