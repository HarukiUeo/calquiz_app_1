package com.example.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz.entity.User;
import com.example.quiz.repository.UserRepository;

/**
 * ユーザー情報を取り扱うサービスを実装したクラス
 * @version 1.0.1
 */

@Service
public class ImplUserService implements UserService{
	
	@Autowired
	UserRepository repository;

	@Override
	public List<User> selectAllUsers() {
		return repository.findAll();
	}

	@Override
	public User selectOneUserById(Integer id) {
		// nullかどうかチェックした方がいい
		Optional<User> userOpt = repository.findById(id);
		return userOpt.get();
	}
	
	@Override
	public List<User> findTop10ByOrderByScoreDesc() {
		return repository.findTop10ByOrderByScoreDesc();
	}

	@Override
	public User findFirstByOrderByIdDesc() {
		return repository.findFirstByOrderByIdDesc();
	}

	@Override
	public void saveUser(User user) {
		repository.save(user);
	}

}
