package com.mtsan.techstore.repositories;

import com.mtsan.techstore.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

	@Query("FROM Client c WHERE c.merchantId = ?1")
	List<Client> getClientsByMerchantId(Long merchantId);

	@Query("SELECT COUNT(c) FROM Client c WHERE c.merchantId = ?1")
	Long getMerchantsClientsCount(Long merchantId);
}
