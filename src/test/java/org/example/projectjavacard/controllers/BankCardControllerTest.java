package org.example.projectjavacard.controllers;

import org.example.projectjavacard.domain.BankCard;
import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.service.BankCardService;
import org.example.projectjavacard.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BankCardControllerTest {

    @Mock
    private BankCardService bankCardService;

    @Mock
    private ClientService clientService;

    private BankCardController bankCardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bankCardController = new BankCardController(bankCardService, clientService);
    }

    @Test
    void testGenerateBankCard() {
        // Given
        String email = "test@example.com";
        Client client = new Client();
        client.setEmail(email);

        BankCard newCard = new BankCard();
        newCard.setCardNumber("1234 5678 9012 3456");

        when(clientService.getClientByEmail(email)).thenReturn(client);
        when(bankCardService.generateNewCard(client.getId())).thenReturn(newCard);

        // When
        ResponseEntity<BankCard> response = bankCardController.generateBankCard(email);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCard, response.getBody());
    }

    @Test
    void testGenerateBankCardClientNotFound() {
        // Given
        String email = "notfound@example.com";

        when(clientService.getClientByEmail(email)).thenReturn(null);

        // When
        ResponseEntity<BankCard> response = bankCardController.generateBankCard(email);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeactivateBankCard() {
        // Given
        String cardNumber = "1234 5678 9012 3456";
        BankCard card = new BankCard();
        card.setCardNumber(cardNumber);

        when(bankCardService.findByCardNumber(cardNumber)).thenReturn(card);

        // When
        ResponseEntity<String> response = bankCardController.deactivateBankCard(cardNumber);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Карта успешно деактивирована", response.getBody());
        verify(bankCardService, times(1)).deactivateBankCard(card.getId());
    }

    @Test
    void testDeactivateBankCardClientNotFound() {
        // Given
        String cardNumber = "1234 5678 9012 3456";

        when(bankCardService.findByCardNumber(cardNumber)).thenReturn(null);

        // When
        ResponseEntity<String> response = bankCardController.deactivateBankCard(cardNumber);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetClientCards() {
        // Given
        String email = "test@example.com";
        Client client = new Client();
        client.setEmail(email);

        BankCard card1 = new BankCard();
        card1.setCardNumber("1234 5678 9012 3456");
        card1.setOwner(client);

        BankCard card2 = new BankCard();
        card2.setCardNumber("2345 6789 0123 4567");
        card2.setOwner(client);

        List<BankCard> cards = Arrays.asList(card1, card2);

        when(clientService.getClientByEmail(email)).thenReturn(client);
        when(bankCardService.getCards(client.getId())).thenReturn(cards);

        // When
        ResponseEntity<List<BankCard>> response = bankCardController.getClientCards(email);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cards, response.getBody());
    }
}