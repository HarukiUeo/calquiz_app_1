package com.example.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;

@Service
public class ImplQuizService implements QuizService {

    @Autowired
    QuizRepository repository;

	@Override
	public List<Quiz> selectAllQuiz() {
		return repository.findAll();
	}

	@Override
	public Quiz selectOneQuizById(Integer id) {
		// nullかどうかチェックした方がいい
		Optional<Quiz> quizOpt = repository.findById(id);
		return quizOpt.get();
	}

}