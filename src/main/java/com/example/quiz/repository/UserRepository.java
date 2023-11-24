package com.example.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.entity.UserEntity;

/**
 * Userエンティティを管理するためのリポジトリインターフェースです。
 * このインターフェースはJpaRepositoryを拡張して、基本的なCRUD操作を継承しています。
 * @author Haruki Ueo
 * @author Yuma Matsui
 * @version 1.0.2
 */

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	/**
	 * スコアの降順で上位10件のユーザーを取得します。
	 *
	 * @return 上位10件のユーザーリスト。
	 */
	List<UserEntity> findTop10ByOrderByScoreDesc();

	/**
	 * IDの降順で最初のユーザーを取得します。
	 *
	 * @return IDの降順で最初のユーザー。
	 */
	UserEntity findFirstByOrderByIdDesc();

	/**
	 * ユーザー名でユーザーを取得、ログイン状態であるユーザーの取得、ユーザー名とパスワードでユーザーを取得
	 * 以上の機能を追加しました。
	 * @author 浜田真由美
	 * 
	 */
	//大文字小文字を変えないでください。バグるよ！！
	UserEntity findByName(String name);

	UserEntity findByLoggedin(Boolean loggedIn);

	UserEntity findByNameAndPassword(String name, String password);
	/**
	 * ログイン状態かつユーザーIDが一致するユーザーを取得する
	 * @param loggedIn ユーザーのログイン状態
	 * @param userId ユーザーのID
	 * @return　引数で受け取ったloggedIn,userIdが一致するユーザーを返す
	 * @author Yuma Matsui
	 */
	UserEntity findByLoggedinAndId(Boolean loggedIn,Integer userId);
}