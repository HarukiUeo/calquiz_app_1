package com.example.quiz.service;

import java.util.List;

import com.example.quiz.entity.User;

public interface UserService {
	
	/** ユーザー情報全件取得 */
	List<User> selectAllUsers();
	
	/*ユーザー情報を1件取得 */
	User selectOneUserById(Integer id);
}
