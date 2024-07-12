# Projeto Spring CRUD de Produtos

## Descrição
Este projeto é um exemplo de aplicação Spring Boot que implementa um CRUD básico (Create, Read, Update, Delete) para uma entidade `Produto`. A aplicação também utiliza Swagger para documentação da API e inclui testes de unidade e de integração.

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3.3.1
- Spring Data JPA
- H2 Database
- Swagger 3
- JUnit 5
- Maven

## Estrutura do Projeto
- `src/main/java`: Código-fonte principal da aplicação
    - `io.fagner.product.manager`: Pacote principal da aplicação
    - `io.fagner.product.manager.controller`: Controladores REST
    - `io.fagner.product.manager.model`: Entidades JPA
    - `io.fagner.product.manager.dao`: Repositórios JPA
    - `io.fagner.product.manager.service`: Serviços da aplicação
    - `io.fagner.product.manager.handler`: Tratamento de exceções
    - `io.fagner.product.manager.configuration`: Configurações adicionais
- `src/test/java`: Testes de unidade e integração
- `src/main/resources`: Recursos da aplicação, como arquivos de configuração
- `pom.xml`: Arquivo de configuração do Maven

## Requisitos
- Java 21 ou superior
- Maven 3.6.3 ou superior

## Configuração do Projeto
1. Clone o repositório para sua máquina local:
```bash
   git clone https://github.com/fagnermatos/ProductManager.git
   cd ProductManager
```

2. Compilar o projeto e baixar as dependências do projeto usando Maven:

```bash
mvn clean package
```
## Executando a Aplicação

Execute a aplicação usando Maven:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em http://localhost:8080.

## Documentação da API
A documentação da API gerada pelo Swagger pode ser acessada em:

```bash
http://localhost:8080/swagger-ui.html
```
Endpoints da API
* GET /products: Retorna todos os produtos
* GET /products/{id}: Retorna um produto pelo ID
* POST /products: Cria um novo produto
* PUT /products/{id}: Atualiza um produto existente
* DELETE /products/{id}: Deleta um produto pelo ID

## Testes

Testes de Unidade
Os testes de unidade estão localizados no diretório src/test/java/io/fagner/product/manager/unit. 
Para executar os testes de unidade, use o comando:

```bash
mvn test
```

Testes de Integração
Os testes de integração também estão localizados no diretório src/test/java/io/fagner/product/manager/integration.
Para executar os testes de integração, use o comando:

```bash
mvn verify
```

Banco de Dados H2
A aplicação está configurada para usar o banco de dados em memória H2. 
O console do H2 pode ser acessado em:

```bash
http://localhost:8080/h2-console
```
As credenciais de acesso padrão são:

* URL: jdbc:h2:mem:product_manager
* Usuário: sa
* Senha: (em branco)

Contato
Para mais informações, entre em contato com fagnerjmatosifce@gmail.com.