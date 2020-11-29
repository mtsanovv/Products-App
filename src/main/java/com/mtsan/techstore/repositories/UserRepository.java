package com.mtsan.techstore.repositories;

import com.mtsan.techstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT * FROM users WHERE rank = ?1", nativeQuery = true)
	List<User> getUserByRank(String rank);

}
