package org.example.projectjavacard.scheduler;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.BankCard;
import org.example.projectjavacard.service.BankCardService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
@EnableScheduling
public class NotificationScheduler {

    private final BankCardService bankCardService;
    private final JavaMailSender emailSender;

    // Метод, который будет запускаться каждый день в определенное время (например, в полночь)
    @Scheduled(cron = "*/3 * * * * *")
    public void sendExpirationNotifications() {
        System.out.println("Sending expiration notifications");
        // Получаем список карт, которые истекают сегодня и отправляем уведомления
        List<BankCard> expiredCards = bankCardService.getExpiredBankCards();
        for (BankCard card : expiredCards) {
            String message = "Уважаемый клиент, ваша карта " + card.getCardNumber() + " истекла";
            System.out.println("Sending message: " + message);
            bankCardService.cancelBankCard(card.getId());
//            System.out.println(card.getOwner().getEmail());
            sendNotification(card.getOwner().getEmail(), message);
            //сюда добавить автоматический выпуск новой карты
        }

        // Получаем список карт, которые истекают через неделю
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDateWeekFromNow = currentDate.plusWeeks(1);
        List<BankCard> expiringCards = bankCardService.getExpiringBankCards(currentDate, expirationDateWeekFromNow);

        // Отправляем предупреждающие уведомления о картах, истекающих через неделю
        for (BankCard card : expiringCards) {
            String message = "Уважаемый клиент, ваша карта " + card.getCardNumber() + " истекает через неделю";
            System.out.println("Sending message: " + message);
            sendNotification(card.getOwner().getEmail(), message);
        }
    }

    private void sendNotification(String recipient, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient);
        mailMessage.setSubject("Уведомление о вашей банковской карте");
        mailMessage.setText(message);
        emailSender.send(mailMessage);
    }
}
