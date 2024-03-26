package org.example.projectjavacard.controllers;

import lombok.AllArgsConstructor;
import org.example.projectjavacard.domain.Client;
import org.example.projectjavacard.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Long createdClient = clientService.createClient(client);
        return new ResponseEntity<>(clientService.getClientById(createdClient), HttpStatus.CREATED);
    }

    @GetMapping("/{clientEmail}")
    public ResponseEntity<Client> getClient(@PathVariable String clientEmail) {
        Client client = clientService.getClientByEmail(clientEmail);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(client, HttpStatus.OK);
    }
}

