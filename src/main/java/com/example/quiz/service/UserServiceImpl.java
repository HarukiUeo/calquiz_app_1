package com.example.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz.entity.UserEntity;
import com.example.quiz.repository.UserRepository;

/**
 * ユーザー情報を取り扱うサービスを実装したクラス
 * @version 1.0.1
 */

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository repository;

	@Override
	public List<UserEntity> selectAllUsers() {
		return repository.findAll();
	}

	@Override
	public UserEntity selectOneUserById(Integer id) {
		// nullかどうかチェックした方がいい
		Optional<UserEntity> userOpt = repository.findById(id);
		return userOpt.get();
	}

	@Override
	public List<UserEntity> findTop10ByOrderByScoreDesc() {
		return repository.findTop10ByOrderByScoreDesc();
	}

	@Override
	public UserEntity findFirstByOrderByIdDesc() {
		return repository.findFirstByOrderByIdDesc();
	}

	@Override
	public void saveUser(UserEntity user) {
		repository.save(user);
	}

}