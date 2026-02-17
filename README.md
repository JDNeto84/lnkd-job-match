# MatchJob

Projeto Spring Boot para gerenciamento de usuários, autenticação JWT e preferências de busca de vagas.

## Visão Geral
- Framework: Spring Boot 4.0.2 (WebMVC, Security, Validation)
- Linguagem/Runtime: Java 25
- Persistência: Spring Data JPA com PostgreSQL
- Migrações: Flyway
- Autenticação: JWT (Bearer)
- Documentação de API: Springdoc OpenAPI (Swagger UI)
- Testes: JUnit + JaCoCo

## Arquitetura

Pacotes principais (dentro de `com.matchjob`):
- domain: entidades de domínio (`user`), value objects (`valueobject`) e exceções (`exception`)
- application:
  - port.incoming: interfaces dos casos de uso (entrada)
  - port.outgoing: portas externas (UserRepository, PasswordEncoder, TokenService, etc.)
  - user: implementações dos casos de uso e DTOs de comandos/consultas/resultados
- infrastructure:
  - web: controllers REST, DTOs HTTP e handlers globais de exceção
  - persistence: entidades JPA, repositórios Spring Data e adaptadores das portas de saída
  - security: JWT, filtros, encoder de senha e handlers de segurança
  - messaging: produtores/integrações assíncronas
- config:
  - security: configuração do Spring Security (filtros, authorization rules, encryption)
  - swagger: configuração do Springdoc/OpenAPI
  - usecase: configuração dos beans de casos de uso ligando ports e implementações

`MatchjobApplication` é o ponto de entrada da aplicação e inicializa o contexto Spring.

### Padrão de Persistência (User)

Para o agregado `User`, a camada de persistência segue um padrão explícito:

- `UserEntity` (`infrastructure.persistence.user`)
  - Entidade JPA mapeada para a tabela `users`
  - Colunas explicitadas com `@Column(name = "...")`
  - `@PrePersist` e `@PreUpdate` controlam `created_at` e `updated_at`
  - Setters com visibilidade de pacote; usada apenas por infraestrutura/mapper
- `SpringDataUserRepository`
  - Interface técnica `JpaRepository<UserEntity, UUID>`
  - Define consultas específicas (ex.: `findByEmail`)
- `JpaUserRepository`
  - Adapter que implementa o port `application.port.outgoing.UserRepository`
  - Usa `SpringDataUserRepository` e `UserMapper` para:
    - salvar (`save`) com semântica de criar ou atualizar (merge) entidade
    - carregar domínio via `findById`/`findByEmail`
- `UserMapper`
  - Bean Spring responsável por mapear:
    - `User` → `UserEntity` (criação)
    - `User` + `UserEntity` → merge de campos mutáveis (`applyToEntity`)
    - `UserEntity` → `User` (reconstrução com VOs e timestamps)
  - Não manipula timestamps (responsabilidade da entidade) nem altera email/id de entidades existentes

Esse padrão deve ser reutilizado em novos agregados: entidade JPA + repositório Spring Data técnico + adapter de porta + mapper dedicado.

Visão em camadas (lógica):

```text
┌─────────────────────────────────────────────┐
│         infrastructure.web                  │
│  Controllers REST / DTOs HTTP / handlers    │
└──────────────────────▲──────────────────────┘
                       │ usa casos de uso
┌──────────────────────┴──────────────────────┐
│              application                     │
│  casos de uso + ports in/out + DTOs         │
└──────────────────────▲──────────────────────┘
                       │ manipula domínio via portas
┌──────────────────────┴──────────────────────┐
│                 domain                       │
│  entidades, VOs, regras de negócio          │
└──────────────────────▲──────────────────────┘
                       │ implementado por
┌──────────────────────┴──────────────────────┐
│           infrastructure.*                  │
│  persistence, security, messaging           │
└──────────────────────▲──────────────────────┘
                       │ inicializado por
┌──────────────────────┴──────────────────────┐
│   MatchjobApplication + config.*            │
└─────────────────────────────────────────────┘
```

### Entidades e Value Objects
- User (imutável):
  - Campos: id, name, email, password, active, keyword, location, remote, plan, role, telegramChatId, createdAt, updatedAt
  - Criação:
    - `createNewUser(name, email, rawPassword, plan, role, passwordEncoder)`
      - Gera id novo e `createdAt/updatedAt` com `now`
      - Sempre nasce `active = true`
    - `reconstructUser(id, Name, Email, Password, plan, role, active, Keyword, Location, isRemote, telegramChatId, createdAt, updatedAt)`
      - Usado para reconstruir a partir de persistência/testes
  - Mutação funcional:
    - `withPassword(Password newPassword)` → retorna nova instância com hash atualizado e `updatedAt` renovado
    - `withLocation(Location location)` → retorna nova instância com localização atualizada e `updatedAt` renovado
  - Não expõe senha como `String`; apenas via `Password` VO
- Email:
  - Normaliza para minúsculas
  - Valida formato com regex
- Password:
  - Sempre representa um hash
  - `create(raw, encoder)` para senha em texto com validação de força
  - `fromHashed(hash)` apenas para hash previamente gerado (ex.: banco)
  - Faz `matches(raw, encoder)` para comparação segura
- Name:
  - Não permite vazio
  - Normaliza espaços e caracteres
- Keyword:
  - Não permite vazio
  - Normaliza e limita tamanho
- Location:
  - Canonicaliza cidade/estado/país com regras explícitas

## Regras de Negócio
- Email:
  - Sempre salvo em minúsculas (VO `Email` garante normalização)
  - Unicidade case-insensitive via índice único em `LOWER(email)` (Flyway + DDL)
  - Busca/autenticação sempre usando email normalizado
- Password:
  - Sempre armazenado como hash (BCrypt via `PasswordEncoder`)
  - Senha RAW só entra no domínio por `Password.create(raw, encoder)`
  - `fromHashed` é usado apenas para reconstrução a partir de hash persistido
  - A entidade `User` não expõe o hash como `String`, apenas `Password`
- User:
  - Imutável (campos `final`, sem setters)
  - Novo usuário sempre nasce ativo (`active = true`) em `createNewUser`
  - Métodos `withX` sempre retornam nova instância, preservando invariantes
- Location:
  - Um componente: preserva valor (ex.: `"Curitiba"` → `"Curitiba"`)
  - Dois componentes: adiciona `", Brasil"` quando país não explícito
    - ex.: `"São Paulo, SP"` → `"São Paulo, SP, Brasil"`
  - Três componentes: respeita país informado (vazio vira `"Brasil"`)
- Preferências do usuário:
  - No controller, se `location` vier vazio/nulo, usa `"Brasil"` por padrão
  - `UpdateUserPreferencesService` só altera keyword/location/remote, preservando demais campos

## Configuração
Requer PostgreSQL acessível e variáveis de ambiente:
- SPRING_DATASOURCE: jdbc:postgresql://host:port/db
- POSTGRES_USER, POSTGRES_PASS
- SPRING_JPA_HIBERNATE_DDL_AUTO: none
- SPRING_JPA_SHOW_SQL: true/false
- HIBERNATE_JDBC_FETCH_SIZE
- HIBERNATE_CONNECTION_AUTOCOMMIT
- SERVER_PORT
- CORS_ALLOWED_ORIGENS, CORS_ALLOWED_METHODS
- JWT_SECRET (Base64), JWT_EXP_MIN
- LOG_LEVEL, LOG_SQL

Exemplo (bash):
```
export SPRING_DATASOURCE=jdbc:postgresql://localhost:5432/matchjob
export POSTGRES_USER=matchjob
export POSTGRES_PASS=secret
export SERVER_PORT=8080
export SPRING_JPA_HIBERNATE_DDL_AUTO=none
export SPRING_JPA_SHOW_SQL=true
export JWT_SECRET=ZmFrZV9zZWNyZXRfYmFzZTY0
export JWT_EXP_MIN=60
```

## Migrações (Flyway)
- V1__create_users_table.sql: cria tabela `users` com colunas principais
  - Normaliza emails existentes para minúsculas
As migrações rodam automaticamente na inicialização da aplicação.

## Build, Test e Execução
No módulo `matchjob`:
- Build: `cd matchjob && ./gradlew build`
- Testes: `cd matchjob && ./gradlew test`
- Cobertura (HTML): `matchjob/build/reports/tests/test/index.html` e `matchjob/build/reports/jacoco/test/html/index.html`
- Execução (dev): `cd matchjob && ./gradlew bootRun` ou `./bootrun.sh` na raiz do repositório

## Endpoints Principais
Base: `/api`

### Autenticação
- POST `/api/auth/login`
  - Body: `{ "email": "user@teste.com", "password": "Senha123" }`
  - Retorna: `{ token, type: "Bearer", expiresIn }`
- POST `/api/auth/register`
  - Body: `{ "name": "João", "email": "user@teste.com", "password": "Senha123" }`
  - Retorna: dados básicos do usuário
- POST `/api/auth/me`
  - Header: `Authorization: Bearer <token>`
  - Retorna: dados do usuário autenticado

### Usuário
- POST `/api/user/change-preferences`
  - Header: `Authorization: Bearer <token>`
  - Body:
    ```
    {
      "keyword": "Desenvolvedor",
      "location": "",        // se vazio, aplica "Brasil"
      "remotePreferred": true
    }
    ```
  - Retorna: dados básicos do usuário
- POST `/api/user/change-password`
  - Header: `Authorization: Bearer <token>`
  - Body: `{ "currentPassword": "SenhaAtual123", "newPassword": "NovaSenha123" }`

## Segurança
- JWT via header `Authorization: Bearer <token>`
- Filtro de autenticação popula contexto com authorities do token

## Documentação da API
- Swagger UI: `/swagger-ui.html`

## Convenções & Boas Práticas
- Emails sempre em minúsculas, comparação/busca normalizada
- Senhas nunca em texto plano no banco
- Value Objects garantem validações e canonicalizações
- Casos de uso isolam regras de negócio e persistência

## Desenvolvimento
- Novos casos de uso: definir portas em `application/port/incoming/...` e implementação em `application/user/...`, registrando beans em `config/usecase`
- Persistência: mapear entidades e adaptadores em `infrastructure/persistence/...`
- Exceções de domínio: `domain/exception`
- Handlers REST: `infrastructure/web/handler`

## Licença
Projeto acadêmico/experimental; adapte conforme necessidade.
