package org.example.projectjavacard.service;

import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.repos.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ClientService clientService;

    @Test
    void testCreateClient() {
        // Given
        Client client = new Client();
        client.setFullName("John Doe");
        client.setEmail("john@example.com");
        client.setBirthDate(LocalDate.of(1990, 5, 15));

        // Mock repository behavior and When
        when(clientRepository.save(client)).thenReturn(client);
        Long savedClientId = clientService.createClient(client);
        when(clientRepository.findById(savedClientId)).thenReturn(Optional.of(client));
        Client savedClient = clientRepository.findById(savedClientId).orElse(null);

        // Then
        assertEquals("John Doe", savedClient.getFullName());
        assertEquals("john@example.com", savedClient.getEmail());
        assertEquals(LocalDate.of(1990, 5, 15), savedClient.getBirthDate());
    }

    @Test
    void testGetClientById() {
        // Given
        long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        client.setFullName("Jane Smith");
        client.setEmail("jane@example.com");
        client.setBirthDate(LocalDate.of(1985, 8, 20));

        // Mock repository behavior
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // When
        Optional<Client> retrievedClientOptional = Optional.ofNullable(clientService.getClientById(clientId));

        // Then
        assertEquals(client, retrievedClientOptional.orElse(null));
    }

    @Test
    void testGetClientByEmail() {
        String email = "test@example.com";
        Client client = new Client();
        client.setFullName("Test User");
        client.setEmail(email);
        client.setBirthDate(LocalDate.now());

        when(clientRepository.findByEmail(email)).thenReturn(client);


        Client foundClient = clientService.getClientByEmail(email);

        // Verify that the client was retrieved successfully
        assertNotNull(foundClient);
        assertEquals(email, foundClient.getEmail());
        assertEquals("Test User", foundClient.getFullName());

    }
}