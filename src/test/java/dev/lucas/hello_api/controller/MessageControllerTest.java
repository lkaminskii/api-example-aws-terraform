package dev.lucas.hello_api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

import dev.lucas.hello_api.service.SqsMessageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@DisplayName("MessageController Unit Tests")
class MessageControllerTest {

    @Mock
    private SqsClient sqsClientMock;

    @Mock
    private SqsMessageService sqsMessageServiceMock;

    private MessageController messageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageController = new MessageController(sqsClientMock, sqsMessageServiceMock);
    }

    @Test
    @DisplayName("Should list queues successfully")
    void testListQueuesSuccess() {
        // Arrange
        List<String> queueUrls = Arrays.asList(
                "http://localhost:4566/000000000000/minha-fila-teste"
        );

        ListQueuesResponse response = ListQueuesResponse.builder()
                .queueUrls(queueUrls)
                .build();

        when(sqsClientMock.listQueues()).thenReturn(response);

        // Act
        String result = messageController.listQueues();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Sqs queues created by Terraform"));
        assertTrue(result.contains("minha-fila-teste"));
        verify(sqsClientMock, times(1)).listQueues();
    }

    @Test
    @DisplayName("Should handle exception when listing queues fails")
    void testListQueuesFailure() {
        // Arrange
        when(sqsClientMock.listQueues())
                .thenThrow(new RuntimeException("Connection error"));

        // Act
        String result = messageController.listQueues();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Error connecting to LocalStack"));
        assertTrue(result.contains("Connection error"));
        verify(sqsClientMock, times(1)).listQueues();
    }

    @Test
    @DisplayName("Should feed queue with 20 names successfully")
    void testFeedQueueWithNamesSuccess() {
        // Arrange
        doNothing().when(sqsMessageServiceMock).sendMultipleMessages(any(String[].class));

        // Act
        String result = messageController.feedQueueWithNames();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Successfully sent"));
        assertTrue(result.contains("20"));
        assertTrue(result.contains("names to the queue"));
        verify(sqsMessageServiceMock, times(1)).sendMultipleMessages(any(String[].class));
    }

    @Test
    @DisplayName("Should feed queue with exactly 20 names")
    void testFeedQueueWithExactlyTwentyNames() {
        // Arrange
        doNothing().when(sqsMessageServiceMock).sendMultipleMessages(any(String[].class));

        // Act
        String result = messageController.feedQueueWithNames();

        // Assert
        assertNotNull(result);
        assertEquals("Successfully sent 20 names to the queue!", result);
        verify(sqsMessageServiceMock, times(1)).sendMultipleMessages(any(String[].class));
    }

    @Test
    @DisplayName("Should handle exception when feeding queue fails")
    void testFeedQueueFailure() {
        // Arrange
        doThrow(new RuntimeException("Queue service unavailable"))
                .when(sqsMessageServiceMock).sendMultipleMessages(any(String[].class));

        // Act
        String result = messageController.feedQueueWithNames();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Error feeding queue"));
        assertTrue(result.contains("Queue service unavailable"));
        verify(sqsMessageServiceMock, times(1)).sendMultipleMessages(any(String[].class));
    }

    @Test
    @DisplayName("Should list multiple queues successfully")
    void testListMultipleQueuesSuccess() {
        // Arrange
        List<String> queueUrls = Arrays.asList(
                "http://localhost:4566/000000000000/fila-1",
                "http://localhost:4566/000000000000/fila-2",
                "http://localhost:4566/000000000000/fila-3"
        );

        ListQueuesResponse response = ListQueuesResponse.builder()
                .queueUrls(queueUrls)
                .build();

        when(sqsClientMock.listQueues()).thenReturn(response);

        // Act
        String result = messageController.listQueues();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("fila-1"));
        assertTrue(result.contains("fila-2"));
        assertTrue(result.contains("fila-3"));
        assertEquals(3, queueUrls.size());
        verify(sqsClientMock, times(1)).listQueues();
    }

    @Test
    @DisplayName("Should return non-null response in all scenarios")
    void testAllMethodsReturnNonNullResponse() {
        // Arrange
        ListQueuesResponse response = ListQueuesResponse.builder()
                .queueUrls(Arrays.asList("http://localhost:4566/000000000000/test-queue"))
                .build();

        when(sqsClientMock.listQueues()).thenReturn(response);
        doNothing().when(sqsMessageServiceMock).sendMultipleMessages(any(String[].class));

        // Act
        String listResult = messageController.listQueues();
        String feedResult = messageController.feedQueueWithNames();

        // Assert
        assertNotNull(listResult);
        assertNotNull(feedResult);
        assertTrue(listResult.length() > 0);
        assertTrue(feedResult.length() > 0);
    }
}
