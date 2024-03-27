package org.example.projectjavacard.service;

import org.example.projectjavacard.domain.BankCard;
import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.repos.BankCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankCardServiceTest {
    @Mock
    private BankCardRepository bankCardRepository;
    @Mock
    private ClientService clientService;
    @InjectMocks
    private BankCardService bankCardService;

    @Test
    void testDeactivateBankCard() {
        // Given
        BankCard bankCard = new BankCard();
        bankCard.setIsActive(true);

        // Mock repository behavior
        when(bankCardRepository.findById(anyLong())).thenReturn(Optional.of(bankCard));

        // When
        bankCardService.deactivateBankCard(1L);

        // Then
        assertFalse(bankCard.getIsActive());
    }

    @Test
    void testGetExpiredBankCards() {
        // Given
        BankCard expiredCard = new BankCard();
        expiredCard.setExpirationDate(LocalDate.now().minusDays(1));
        expiredCard.setIsActive(true);

        BankCard activeCard = new BankCard();
        activeCard.setExpirationDate(LocalDate.now().plusDays(1));
        activeCard.setIsActive(false);

        List<BankCard> list = new ArrayList<>();
        list.add(expiredCard);
        list.add(activeCard);

        // Mock repository behavior
        when(bankCardRepository.findByExpirationDateBefore(any(LocalDate.class)))
                .thenReturn(list);

        // When
        List<BankCard> expiredCards = bankCardService.getExpiredBankCards();

        // Then
        assertEquals(1, expiredCards.size());
        assertEquals(expiredCard, expiredCards.get(0));
    }

    @Test
    void testGenerateNewCard() {
        // Given
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        when(clientService.getClientById(clientId)).thenReturn(client);

        // Mock repository behavior
        BankCard existingCard1 = new BankCard();
        existingCard1.setCardNumber("1111 1111 1111 1111");
        existingCard1.setExpirationDate(LocalDate.now().plusYears(1));
        existingCard1.setIsActive(true);
        existingCard1.setIssueDate(LocalDate.now());
        existingCard1.setOwner(client);

        BankCard existingCard2 = new BankCard();
        existingCard2.setCardNumber("2222 2222 2222 2222");
        existingCard2.setExpirationDate(LocalDate.now().plusYears(2));
        existingCard2.setIsActive(true);
        existingCard2.setIssueDate(LocalDate.now());
        existingCard2.setOwner(client);

        List<BankCard> existingCards = Arrays.asList(existingCard1, existingCard2);
        when(bankCardRepository.findAll()).thenReturn(existingCards);

        // When
        BankCard generatedCard = bankCardService.generateNewCard(clientId);

        // Then
        assertNotNull(generatedCard);
        assertEquals(client, generatedCard.getOwner());
        assertNotEquals("1111 1111 1111 1111", generatedCard.getCardNumber());
        assertNotEquals("2222 2222 2222 2222", generatedCard.getCardNumber());
        assertTrue(generatedCard.getIsActive());
        assertEquals(LocalDate.now().plusYears(4), generatedCard.getExpirationDate());
        assertEquals(LocalDate.now(), generatedCard.getIssueDate());
        verify(bankCardRepository, times(1)).save(any(BankCard.class));
    }

}