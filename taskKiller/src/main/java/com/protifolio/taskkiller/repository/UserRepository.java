package com.protifolio.taskkiller.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protifolio.taskkiller.model.Users;

public interface UserRepository extends JpaRepository<Users, Long>{
	Optional<Users> getByEmail(String email);
}
