package com.example.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.entity.Quiz;

/**
 * @author 宮崎・中村
 */
/* CalQuizテーブル:RepositoryImpl */
public interface QuizRepository extends JpaRepository<Quiz,Integer>{
	
	/**
	 * @author Haruki Ueo
	 * @param quizName String型の変数（クイズ名）を受け取ります。
	 * @return 指定した名前を持つ要素。 実装.
	 */
	//Datalnintializationクラスで使用したかったのでfindByNameを作成しました。
	Quiz findByQuizName(String quizName);
}
