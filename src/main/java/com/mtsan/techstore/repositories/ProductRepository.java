package com.mtsan.techstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mtsan.techstore.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
