package com.example.quiz.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.entity.UserTimeEntity;

/**
 * ユーザーの時間エンティティ（UserTimeEntity）に対するリポジトリインターフェースです。
 * @author Haruki Ueo.
 */
public interface UserTimeRepository extends JpaRepository<UserTimeEntity, Integer> {

	/**
	 * 指定された時間範囲内のエンティティを取得します。
	 *
	 * @param startTime ゲームの開始時に作成されたInstant型のstartTime
	 * @param endTime ゲームの終了時に作成されたInstant型のendTime
	 * @return 指定された時間範囲内のエンティティのリスト
	 */
	List<UserTimeEntity> findByStartTimeBetween(Instant startTime, Instant endTime);

	// UserエンティティのIDに基づいてUserTimeEntityを取得
	Optional<UserTimeEntity> findByUserId(Integer userId);
}

