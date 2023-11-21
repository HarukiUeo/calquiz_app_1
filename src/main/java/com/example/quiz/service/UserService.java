package com.example.quiz.service;

import java.util.List;

import com.example.quiz.entity.User;

/**
 * ユーザー情報を取り扱うサービスインターフェース
 * @version 1.0.1
 */

public interface UserService {
	
	/** ユーザー情報全件取得 */
	List<User> selectAllUsers();

//	/** ゲストユーザー以外のユーザー情報全件取得 */
//	List<User> selectAllUsersExceptGuest();
	
	/*ユーザー情報を1件取得 */
	User selectOneUserById(Integer id);
	
	/** 上位10人のユーザーを取得 */
	List<User> findTop10ByOrderByScoreDesc();
	
	/** ユーザー１件取得 */
	User findFirstByOrderByIdDesc();
	
	/** ユーザー１件保存 */
	void saveUser(User user);
	
}
