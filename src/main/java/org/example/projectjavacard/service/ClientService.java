package org.example.projectjavacard.service;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.repos.ClientRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientService {
    private ClientRepository clientRepository;

    /**
     * Метод для создания клиента
     * @param client клиент
     * @return id созданного клиента
     */
    public Long createClient(Client client) {
        // Проверяем, существует ли клиент с таким же email
        Client existingClient = clientRepository.findByEmail(client.getEmail());
        if (existingClient != null) {
            return existingClient.getId(); // Если клиент уже существует, возвращаем его id
        }
        // Если клиент не существует, сохраняем его в базу данных
        clientRepository.save(client);
        return client.getId();
    }

    /**
     * Метод для получения клиента по id
     * @param id идентификатор клиента
     * @return клиент
     */
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Client with id " + id + " not found."));
    }

    /**
     * Метод для получения клиента по email
     * @param email email клиента
     * @return клиент
     */
    public Client getClientByEmail(String email) {
        if (clientRepository.findByEmail(email) == null)
            throw new IllegalArgumentException("Client with email " + email + " not found.");
        return clientRepository.findByEmail(email);
    }
}
