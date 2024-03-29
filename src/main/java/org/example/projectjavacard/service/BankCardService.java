package org.example.projectjavacard.service;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.BankCard;
import org.example.projectjavacard.repos.BankCardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class BankCardService {
    private BankCardRepository bankCardRepository;
    private ClientService clientService;
    private final Random random = new Random();

    /**
     * Метод для деактивации карты
     * @param bankCardId идентификатор карты
     */
    public void deactivateBankCard(Long bankCardId) {
        Optional<BankCard> optionalBankCard = bankCardRepository.findById(bankCardId);
        if (optionalBankCard.isPresent()) {
            BankCard bankCard = optionalBankCard.get();
            bankCard.setIsActive(false);
            bankCardRepository.save(bankCard);
        } else {
            throw new IllegalArgumentException("Bank card with id " + bankCardId + " not found.");
        }
    }

    /**
     * Метод для получения списка карт, которые уже истекли
     * @return список карт, которые уже истекли
     */
    public List<BankCard> getExpiredBankCards() {
        List<BankCard> list = bankCardRepository.findByExpirationDateBefore(LocalDate.now());
        list.removeIf(bankCard -> !bankCard.getIsActive());
        return list;
    }

    /**
     * Метод для получения списка карт, которые истекают в течение недели
     * @param currentDate текущая дата
     * @param expirationDateWeekFromNow дата, через неделю
     * @return список карт, которые истекают в течение недели
     */
    public List<BankCard> getExpiringBankCards(LocalDate currentDate, LocalDate expirationDateWeekFromNow) {
        return bankCardRepository.findByExpirationDateBetween(currentDate, expirationDateWeekFromNow);
    }

    /**
     * Метод для генерации новой карты для клиента
     * Создает карту, проверяет уникальность и сохраняет в базу данных
     * @param id идентификатор клиента
     */
    public BankCard generateNewCard(Long id) {
        BankCard bankCard = new BankCard();
        bankCard.setOwner(clientService.getClientById(id));

        // Генерируем новый номер карты
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardNumber.append(random.nextInt(10));
            }
            if (i < 3) {
                cardNumber.append(" ");
            }
        }
        //проверка уникальности номера карты, сравнение существующих карт с новой
        String cardNumberStr = cardNumber.toString();
        bankCardRepository.findAll().forEach(bankCard1 -> {
            if (bankCard1.getCardNumber().equals(cardNumberStr)) {
                generateNewCard(id);
            }
        });

        bankCard.setCardNumber(cardNumberStr);
        bankCard.setExpirationDate(LocalDate.now().plusYears(4));
        bankCard.setIsActive(true);
        bankCard.setIssueDate(LocalDate.now());
        bankCardRepository.save(bankCard);
        return bankCard;
    }

    /**
     * Метод для поиска карты по номеру
     * @param cardNumber номер карты
     * @return карта
     */
    public BankCard findByCardNumber(String cardNumber) {
        return bankCardRepository.findByCardNumber(cardNumber);
    }

    /**
     * Метод для получения списка карт клиента
     * @param clientId идентификатор клиента
     * @return список карт клиента
     */
    public List<BankCard> getCards(Long clientId) {
        return bankCardRepository.findByOwnerId(clientId);
    }

    /**
     * Метод для сохранения карты
     * @param card карта
     */
    public void save(BankCard card) {
        bankCardRepository.save(card);
    }
}
