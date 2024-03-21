package org.example.projectjavacard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "bank_cards")
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cardNumber;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client owner;

    @Override
    public String toString() {
        return "BankCard{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", isActive=" + isActive +
                ", owner=" + owner +
                '}';
    }
}
