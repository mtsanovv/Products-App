package com.mtsan.techstore.repositories;

import com.mtsan.techstore.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
