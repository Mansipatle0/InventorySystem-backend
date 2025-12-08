package com.smartinventory.backend.repository;



import com.smartinventory.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	   @Query(value = """
		        SELECT COUNT(*)
		        FROM users u
		        WHERE u.active = 1
		        """, nativeQuery = true)
		    long countActiveUsers();
	   User findByEmail(String email);
}
