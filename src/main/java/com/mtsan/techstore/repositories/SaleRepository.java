package com.mtsan.techstore.repositories;

import com.mtsan.techstore.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
	@Query("FROM Sale s WHERE s.dateSold BETWEEN ?1 AND ?2")
	List<Sale> getSalesByTimeRange(Date startDate, Date endDate);

	@Query("FROM Sale s WHERE s.dateSold >= ?1")
	List<Sale> getSalesAfterDate(Date startDate);

	@Query("FROM Sale s WHERE s.dateSold <= ?1")
	List<Sale> getSalesBeforeDate(Date endDate);
}
