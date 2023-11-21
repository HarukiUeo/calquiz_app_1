package com.example.quiz.component;


import org.springframework.stereotype.Component;

import com.example.quiz.entity.User;
import com.example.quiz.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.Data;

/**
 * ゲストアカウント初期値設定クラスです。
 * @author HarukiUeo
 */
@Component
@Data
public class UserDataInitialization {
	
	private final UserRepository userRepository;
	
	@PostConstruct
    public void init() {
        createAndSaveUserIfNotExists();
    }
	
	 private void createAndSaveUserIfNotExists() {
	        // 既存のデータを探す
	        User existingName = userRepository.findByName("ゲスト");

	        if (existingName == null) {
	            // 既存のデータがない場合に新しいデータを作成して保存
	            User user = new User();
	            user.setId(2);
	            user.setName("ゲスト");
	            user.setPassword("gest");
	            user.setLoggedin(false);
	            user.setRank(0);
	            user.setScore(0);

	            userRepository.save(user);
	        }
	    }
}
