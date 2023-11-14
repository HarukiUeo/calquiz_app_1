package com.example.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.entity.User;

/**
 * Userエンティティを管理するためのリポジトリインターフェースです。
 * このインターフェースはJpaRepositoryを拡張して、基本的なCRUD操作を継承しています。
 * @author Haruki Ueo
 * @author Yuma Matui
 * @version 1.0.0
 */
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * スコアの降順で上位10件のユーザーを取得します。
	 *
	 * @return 上位10件のユーザーリスト。
	 */
	List<User> findTop10ByOrderByScoreDesc();

	/**
	 * IDの降順で最初のユーザーを取得します。
	 *
	 * @return IDの降順で最初のユーザー。
	 */
	User findFirstByOrderByIdDesc();
}
