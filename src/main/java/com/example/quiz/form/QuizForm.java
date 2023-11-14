package com.example.quiz.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 宮崎・中村
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizForm {
	
	/** 問題のID */
	private Integer quizId;
	
	/** 問題の名前 */
//	private String quizName;
	
	/** 問題の解答 */
	private String quizAnswer;
	
}

