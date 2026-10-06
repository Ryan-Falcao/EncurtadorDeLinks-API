# CurtaLink

> Links menores. Ideias maiores.

O **CurtaLink** é um encurtador de URLs criado para transformar links longos em endereços curtos, limpos e fáceis de compartilhar. O projeto foi pensado como uma experiência completa: interface pública, API, persistência de dados, painel de uso e publicação em nuvem.

## O que foi construído

- Criação de links curtos a partir de URLs HTTP e HTTPS;
- Redirecionamento automático para o destino original;
- Reutilização do mesmo código para um mesmo destino;
- Contagem de cliques por link;
- Painel administrativo protegido por senha, com métricas e links recentes;
- Páginas de Privacidade, Termos e Contato;
- Espaços reservados para publicidade;
- Identidade visual própria, leve e com foco em tipografia e tons de azul.

## Arquitetura

```text
Visitante
   ↓
Vercel — interface pública e domínio
   ↓
Render — API Java / Spring Boot
   ↓
Neon — banco de dados PostgreSQL
```

A Vercel encaminha as chamadas da interface para a API e mantém os links curtos sob o mesmo domínio público. O Render executa a aplicação em contêiner Docker e o Neon mantém os dados em PostgreSQL.

## Segurança e cuidados adotados

- Validação de URL realizada também no servidor;
- Aceita apenas destinos `http` e `https`;
- Proteção contra injeção SQL por meio do Spring Data JPA;
- Saída escapada no painel para evitar execução de HTML ou JavaScript vindo de URLs armazenadas;
- Limite de criação de links por IP para reduzir abuso;
- Painel separado e protegido por senha configurada apenas no ambiente de produção;
- Credenciais mantidas em variáveis de ambiente, fora do código-fonte;
- Logs SQL desativados em produção.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Data JPA / Hibernate
- PostgreSQL
- Docker
- Render
- Vercel
- Neon

## Status

Projeto em evolução, com API e banco publicados em nuvem e frontend preparado para a Vercel.

## Autor

**Ryan Marques Monteiro Falcao**

Contato: [linkly.contato@gmail.com](mailto:linkly.contato@gmail.com) (endereço em transição de marca)

---

Este é um projeto pessoal. O código e a identidade do CurtaLink não são disponibilizados como um template de código aberto.
