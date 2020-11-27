package com.mtsan.techstore.repositories;

import org.springframework.data.repository.CrudRepository;
import com.mtsan.techstore.entities.Products;

public interface ProductRepository extends CrudRepository<Products, Long> {

}
