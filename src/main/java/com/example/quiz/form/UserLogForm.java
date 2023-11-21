package com.example.quiz.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ログインに使うフォーム。
 * @author 浜田真由美
 * 
 */
@Data
public class UserLogForm {
	@NotBlank(message = "名前が未入力です")
	private String name;

	@NotBlank(message = "パスワードが未入力です")
	private String password;
}
