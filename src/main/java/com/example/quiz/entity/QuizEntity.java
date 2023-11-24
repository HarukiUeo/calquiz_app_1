package com.example.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** QuizControllerのEntityクラス
 * @author 中村
 * @Date 2023_11_16
 * @Version 1.0.1
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="quizinfo")
public class QuizEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String quizName;

	@Column(name="answer")
	private String answer;

	/** quizinfoテーブルにhint列(String型)を追加
	 * @author 中村
	 * @Date 2023_11_16
	 */

	@Column(name="hint")
	private String hint;

}