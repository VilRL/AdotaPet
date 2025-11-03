# 🐾 AdotaPet - Sistema de Adoção de Animais

Projeto desenvolvido em **Java 17** com JDBC + HttpServer embutido.  
O sistema gerencia animais disponíveis para adoção e seus owners, permitindo operações de cadastro, consulta, atualização, exclusão e adoção.

---

## ⚙️ Requisitos

- **Java 17+** instalado
- **Maven** (ou IntelliJ com suporte a Maven)
- Banco de dados **MySQL** rodando e acessível

---

## 📦 Procedimentos de compilação, build e execução

### 1. Clonar o repositório
```bash
git clone https://github.com/seu-usuario/adotapet.git
cd adotapet
```
### 2. Configurar o banco de dados
```bash
CREATE DATABASE adotapet;
USE adotapet;
-- rodar CREATE TABLE de animais e owners aqui
```
No arquivo ConexaoFactory.java, configure:
```bash
private static final String URL = "jdbc:mysql://localhost:3306/adotapet";
private static final String USER = "root";
private static final String PASSWORD = "sua_senha";
```
### 3. Compilar o projeto
```bash
mvn clean compile
```
### 4. Gerar o build
```bash
mvn package
```
### 5. Executar a aplicação
```bash
mvn exec:java -Dexec.mainClass="com.lojaadocao.Main"
A API subirá em http://localhost:8080
```
## 🌐 Endpoints disponíveis

### 🐶 Animal
- **POST /animais** → Cadastrar um animal  
- **GET /animais** → Listar todos os animais  
- **GET /animais/disponiveis** → Listar apenas os animais disponíveis  
- **GET /animais/{id}** → Buscar um animal por ID  
- **PUT /animais/{id}** → Atualizar informações de um animal  
- **DELETE /animais/{id}** → Excluir um animal  
- **POST /animais/adotar** → Adotar um animal (informando owner e animal)  

### 👤 Dono
- **POST /owners** → Cadastrar um owner  
- **GET /owners** → Listar todos os owners  
- **GET /owners/{id}** → Buscar um owner por ID  
- **PUT /owners/{id}** → Atualizar informações do owner  
- **DELETE /owners/{id}** → Excluir owner 
