package com.example.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.entity.UserTimeEntity;

public interface UserTimeRepository extends JpaRepository<UserTimeEntity, Integer> {

}
