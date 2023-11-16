package com.example.quiz.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** QuizControllerのEntityクラス
 * @author 宮崎　中村
 * @Date 2023_11_16
 * @Version 1.0.1
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
	
	/** quizinfoテーブルにhint列(String型)を追加
	 * @author 中村
	 * @Date 2023_11_16
	 */
	
	/** 問題のヒント */
	private String quizHint;
}

