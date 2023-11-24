package com.example.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz.entity.QuizEntity;
import com.example.quiz.repository.QuizRepository;

/** 
 * @author 宮崎　中村
 * @Date 2023_11_16
 * @Version 1.0.1
 */

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	QuizRepository repository;

	@Override
	public List<QuizEntity> selectAllQuiz() {
		return repository.findAll();
	}

	@Override
	public QuizEntity selectOneQuizById(Integer id) {
		// nullかどうかチェックした方がいい
		Optional<QuizEntity> quizOpt = repository.findById(id);
		return quizOpt.get();
	}

	/** クイズカウントメソッドを追加
	 * @author 中村
	 * @Date 2023_11_16
	 * @Version 1.0.1
	 */

	@Override
	public int getCountQuestion() {
		// int以上の問題数は追加しない＆longは使いづらいため
		int quizTotal = (int) repository.count();
		return quizTotal;
	}


}