# To-Do List

Uma aplicação que propõe ser uma todo-list simples...


## Como configurar?

No ´application.properties´, coloque os seguintes parametros do mysql:
```propeties
spring.datasource.url=

spring.datasource.username=

spring.datasource.password=
```
E também coloque o path para onde vão as imagens da aplicação:
```propeties
file.path=
```

## Como utilizar?

### Manipular task's

[GET] - /api/v1/task -> Devolve todas as task's cadastradas.

[GET] - /api/v1/task/{id} -> Devolve a task's referida.

[POST] - /api/v1/task 

body:
```json
{
    description: string;
}  
```
-> Cadastra uma task.

[PUT] - /api/v1/task/{id}

body:
```json
{
    description: string;
    situation: "PENDING" | "COMPLETED"
}  
```
-> Altera uma task.

[POST] - /api/v1/task/{id}/complete -> Completa uma task.

### Manipular arquivos

[POST] - /api/v1/task/{id}/upload

multipart/form-data: image

-> Faz o upload de uma imagem vinculada a task.

[GET] - /api/v1/task/{id}/image -> Devolve a imagem da task referida.
