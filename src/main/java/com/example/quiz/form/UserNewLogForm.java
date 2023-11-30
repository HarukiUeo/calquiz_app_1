package com.example.quiz.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 新規登録に使うフォームです。
 * @author Mayumi Hamada
 */

@Data
public class UserNewLogForm {

		@NotBlank(message="名前を入力してください")
		@Pattern(regexp=".{1,20}", message="20文字以内で入力してください")
		private String name;
		
		@NotBlank(message="パスワードを入力してください")
		@Pattern(regexp=".{1,10}", message="10文字以内で入力してください")
		private String password;
		
		@NotBlank(message="確認用パスワードを入力してください")
		private String confirmPassword;	
}