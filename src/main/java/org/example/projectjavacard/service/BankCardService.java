package org.example.projectjavacard.service;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.BankCard;
import org.example.projectjavacard.repos.BankCardRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class BankCardService {
    private BankCardRepo bankCardRepository;
    private ClientService clientService;
    private final Random random = new Random();

    public BankCard createBankCard(BankCard bankCard) {
        // Здесь можно добавить дополнительные проверки, например, уникальность номера карты
        return bankCardRepository.save(bankCard);
    }

    public void cancelBankCard(Long bankCardId) {
        Optional<BankCard> optionalBankCard = bankCardRepository.findById(bankCardId);
        if (optionalBankCard.isPresent()) {
            BankCard bankCard = optionalBankCard.get();
            // Устанавливаем статус карты как неактивный
            bankCard.setIsActive(false);
            // Сохраняем обновленную карту в базу данных
            bankCardRepository.save(bankCard);
        } else {
            throw new IllegalArgumentException("Bank card with id " + bankCardId + " not found.");
        }
    }

    public List<BankCard> getExpiredBankCards() {
        List<BankCard> list = bankCardRepository.findByExpirationDateBefore(LocalDate.now());
        list.removeIf(bankCard -> !bankCard.getIsActive());
        return list;
    }

    public List<BankCard> getExpiringBankCards(LocalDate currentDate, LocalDate expirationDateWeekFromNow) {
        return bankCardRepository.findByExpirationDateBetween(currentDate, expirationDateWeekFromNow);
    }

    /**
     * Метод для генерации новой карты для клиента
     * Создает карту и сохраняет в базу данных
     * @param id идентификатор клиента
     */
    public void generateNewCard(Long id) {
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
        //todo проверять уникальность номера карты при заведении сравнить с номерами карт в базе

        bankCard.setCardNumber(cardNumber.toString());
        bankCard.setExpirationDate(LocalDate.now().plusYears(4));
        bankCard.setIsActive(true);
        bankCard.setIssueDate(LocalDate.now());
        bankCardRepository.save(bankCard);
    }
}
