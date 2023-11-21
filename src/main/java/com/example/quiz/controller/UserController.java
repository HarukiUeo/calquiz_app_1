package com.example.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.quiz.entity.UserEntity;
import com.example.quiz.service.UserServiceImpl;

/**
 * @author Haruki Ueo
 * @author Yuma Matui
 * @version 1.0.1
 */
@Controller
public class UserController {
	@Autowired
	private UserServiceImpl userService;



	@PostMapping("score")
	public String showTopScores(
			@RequestParam("userId") String id,
			@RequestParam("userScore") String uScore,
			Model model) {

		Integer userId = Integer.parseInt(id);
		Integer userScore = Integer.parseInt(uScore);
		UserEntity user = userService.selectOneUserById(userId);


		// IDが0はゲスト(アカウント持ちユーザーのみデータベースに保存)
		if(userId!=0) {
			user.setScore(userScore);
			userService.saveUser(user);
		}

		List<UserEntity> topScores = userService.findTop10ByOrderByScoreDesc();
		model.addAttribute("topScores",topScores);

		/** スコアを比較してランク付け（ゲストユーザーはランキングに反映しない） */
		List<UserEntity> allUsers = userService.selectAllUsers();
		for(UserEntity user1 : allUsers) {

			// ゲストのランク付けは飛ばす
			if(user1.getId()==0) {
				continue;
			}

			int playerRank = 1;
			for(UserEntity user2 : allUsers) {
				if(user2.getScore()>user1.getScore()) {
					playerRank++;
				}
			}
			user1.setRank(playerRank);
			userService.saveUser(user1);
		}

		model.addAttribute("newUser",user);
		model.addAttribute("userScore",userScore);
		return "score";
	}

	//ユーザーIDを保持したままスタートページに移動
	@PostMapping("start")	
	public String startGame(@RequestParam("userId") String id,Model model) {
		Integer userId = Integer.parseInt(id);
		UserEntity user = userService.selectOneUserById(userId);
		model.addAttribute("user",user);
		return "start";
	}

}