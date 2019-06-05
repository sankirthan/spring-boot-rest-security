package com.example.springbootrestsecurity.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootrestsecurity.jpa.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	@Transactional
	@Modifying
	@Query("update User set failedLoginCount = failedLoginCount + 1 where username = ?1")
	public void incrementFailedLoginCount(String username);
	
	@Transactional
	@Modifying
	@Query("update User set failedLoginCount = failedLoginCount + 1, locked = true where username = ?1")
	public void incrementFailedLoginCountAndLock(String username);
	
	@Transactional
	@Modifying
	@Query("update User set locked = false where username = ?1")
	public void unlock(String username);
}
