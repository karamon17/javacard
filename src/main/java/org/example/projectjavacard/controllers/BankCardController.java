package org.example.projectjavacard.controllers;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.BankCard;
import org.example.projectjavacard.service.BankCardService;
import org.example.projectjavacard.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cards")
public class BankCardController {

    private final BankCardService bankCardService;
    private final ClientService clientService;

    @GetMapping("/generate")
    public ResponseEntity<BankCard> generateBankCard(@RequestParam String email) {
        BankCard createdCard = bankCardService.generateNewCard(clientService.getClientByEmail(email).getId());
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @GetMapping("/deactivate")
    public ResponseEntity<String> deactivateBankCard(@RequestParam String cardNumber) {
        try {
            bankCardService.deactivateBankCard(bankCardService.findByCardNumber(cardNumber).getId());
            return ResponseEntity.ok("Карта успешно деактивирована");
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>("В базе данных такой карты не найдено", HttpStatus.NOT_FOUND);
        }
        
    }

    @GetMapping("/{clientEmail}")
    public ResponseEntity<List<BankCard>> getClientCards(@PathVariable String clientEmail) {
        List<BankCard> cards = bankCardService.getCards(clientService.getClientByEmail(clientEmail).getId());
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }
}
