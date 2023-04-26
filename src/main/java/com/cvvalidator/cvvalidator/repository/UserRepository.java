package com.cvvalidator.cvvalidator.repository;

import java.util.Optional;

import com.cvvalidator.cvvalidator.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // @Query("SELECT u FROM User u WHERE u.email = :username and u.password = :password")
    // boolean signIn(@Param("username") String username,@Param("password") String password);
    Optional<User> findByEmail(String email);
}
