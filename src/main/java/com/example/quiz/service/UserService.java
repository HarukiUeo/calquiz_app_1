package com.example.quiz.service;

import java.util.List;

import com.example.quiz.entity.UserEntity;

/**
 * ユーザー情報を取り扱うサービスインターフェース
 * @version 1.0.1
 */

public interface UserService {

	/** ユーザー情報全件取得 */
	List<UserEntity> selectAllUsers();

	/*ユーザー情報を1件取得 */
	UserEntity selectOneUserById(Integer id);

	/** 上位10人のユーザーを取得 */
	List<UserEntity> findTop10ByOrderByScoreDesc();

	/** ユーザー１件取得 */
	UserEntity findFirstByOrderByIdDesc();

	/** ユーザー１件保存 */
	void saveUser(UserEntity user);

}
