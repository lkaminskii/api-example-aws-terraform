package dev.lucas.hello_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("SqsMessageService Unit Tests")
class SqsMessageServiceTest {

    @Mock
    private SqsClient sqsClientMock;

    private SqsMessageService sqsMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sqsMessageService = new SqsMessageService(sqsClientMock);
    }

    @Test
    @DisplayName("Should send a single message successfully")
    void testSendMessageSuccess() {
        // Arrange
        String messageBody = "Test message";
        String queueUrl = "http://localhost:4566/000000000000/minha-fila-teste";
        String messageId = "msg-123";

        GetQueueUrlResponse queueUrlResponse = GetQueueUrlResponse.builder()
                .queueUrl(queueUrl)
                .build();

        SendMessageResponse sendMessageResponse = SendMessageResponse.builder()
                .messageId(messageId)
                .build();

        when(sqsClientMock.getQueueUrl(any(GetQueueUrlRequest.class)))
                .thenReturn(queueUrlResponse);

        when(sqsClientMock.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(sendMessageResponse);

        // Act
        sqsMessageService.sendMessage(messageBody);

        // Assert
        verify(sqsClientMock, times(1)).getQueueUrl(any(GetQueueUrlRequest.class));
        verify(sqsClientMock, times(1)).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when send message fails")
    void testSendMessageFailure() {
        // Arrange
        String messageBody = "Test message";
        when(sqsClientMock.getQueueUrl(any(GetQueueUrlRequest.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            sqsMessageService.sendMessage(messageBody);
        });

        verify(sqsClientMock, times(1)).getQueueUrl(any(GetQueueUrlRequest.class));
        verify(sqsClientMock, never()).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    @DisplayName("Should send multiple messages successfully")
    void testSendMultipleMessagesSuccess() {
        // Arrange
        String[] messages = { "Message 1", "Message 2", "Message 3" };
        String queueUrl = "http://localhost:4566/000000000000/minha-fila-teste";

        GetQueueUrlResponse queueUrlResponse = GetQueueUrlResponse.builder()
                .queueUrl(queueUrl)
                .build();

        SendMessageResponse sendMessageResponse = SendMessageResponse.builder()
                .messageId("msg-123")
                .build();

        when(sqsClientMock.getQueueUrl(any(GetQueueUrlRequest.class)))
                .thenReturn(queueUrlResponse);

        when(sqsClientMock.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(sendMessageResponse);

        // Act
        sqsMessageService.sendMultipleMessages(messages);

        // Assert
        verify(sqsClientMock, times(messages.length)).getQueueUrl(any(GetQueueUrlRequest.class));
        verify(sqsClientMock, times(messages.length)).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    @DisplayName("Should send empty array without errors")
    void testSendEmptyMessages() {
        // Arrange
        String[] messages = {};

        // Act
        sqsMessageService.sendMultipleMessages(messages);

        // Assert
        verify(sqsClientMock, never()).getQueueUrl(any(GetQueueUrlRequest.class));
        verify(sqsClientMock, never()).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    @DisplayName("Should send 20 names to queue successfully")
    void testSendTwentyNamesSuccess() {
        // Arrange
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

        String queueUrl = "http://localhost:4566/000000000000/minha-fila-teste";

        GetQueueUrlResponse queueUrlResponse = GetQueueUrlResponse.builder()
                .queueUrl(queueUrl)
                .build();

        SendMessageResponse sendMessageResponse = SendMessageResponse.builder()
                .messageId("msg-123")
                .build();

        when(sqsClientMock.getQueueUrl(any(GetQueueUrlRequest.class)))
                .thenReturn(queueUrlResponse);

        when(sqsClientMock.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(sendMessageResponse);

        // Act
        sqsMessageService.sendMultipleMessages(names);

        // Assert
        assertEquals(names.length, 20);
        verify(sqsClientMock, times(names.length)).getQueueUrl(any(GetQueueUrlRequest.class));
        verify(sqsClientMock, times(names.length)).sendMessage(any(SendMessageRequest.class));
    }
}
