package com.example.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz.entity.User;
import com.example.quiz.repository.UserRepository;

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
}
