# 📚 Bookshelf API

> API REST para gerenciamento de uma biblioteca virtual — Projeto Diamante 03 | Java Advanced

## 🎯 Sobre o Projeto

A **Bookshelf API** é uma API REST completa para gerenciamento de livros, autores e resenhas. O sistema permite catalogar obras literárias, seus autores e coletar avaliações dos leitores com controle de spoilers e notas de 1 a 5.

A terceira entidade escolhida foi **Resenha** — uma escolha foda porque:
- Conecta `Livro` e cria uma relação `@ManyToOne` interessante
- Tem lógica de negócio real (média de notas calculada dinamicamente)
- Tem o campo `temSpoiler` que habilita filtros específicos (`/sem-spoiler`)
- Agrega valor ao domínio: um livro sem resenha está "vazio"

---

## 🏗️ Entidades e Relacionamentos

```
Autor (1) ────────── (N) Livro (1) ────────── (N) Resenha
```

### `Autor`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | PK auto-gerado |
| nome | String | Nome completo (obrigatório) |
| nacionalidade | String | País de origem |
| bio | String | Biografia (até 2000 chars) |
| dataNascimento | LocalDate | Data de nascimento |

### `Livro`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | PK auto-gerado |
| titulo | String | Título da obra (obrigatório) |
| isbn | String | ISBN do livro |
| anoPublicacao | Integer | Ano de lançamento |
| genero | String | Gênero literário |
| paginas | Integer | Número de páginas |
| sinopse | String | Sinopse (até 2000 chars) |
| autor | Autor | FK para Autor (obrigatório) |

### `Resenha`
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | PK auto-gerado |
| titulo | String | Título da resenha (obrigatório) |
| conteudo | String | Texto completo (obrigatório) |
| nota | Integer | Nota de 1 a 5 (obrigatório) |
| temSpoiler | Boolean | Flag de spoiler (default: false) |
| livro | Livro | FK para Livro (obrigatório) |

---

## 🚀 Como Rodar

### Pré-requisitos
- Java 21+
- Maven 3.8+

### Executar

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/bookshelf-api.git
cd bookshelf-api

# Rodar
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080` com banco H2 em memória (dados de exemplo carregados automaticamente).

---

## 📖 Documentação

| Recurso | URL |
|---------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/api-docs |
| **H2 Console** | http://localhost:8080/h2-console |
| **Health** | http://localhost:8080/actuator/health |
| **Metrics** | http://localhost:8080/actuator/metrics |
| **Info** | http://localhost:8080/actuator/info |
| **Caches** | http://localhost:8080/actuator/caches |

---

## 🔌 Endpoints

### Autores `/api/autores`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/autores` | Lista todos (paginado, ordenável) |
| GET | `/api/autores/{id}` | Busca por ID (com HATEOAS) |
| GET | `/api/autores/buscar?nome=` | Busca por nome |
| GET | `/api/autores/nacionalidade/{nac}` | Filtra por nacionalidade |
| POST | `/api/autores` | Cria novo autor |
| PUT | `/api/autores/{id}` | Atualiza autor |
| DELETE | `/api/autores/{id}` | Remove autor |

### Livros `/api/livros`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/livros` | Lista todos (com média de notas!) |
| GET | `/api/livros/{id}` | Busca por ID (com HATEOAS) |
| GET | `/api/livros/buscar?titulo=` | Busca por título |
| GET | `/api/livros/genero/{genero}` | Filtra por gênero |
| GET | `/api/livros/autor/{autorId}` | Livros de um autor |
| GET | `/api/livros/ano/{ano}` | Filtra por ano |
| POST | `/api/livros` | Cria novo livro |
| PUT | `/api/livros/{id}` | Atualiza livro |
| DELETE | `/api/livros/{id}` | Remove livro |

### Resenhas `/api/resenhas`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/resenhas` | Lista todas |
| GET | `/api/resenhas/{id}` | Busca por ID |
| GET | `/api/resenhas/livro/{livroId}` | Resenhas de um livro |
| GET | `/api/resenhas/livro/{livroId}/sem-spoiler` | Resenhas sem spoiler 🎯 |
| GET | `/api/resenhas/nota/{nota}` | Filtra por nota (1-5) |
| POST | `/api/resenhas` | Cria nova resenha |
| PUT | `/api/resenhas/{id}` | Atualiza resenha |
| DELETE | `/api/resenhas/{id}` | Remove resenha |

---

## ⚙️ Funcionalidades Técnicas

### ✅ Paginação e Ordenação
Todos os `GET` de listagem aceitam:
```
?page=0&size=10&sort=titulo&direction=asc
```

### ✅ Projections
- `AutorResumoDTO` — sem bio, apenas dados essenciais
- `LivroResumoDTO` — sem sinopse, com média de notas

### ✅ Spring Cache (Caffeine)
Caches configurados: `autores`, `autor`, `livros`, `livro`, `resenhas`, `resenha`
- TTL: 10 minutos
- Eviction automático em operações de escrita

### ✅ Spring Actuator
```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/caches
curl http://localhost:8080/actuator/metrics
```

### ✅ HATEOAS
Endpoints de `GET /{id}` retornam `_links` com navegação:
```json
{
  "id": 1,
  "titulo": "Dom Casmurro",
  "_links": {
    "self": { "href": "/api/livros/1" },
    "livros": { "href": "/api/livros" },
    "autor": { "href": "/api/autores/1" },
    "resenhas": { "href": "/api/resenhas/livro/1" }
  }
}
```

### ✅ Swagger / OpenAPI
Documentação automática acessível em `/swagger-ui.html` com todos os endpoints, parâmetros e schemas descritos.

---

## 🛠️ Stack

- **Java 21**
- **Spring Boot 3.3**
- **Spring Data JPA** + H2 (dev) / PostgreSQL (prod-ready)
- **Spring Cache** + Caffeine
- **Spring Actuator**
- **Spring HATEOAS**
- **SpringDoc OpenAPI 2.5** (Swagger)
- **Lombok**
- **Bean Validation**

---

## 📦 Dados de Exemplo

O banco é populado automaticamente com:
- 4 autores: Machado de Assis, Clarice Lispector, George Orwell, García Márquez
- 7 livros clássicos da literatura
- 5 resenhas com notas e flags de spoiler

---

*Projeto desenvolvido para a disciplina Java Advanced — Primeiro Semestre*
