package com.example.quiz.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ゲストログインに使うフォーム。
 * @author 浜田真由美
 * 
 */
@Data
public class UserForm {
	@NotBlank(message = "名前が未入力です")
	private String name;

}