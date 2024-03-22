package org.example.projectjavacard.service;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.repos.ClientRepo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientService {
    private ClientRepo clientRepository;



    public Long createClient(Client client) {
        // Проверяем, существует ли клиент с таким именем
        Client existingClient = clientRepository.findByFullName(client.getFullName());
        if (existingClient != null) {
            return existingClient.getId(); // Если клиент уже существует, возвращаем его id
        }
        // Если клиент не существует, сохраняем его в базу данных
        clientRepository.save(client);
        return client.getId();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Client with id " + id + " not found."));
    }
}
