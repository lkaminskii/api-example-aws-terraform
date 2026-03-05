package dev.lucas.hello_api.service;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;

@Service
public class SqsMessageService {
    
    private SqsClient sqsClient;
    private static final String QUEUE_NAME = "minha-fila-teste";
    
    public SqsMessageService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }
    
    public void sendMessage(String message) {
        try {
            GetQueueUrlRequest queueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(QUEUE_NAME)
                .build();
            
            GetQueueUrlResponse queueUrlResponse = sqsClient.getQueueUrl(queueUrlRequest);
            String queueUrl = queueUrlResponse.queueUrl();
            
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
            
            SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
            System.out.println("Message sent with ID: " + sendMessageResponse.messageId());
            
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
            throw new RuntimeException("Failed to send message to SQS queue", e);
        }
    }
    
    public void sendMultipleMessages(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }
}
