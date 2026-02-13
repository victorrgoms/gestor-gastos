# 💰 Gestor de Gastos (Backend)

API REST desenvolvida em **Java** com **Spring Boot** para gerenciamento de despesas pessoais e compartilhadas (cartão de crédito).

O objetivo principal é resolver o problema de **divisão de faturas** entre casais ou parceiros, calculando automaticamente quem deve pagar o que, baseando-se no dono do cartão e na pessoa responsável pela compra.

## 🚀 Tecnologias Utilizadas

* **Java 21** (Moderno e performático)
* **Spring Boot 3** (Framework principal)
* **Spring Data JPA** (Persistência de dados)
* **PostgreSQL** (Banco de dados relacional)
* **Bean Validation** (Validação de DTOs)
* **SpringDoc OpenAPI / Swagger** (Documentação automática)
* **Maven** (Gerenciador de dependências)

## ⚙️ Funcionalidades

* **CRUD Completo:**
    * Pessoas (Compradores/Parceiros)
    * Cartões de Crédito
    * Compras (Despesas)
* **Lógica de Divisão de Contas:**
    * Cálculo automático de quanto cada pessoa deve pagar na fatura.
    * Suporte a compras divididas (50/50) ou individuais.
    * Relatórios por pessoa.
* **Controle de Fatura:**
    * Registro de Mês e Ano de competência da fatura.

## 🛠️ Como rodar o projeto

### Pré-requisitos
* Java JDK 21 instalado.
* PostgreSQL instalado e rodando.
* Maven (opcional, se usar o wrapper do projeto).

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/victorrgoms/gestor-gastos](https://github.com/victorrgoms/gestor-gastos.git)
    cd gestor-gastos-backend
    ```

2.  **Configure o Banco de Dados:**
    * Crie um banco de dados no PostgreSQL chamado `gestor_gastos`.
    * Abra o arquivo `src/main/resources/application.properties`.
    * Atualize seu usuário e senha:
        ```properties
        spring.datasource.username=seu_usuario
        spring.datasource.password=sua_senha
        ```

3.  **Execute o projeto:**
    * Pela IDE (IntelliJ/Eclipse): Rode a classe `Main.java`.
    * Pelo terminal:
        ```bash
        ./mvnw spring-boot:run
        ```

## 📚 Documentação da API (Swagger)

Com o projeto rodando, acesse a documentação interativa para testar os endpoints:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

## 🧩 Estrutura do Projeto

O projeto segue a arquitetura em camadas (MVC/Layered):

* `controller`: Camada REST (Endpoints).
* `service`: Regras de negócio e lógica de divisão.
* `repository`: Acesso ao banco de dados (Spring Data).
* `model`: Entidades JPA (Tabelas).
* `dto`: Objetos de transferência de dados (Records) com validações.
* `config`: Configurações globais (CORS, etc).

## 💡 Regra de Negócio (Divisão)

A lógica central está no `CompraService`. O cálculo do total por pessoa segue estas premissas:

1.  **Compra Individual:** Se eu compro no meu cartão para mim mesmo -> Eu pago 100%.
2.  **Compra Dividida:** Se eu compro no meu cartão, mas marco um parceiro -> Eu pago 50%, Parceiro paga 50%.
3.  **Compra de Terceiro:** Se sou marcado como parceiro numa compra (mesmo que não seja meu cartão) -> Eu pago 50%.

---
Desenvolvido por **Victor Gomes** 🚀