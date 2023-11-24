package com.example.quiz.service;

import java.util.List;

import com.example.quiz.entity.QuizEntity;

/** QuizControllerのEntityクラス
 * @author 宮崎　中村
 * @Date 2023_11_16
 * @Version 1.0.1
 */

public interface QuizService {

	/** クイズ全件取得 */
	List<QuizEntity> selectAllQuiz();

	/** クイズを1件取得 */
	QuizEntity selectOneQuizById(Integer id);

	/** クイズカウントメソッドを追加
	 * @author 中村
	 * @Date 2023_11_16
	 * @Version 1.0.1
	 */

	/** 登録されているクイズをカウントする */
	int getCountQuestion();

}