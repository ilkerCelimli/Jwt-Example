package com.securityexample.example.repository;

import com.securityexample.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findByUsernameEquals(String username);

}
