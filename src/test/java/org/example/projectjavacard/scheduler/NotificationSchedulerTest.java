package org.example.projectjavacard.scheduler;

import org.example.projectjavacard.domain.BankCard;
import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.service.BankCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;

class NotificationSchedulerTest {

    @Mock
    private BankCardService bankCardService;

    @Mock
    private JavaMailSender emailSender;

    private NotificationScheduler notificationScheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationScheduler = new NotificationScheduler(bankCardService, emailSender);
    }

    @Test
    void testSendExpirationNotifications() {
        // Given
        Client client = new Client();
        client.setEmail("test@example.com");

        BankCard expiredCard = new BankCard();
        expiredCard.setOwner(client);
        expiredCard.setCardNumber("1111 1111 1111 1111");

        BankCard expiringCard = new BankCard();
        expiringCard.setOwner(client);
        expiringCard.setCardNumber("2222 2222 2222 2222");

        when(bankCardService.getExpiredBankCards()).thenReturn(Arrays.asList(expiredCard));
        when(bankCardService.getExpiringBankCards(any(LocalDate.class), any(LocalDate.class))).thenReturn(Arrays.asList(expiringCard));

        // When
        notificationScheduler.sendExpirationNotifications();

        // Then
        verify(bankCardService, times(1)).deactivateBankCard(expiredCard.getId());
        verify(bankCardService, times(1)).generateNewCard(client.getId());
        verify(emailSender, times(2)).send(any(SimpleMailMessage.class));
    }
}