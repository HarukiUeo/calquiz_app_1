package com.example.quiz.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
	
/**
 * 新規登録に使うフォーム。
 * @author 浜田真由美
 * 
 */
@Data
public class UserNewLogForm {
    @NotBlank(message = "名前が未入力です")
    private String name;
	
    @NotBlank(message = "パスワードが未入力です")
    private String password;

    @NotBlank(message = "確認用パスワードが未入力です")
    private String confirmPassword;
}
