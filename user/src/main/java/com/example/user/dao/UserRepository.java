package com.example.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.domain.entity.UserEntity;

// 객체 생성을 해서 apring container에 위임 시키는 역할
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{

    public Optional<UserEntity> findByEmailAndPassword(String email, String password);
}
