package org.example.projectjavacard.controllers;

import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    private ClientController clientController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientController = new ClientController(clientService);
    }

    @Test
    void testCreateClient() {
        // Given
        Client client = new Client();
        client.setEmail("test@example.com");

        when(clientService.createClient(client)).thenReturn(client.getId());
        when(clientService.getClientById(client.getId())).thenReturn(client);

        // When
        ResponseEntity<Client> response = clientController.createClient(client);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testGetClient() {
        // Given
        String email = "test@example.com";
        Client client = new Client();
        client.setEmail(email);

        when(clientService.getClientByEmail(email)).thenReturn(client);

        // When
        ResponseEntity<Client> response = clientController.getClient(email);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testGetClientNotFound() {
        // Given
        String email = "test@example.com";

        when(clientService.getClientByEmail(email)).thenReturn(null);

        // When
        ResponseEntity<Client> response = clientController.getClient(email);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}