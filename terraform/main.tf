terraform {
    required_version = ">= 1.0"
    required_providers {
        aws = {
            source = "hashicorp/aws"
            version = "~> 5.0"

            }
        }
    }

    provider "aws" {
        region                      = "us-east-1"
        access_key                  = "test"
        secret_key                  = "test"
        skip_credentials_validation = true
        skip_metadata_api_check     = true
        skip_requesting_account_id  = true

        endpoints {
            sqs = "http://localhost:4566"
            sns = "http://localhost:4566"
        }
        max_retries = 2

    }

resource "aws_sqs_queue" "minha_fila" {
    name                      = "minha-fila-teste"
    delay_seconds             = 0
    max_message_size          = 2048
    message_retention_seconds = 86400
    receive_wait_time_seconds = 0
}

resource "aws_sns_topic" "meu_topico" {
    name = "meu-topico-teste"
}


output "queue_url" {
    value = aws_sqs_queue.minha_fila.id
}

output "topic_arn" {
    value = aws_sns_topic.meu_topico.arn
}

