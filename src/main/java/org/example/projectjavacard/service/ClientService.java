package org.example.projectjavacard.service;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.repos.ClientRepo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientService {
    private ClientRepo clientRepository;

    public Client createClient(Client client) {
        // Проверяем, существует ли клиент с таким именем
        Client existingClient = clientRepository.findByFullName(client.getFullName());
        if (existingClient != null) {
            return existingClient; // Если клиент уже существует, возвращаем его
        }
        // Если клиент не существует, сохраняем его в базу данных
        return clientRepository.save(client);
    }

}
