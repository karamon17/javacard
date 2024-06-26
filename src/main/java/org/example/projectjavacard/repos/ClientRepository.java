package org.example.projectjavacard.repos;

import org.example.projectjavacard.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long>{
    Client findByEmail(String email);
}
