# Task Killer - Gerenciador de Tarefas

## Descrição
Task Killer é uma API REST para gerenciamento de tarefas, construída com **Spring Boot** e **JWT Authentication**. O sistema oferece controle de acesso baseado em papéis, permitindo que usuários gerenciem suas tarefas com segurança.

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3
- Spring Security (JWT)
- Hibernate / JPA
- H2 Database (para testes)

## Recursos Principais
✅ Autenticação via **JWT** (Header e Cookies)  
✅ Controle de usuários e permissões (**ADMIN, USER**)  
✅ CRUD de **tarefas**, **comentários** e **histórico de alterações**  
✅ Suporte a login persistente (**remember-me**)  

## Estrutura do Projeto
src/main/java/com/protifolio/taskkiller 
├── config/ # Configuração de segurança e JWT 
├── model/ # Entidades do banco de dados (Users, Tasks, Roles, etc.) 
├── repository/ # Interfaces JPA para acesso ao banco 
├── service/ # Lógica de negócio (manipulação de usuários e tarefas) 
├── controller/ # Endpoints REST para interações com a API

## Endpoints Principais
###  AuthRequest
```
{
    "email":"YOUR_EMAIL",
    "password":"YOUR_PASSWORD",
    "rememberMe":true/false
}
```
- `POST /register` → Criação de novo usuário.
- `POST /login` → Autenticação e geração de JWT.
### Users
```
{
    "email":"YOUR_EMAIL",
    "password":"YOUR_PASSWORD",
    "roles":[],
    "profile":{}
}
```
- `GET 		/me` → Retorna informações do usuário autenticado.
- `PUT 		/me` → Edita o usuário autenticado.
- `PATCH 	/me` → Edita informações do usuário autenticado.
- `DELETE 	/me` → Exclui o usuário autenticado, seu perfil, suas tarefas, comentários e históricos de cada tarefa.
### Profile
```
{
    "nome":"YOUR_NAME_OR_TITLE",
    "users":{},
    "task":[]
}
```
- `GET		/me/profile` → Retorna informações do perfil do usuário autenticado.
- `POST		/me/profile` → Cria o perfil do usuário autenticado.
- `PUT		/me/profile` → Edita o perfil do usuário autenticado.
- `PATCH	/me/profile` → Edita informações do perfil do usuário autenticado.
- `DELETE	/me/profile` → Exclui o perfil do usuário autenticado, suas tarefas, comentários e históricos de cada tarefa.
### Task
```
{
    "date":"yyyy-MM-ddTHH:mm:ss",
    "frequency":"DIARIO/SEMANAL/MENSAL/ANUAL"
    "title":"TITLE"
    "description":"DESCRIPTION",
    "status":"PENDENTE/EM_PROGRESSO/CONCLUIDO/CANCELADO",
    "profile":{},
    "taskComments":[],
    "history":[]
}
```
- `GET 		/me/profile/tasks` → Lista as tarefas no perfil do usuário autenticado.
- `POST 	/me/profile/tasks` → Cria uma nova tarefa no perfil do usuário autenticado
- `GET 		/me/profile/tasks/{task_id}` → Retorna informações de uma tarefa específica no perfil do usuário autenticado.
- `PUT 		/me/profile/tasks/{task_id}` → Edita uma tarefa específica no perfil do usuário autenticado.
- `PATCH 	/me/profile/tasks/{task_id}` → Edita informações de uma tarefa específica no perfil do usuário autenticado.
- `DELETE 	/me/profile/tasks/{task_id}` → Deleta uma tarefa específica no perfil do usuário autenticado, com seus comentários e históricos.
### Comment
```
{
    "text":"YOUR_COMMENT",
    "task":{}
}
```
- `GET 		/me/profile/tasks/{task_id}/comments` → Lista os comentários de uma tarefa específica no perfil do usuário autenticado.
- `POST 	/me/profile/tasks/{task_id}/comments/{comment_id}` → Cria um novo comentário em uma tarefa específica no perfil do usuário autenticado.
- `GET 		/me/profile/tasks/{task_id}/comments/{comment_id}` → Retorna um comentário específico de uma tarefa específica no perfil do usuário autenticado.
- `PUT 		/me/profile/tasks/{task_id}/comments/{comment_id}` → Edita um comentário específico de uma tarefa específica no perfil do usuário autenticado.
- `PATCH 	/me/profile/tasks/{task_id}/comments/{comment_id}` → Edita informações de um comentário específico de uma tarefa específica no perfil do usuário autenticado.
- `DELETE 	/me/profile/tasks/{task_id}/comments/{comment_id}` → Exclui um comentário específico de uma tarefa específica no perfil do usuário autenticado, com seu histórico.
### History
```
{
    "oldDescription":"yyyy-MM-ddTHH:mm:ss",
    "newDescription":"DIARIO/SEMANAL/MENSAL/ANUAL"
    "updateData":"yyyy-MM-ddTHH:mm:ss",
    "task":{}
}
```
- `GET 		/me/profile/tasks/{task_id}/history` → Lista o histórico de alterações de uma tarefa específica no perfil do usuário autenticado.
- `POST 	/me/profile/tasks/{task_id}/history/{history_id}` → Cria um registro de histórico de alterações de uma tarefa específica no perfil do usuário autenticado.
- `GET 		/me/profile/tasks/{task_id}/history/{history_id}` → Retorna um registro de histórico específico de alterações de uma tarefa específica no perfil do usuário autenticado.
- `PUT 		/me/profile/tasks/{task_id}/history/{history_id}` → Edita um registro de histórico específico de alterações de uma tarefa específica no perfil do usuário autenticado.
- `PATCH 	/me/profile/tasks/{task_id}/history/{history_id}` → Edita informações de um registro de histórico específico de alterações de uma tarefa específica no perfil do usuário autenticado.
- `DELETE 	/me/profile/tasks/{task_id}/history/{history_id}` → Exclui um registro de histórico específico de alterações de uma tarefa específica no perfil do usuário autenticado.
> **_OBSERVAÇÕES:_**
> ⚠️ Ao alterar uma tarefa, um novo histórico é criado automaticamente.
> ⚠️ Ao rodar a primeira vez, o sistema criará um adm default, apenas para efeitos de teste.
### ADMIN
```
{
    "email":"adm@adm.com",
    "password":"adm123",
    "roles":["ADMIN"],
    "profile":{}
}
```
