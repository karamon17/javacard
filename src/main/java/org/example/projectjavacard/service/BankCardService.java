package org.example.projectjavacard.service;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.BankCard;
import org.example.projectjavacard.repos.BankCardRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BankCardService {
    private BankCardRepo bankCardRepository;

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
        return bankCardRepository.findByExpirationDateBefore(LocalDate.now());
    }

    public List<BankCard> getExpiringBankCards(LocalDate currentDate, LocalDate expirationDateWeekFromNow) {
        return bankCardRepository.findByExpirationDateBetween(currentDate, expirationDateWeekFromNow);
    }
}
