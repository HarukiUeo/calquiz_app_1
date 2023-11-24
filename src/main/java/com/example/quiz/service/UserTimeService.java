package com.example.quiz.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz.entity.UserTimeEntity;
import com.example.quiz.repository.UserTimeRepository;

/**
 * ユーザーの時間データに対するサービスクラスです。
 * @author Haruki Ueo.
 */
@Service
public class UserTimeService {

	@Autowired
	UserTimeRepository userTimeRepository;


	/**
	 * 現在の時刻を使用してエンティティを作成し、保存します。
	 */
	public void saveEntityWithStartTime(Integer userId) {
		UserTimeEntity userTime= userTimeRepository.findByUserId(userId).get();
		userTime.setStartTime(Instant.now());
		userTimeRepository.save(userTime);
	}

	/**
	 * 指定された時間範囲内のエンティティを取得します。
	 *
	 * @param startTime 検索の開始時間
	 * @param endTime   検索の終了時間
	 * @return 指定された時間範囲内のエンティティのリスト
	 */
	public List<UserTimeEntity> getEntitiesBetweenTimes(Instant startTime, Instant endTime) {
		return userTimeRepository.findByStartTimeBetween(startTime, endTime);
	}

	/**
	 * 開始時間と終了時間の間の経過時間を秒単位で取得します。
	 *
	 * @param startTime 開始時間
	 * @param endTime   終了時間
	 * @return 開始時間から終了時間までの経過時間（秒）
	 */
	public long getElapsedTimeInSeconds(Instant startTime, Instant endTime) {
		Duration duration = Duration.between(startTime, endTime);
		return duration.getSeconds();
	}

	public Instant getStartTimeByUserId(Integer userId) {    
		Optional<UserTimeEntity> userTimeEntityOptional = userTimeRepository.findByUserId(userId);

		if (userTimeEntityOptional.isPresent()) {
			return userTimeEntityOptional.get().getStartTime();
		} else {
			throw new RuntimeException("UserTimeEntityが見つかりませんでした。");
		}
	}
}