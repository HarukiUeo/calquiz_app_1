package com.example.quiz.service;

import java.util.List;

import com.example.quiz.entity.Quiz;



public interface QuizService {

	/** クイズ全件取得 */
	List<Quiz> selectAllQuiz();
	
	/** クイズを1件取得 */
	Quiz selectOneQuizById(Integer id);
	
}