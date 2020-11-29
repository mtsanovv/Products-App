package com.mtsan.techstore.repositories;

import org.springframework.data.repository.CrudRepository;
import com.mtsan.techstore.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
