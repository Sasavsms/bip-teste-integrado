# 💻 Projeto Integrado — Sistema de Transferências e Benefícios

Este projeto foi desenvolvido como parte do **Desafio Fullstack Integrado**, cujo objetivo era **corrigir um módulo EJB com bugs** e **reimplementar a solução completa utilizando Spring Boot** com as boas práticas modernas de back-end.

---

## 🧠 Contexto do Problema

O módulo original (`ejb-module`) apresentava diversos problemas:
- ❌ Não verificava saldo antes das transferências  
- ❌ Não implementava rollback em caso de erro  
- ❌ Não havia controle de concorrência (locking), podendo causar inconsistências de saldo  

Além disso, o sistema não possuía documentação de API nem um gerenciamento completo de entidades como **contas** e **benefícios**.

---

## ✅ Solução Implementada

A solução foi reestruturada no módulo **`backend-module`**, utilizando o ecossistema Spring Boot com **injeção de dependência, transações, locking e documentação automática via Swagger**.

### 🔧 Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA / Hibernate**
- **Banco de Dados H2 (em memória)**
- **Springdoc OpenAPI (Swagger UI)**
- **Maven**

---

## ⚙️ Arquitetura do Projeto

```
projeto-integrado/
 ├── backend-module/
 │   ├── controller/
 │   │   ├── BeneficioController.java
 │   │   └── TransferController.java
 │   ├── model/
 │   │   ├── Conta.java
 │   │   ├── Beneficio.java
 │   │   ├── Transferencia.java
 │   │   └── TipoTransferencia.java
 │   ├── repository/
 │   │   ├── ContaRepository.java
 │   │   ├── BeneficioRepository.java
 │   │   └── TransferenciaRepository.java
 │   ├── service/
 │   │   ├── BeneficioService.java
 │   │   └── TransferenciaService.java
 │   └── BackendModuleApplication.java
 └── ejb-module/        ← código legado mantido apenas como referência
```

---

## 🏦 Modelagem e Funcionalidades

### 🏦 Entidade **Conta**
Representa as contas bancárias do sistema.

- Campos: `id`, `saldo`, `version`
- Possui controle de concorrência otimista via `@Version`
- Dados iniciais carregados automaticamente via `data.sql`

---

### 💸 Entidade **Transferencia**
Responsável por registrar todo o histórico de movimentações entre contas.

- Campos: `id`, `contaOrigem`, `contaDestino`, `valor`, `dataHora`, `tipo`
- O campo `tipo` é um `enum` (`ENTRADA` ou `SAIDA`)
- Cada operação de transferência gera **duas entradas** (uma de saída e outra de entrada)

#### 🔐 Confiabilidade
- Uso de **`@Transactional`** para rollback automático em caso de erro  
- **Pessimistic Locking** (`LockModeType.PESSIMISTIC_WRITE`) para evitar concorrência de atualização de saldo  

---

### 🎁 Entidade **Beneficio**
Gerencia os benefícios disponíveis no sistema.  
Inclui operações CRUD completas.

- Campos: `id`, `nome`, `descricao`, `valor`, `ativo`, `version`
- Exemplo: *“Vale Alimentação”, “Plano de Saúde”*, etc.

---

## ⚙️ Serviços Implementados

### 🔹 TransferenciaService
Camada de negócio responsável pelas transferências.

Principais métodos:
- `transferir(Long origemId, Long destinoId, Double valor)`  
  → Executa transferência com validação de saldo, rollback e registro no histórico  
- `listarPorConta(Long contaId)`  
  → Retorna todas as transferências (entradas e saídas) associadas a uma conta  

---

### 🔹 BeneficioService
Gerencia operações CRUD sobre a entidade **Beneficio**:
- Criar novo benefício  
- Listar todos  
- Atualizar  
- Deletar  

---

## 🧠 Repositórios

Todos os repositórios utilizam **Spring Data JPA**:
- `ContaRepository`  
  - Contém método `findByIdForUpdate` com `@Lock(PESSIMISTIC_WRITE)`
- `TransferenciaRepository`  
  - Método `findByContaOrigemOrContaDestino`
- `BeneficioRepository`  
  - CRUD padrão do Spring Data

---

## 🧠 Banco de Dados (H2)

Foi utilizado o banco **H2 em memória**, com criação automática de tabelas e carga inicial de dados.

### 📄 Arquivo `application.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.defer-datasource-initialization=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### 🗃️ Arquivo `data.sql`

```sql
INSERT INTO conta (saldo, version) VALUES (1000.00, 0);
INSERT INTO conta (saldo, version) VALUES (500.00, 0);
```

Acesso ao console:  
🔗 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## 📘 Documentação da API (Swagger UI)

A documentação automática foi configurada com **Springdoc OpenAPI**, permitindo visualizar e testar os endpoints via interface web.

### 🔧 Dependência no `pom.xml`

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

### 🔗 Acesso à documentação
- Interface Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Esquema JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🏁 Endpoints Principais

### 🔹 Transferências
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `POST` | `/api/transferencias?origem=1&destino=2&valor=200` | Realiza transferência entre contas |
| `GET` | `/api/transferencias/{contaId}` | Lista histórico de transferências de uma conta |

---

### 🔹 Benefícios
| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `POST` | `/api/beneficios` | Cria um novo benefício |
| `GET` | `/api/beneficios` | Lista todos os benefícios |
| `GET` | `/api/beneficios/{id}` | Busca benefício por ID |
| `PUT` | `/api/beneficios/{id}` | Atualiza benefício existente |
| `DELETE` | `/api/beneficios/{id}` | Remove um benefício |

---

## 🧠 Conclusão

O projeto entrega uma aplicação **robusta, transacional e documentada**, corrigindo completamente o problema original do EJB.  
Com o uso do **Spring Boot**, o sistema agora oferece:
- Transações seguras com rollback automático  
- Controle de concorrência (locking)  
- Histórico completo de transferências  
- Interface Swagger para documentação e testes  
- Banco H2 para execução imediata sem configuração externa  

---

## 👩‍💻 Autora
**Sabriny Macedo**  
📩 [sabrinyvsm@gmail.com](mailto:sabrinyvsm@gmail.com)  
🔗 [linkedin.com/in/victoria-sabriny-macedo-008247336](https://linkedin.com/in/victoria-sabriny-macedo-008247336)  
🐙 [github.com/Sasavsms](https://github.com/Sasavsms)

