package dev.lucas.hello_api.controller;

import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import dev.lucas.hello_api.service.SqsMessageService;

@RestController
public class MessageController {
    
    private SqsClient sqsClient;
    private SqsMessageService sqsMessageService;

    public MessageController(SqsClient sqsClient, SqsMessageService sqsMessageService) {
        this.sqsClient = sqsClient;
        this.sqsMessageService = sqsMessageService;
    }

    @GetMapping("/queues")
    public String listQueues() {
        System.out.println("Listing queues...");

        try{
            ListQueuesResponse response = sqsClient.listQueues();
            String queues = response.queueUrls().stream()
                .collect(Collectors.joining(", "));
            
            System.out.println("Queues found: " + queues);
            return "Sqs queues created by Terraform: " + queues;
            
        } catch (Exception e) {
            System.out.println("Error listing queues: " + e.getMessage());
            return "Error connecting to LocalStack: " + e.getMessage();
        }
    }

    @PostMapping("/feed-queue")
    public String feedQueueWithNames() {
        String[] names = {
            "João Silva",
            "Maria Santos",
            "Pedro Oliveira",
            "Ana Costa",
            "Carlos Ferreira",
            "Juliana Marques",
            "Bruno Alves",
            "Fernanda Lima",
            "Ricardo Gomes",
            "Gabriela Souza",
            "Lucas Martins",
            "Camila Ribeiro",
            "Felipe Pereira",
            "Melissa Rocha",
            "Gustavo Mendes",
            "Isadora Cardoso",
            "Rafael Barbosa",
            "Sophie Duarte",
            "Leonardo Tavares",
            "Valentina Campos"
        };
        
        try {
            sqsMessageService.sendMultipleMessages(names);
            return "Successfully sent " + names.length + " names to the queue!";
        } catch (Exception e) {
            System.out.println("Error feeding queue: " + e.getMessage());
            return "Error feeding queue: " + e.getMessage();
        }
    }
}    

