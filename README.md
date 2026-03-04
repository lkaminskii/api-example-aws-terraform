# api-example-aws-terraform

Este repositório contém uma aplicação Java simples desenvolvida para demonstrar o uso integrado de LocalStack (simulação de serviços AWS), Docker e Terraform. O objetivo é servir como exemplo técnico para portfólio e chamar atenção de recrutadores, pois o autor é iniciante em desenvolvimento e infraestrutura na nuvem.

---

## Tecnologias Utilizadas

- Java (Spring Boot)
- LocalStack para simulação de serviços AWS (S3, etc.)
- Docker e Docker Compose para containerização
- Terraform para provisionamento de infraestrutura

## Visão Geral do Projeto

A aplicação oferece um endpoint HTTP simples que retorna uma mensagem. Os exemplos incluem a configuração do LocalStack para que você possa testar recursos da AWS localmente sem custo, além de um `docker-compose.yml` para levantar os serviços necessários e um módulo Terraform (`terraform/main.tf`) que demonstra como provisionar recursos na nuvem.

![Terraform resource](assets/AWS-TERRAFORM-QUEUE.png)


