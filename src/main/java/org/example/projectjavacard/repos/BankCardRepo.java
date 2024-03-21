package org.example.projectjavacard.repos;

import org.example.projectjavacard.domain.BankCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankCardRepo extends CrudRepository<BankCard, Long> {
    List<BankCard> findByExpirationDateBefore(LocalDate expirationDate);
    List<BankCard> findByExpirationDateBetween(LocalDate currentDate, LocalDate expirationDateWeekFromNow);
}
