# Tecnologias
**Load balancing:** Nginx

Estabelecendo um balanceamento de carga devido à possibilidade de expandir horizontalmente a aplicação, embora compreendamos que a quantidade de chamadas não torne necessário escalar além dos dois contêineres já provisionados.

Comumente, as pessoas pensam que, devido à grande quantidade de mensagens que podem chegar simultaneamente, é necessário abrir várias portas para a aplicação. No entanto, a melhor maneira de garantir a saúde da aplicação é controlar a quantidade de conexões que chegam no backend. Isso permite que a aplicação tenha tempo para realizar todo o processamento internamente e responder a todas as requisições com sucesso.

```json
events {
  use epoll;
  worker_connections 10;
}
```
**epoll** é um mecanismo de I/O que o Nginx pode usar para lidar com muitas conexões simultâneas de forma eficiente. Essa diretiva especifica que o Nginx deve usar o mecanismo epoll para manipulação de eventos.

**worker_connections 10:** Esta diretiva define o número máximo de conexões simultâneas que um único processo worker do Nginx pode manipular.

**Banco de dados:** MongoDB

**Cache:** Redis

Utilizei o redis como cache para minimizar a quantidade de leituras na base de dados  e também como forma de alertar os usuários das notificações.

Aqui inclusive surgiu o grande desafio do projeto, eu não gosto da solução de criar workers planejados com cron jobs, então a solução que ofereci foi manter os estacionamentos em cache e uma chave em cahe para expirar com o TTL baseado no tempo necessário para alertar os usuários necessários, com isso em momentos que a quantidade de chamadas e solicitações é menor os recursos são economizados e não precisamos manter um worker rodando com o cronjob consumindo.

**Servidor de aplicação:** Spring Boot