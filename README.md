# 🔗 Encurtador de URL

API REST desenvolvida em Java com Spring Boot para encurtar URLs, com persistência em MySQL e contagem de cliques.

Projeto criado como estudo prático de construção de APIs REST com Spring Boot, JPA/Hibernate e MySQL.

##  Funcionalidades

- Encurtar uma URL original, gerando um código curto único
- Redirecionar automaticamente ao acessar a URL encurtada
- Reutilizar o código já existente caso a mesma URL seja encurtada novamente
- Contabilizar o número de cliques em cada link encurtado

##  Tecnologias

- Java 21+
- Spring Boot 4
- Spring Data JPA / Hibernate
- MySQL
- Lombok
- Maven

##  Regras de negócio

- Cada URL original só gera **um único** código curto — se a mesma URL for encurtada novamente, o mesmo código é retornado (evita duplicidade no banco).
- Toda vez que a URL encurtada é acessada, o contador de cliques é incrementado antes do redirecionamento.
- O redirecionamento é feito via **HTTP 302**, direto no navegador, sem páginas intermediárias.

##  Endpoints

### Criar URL encurtada

```http
POST /api/urls
Content-Type: application/json

{
  "urlOriginal": "https://www.google.com"
}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "urlOriginal": "https://www.google.com",
  "urlEncurtada": "e94e73",
  "dataDeCriacao": "2026-08-07T15:39:45.449673",
  "cliques": 0
}
```

### Acessar URL encurtada (redirect)

```http
GET /{codigo}
```

Redireciona automaticamente (302) para a URL original e incrementa o contador de cliques.

## 🐳 Rodar localmente com Docker

O Docker inicia a API e um MySQL local automaticamente. Não é necessário instalar MySQL no computador.

```powershell
docker compose up --build
```

A interface estará em `http://localhost:8081`. Os dados do banco ficam no volume Docker `mysql-data`.

## 🚀 Deploy com Docker e Aiven MySQL

O contêiner executa somente a API. O banco fica no Aiven, onde as credenciais são fornecidas em variáveis de ambiente e não ficam registradas no código.

1. Crie um serviço **MySQL Free** no [Aiven](https://aiven.io/free-mysql-database) e obtenha a URL, usuário e senha de conexão.
2. Na pasta `EncurtadorDeLinks-API`, crie seu arquivo local de variáveis:

```powershell
Copy-Item .env.example .env
```

3. Edite o `.env` com os dados do Aiven. Preserve `useSSL=true` e `requireSSL=true` na URL.
4. Construa e suba a API usando a configuração do Aiven:

```powershell
docker compose -f compose.aiven.yaml up --build
```

A interface e API estarão disponíveis em `http://localhost:8081`. Para encerrar, use `docker compose down`.

## ⚙️ Como rodar localmente sem Docker

### Pré-requisitos
- Java 21+
- MySQL rodando localmente
- Maven

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/encurtador-url.git
cd encurtador-url
```

2. Crie o banco de dados no MySQL (ou deixe o `createDatabaseIfNotExist=true` criar automaticamente):
```sql
CREATE DATABASE encurtador;
```

3. Copie o arquivo de exemplo de configuração e preencha com suas credenciais:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

4. Edite `application.properties` com seu usuário e senha do MySQL.

5. Rode a aplicação:
```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

##  Testando

Você pode testar os endpoints via Postman/Insomnia:

1. `POST http://localhost:8080/api/urls` com o body indicado acima
2. Copie o `urlEncurtada` retornado
3. Acesse `http://localhost:8080/{codigo}` no navegador para ver o redirect funcionando

##  Próximos passos

- [ ] Deploy em produção (Render)
- [ ] Tratamento de erros com respostas HTTP apropriadas (404 para código não encontrado)
- [ ] Frontend simples para interação visual
- [ ] Validação de formato de URL
- [ ] Migração para Flyway (controle de versão de schema)

##  Autor

Ryan Falcão — desenvolvido como parte dos estudos em Java/Spring Boot.
