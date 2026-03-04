package dev.lucas.hello_api.controller;

import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class MessageController {
    
    private SqsClient sqsClient;

    public MessageController(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
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

}
