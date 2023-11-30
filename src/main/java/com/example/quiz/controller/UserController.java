package com.example.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.quiz.component.ControllerParameter;
import com.example.quiz.entity.UserEntity;
import com.example.quiz.service.UserServiceImpl;

/**
 * @author Haruki Ueo
 * @author Yuma Matsui
 * @version 1.0.1
 */
@Controller
public class UserController {
	@Autowired
	private UserServiceImpl userService;

	@Autowired
	ControllerParameter ctrParam;

	@PostMapping("score")
	public String showTopScores(
			Model model) {
		if(ctrParam.getPlayer().getId() != ControllerParameter.GUEST_ID) {
			ctrParam.getPlayer().setScore(ctrParam.getUserScore());
			userService.saveUser(ctrParam.getPlayer());
		}
		List<UserEntity> topScores = userService.findTop10ByOrderByScoreDesc();
		for(int i = 0 ; i<topScores.size();i++) {
			if(topScores.get(i).getId() == ControllerParameter.GUEST_ID) {
				topScores.remove(i);
			}
		}
		if(topScores.isEmpty()) {
			model.addAttribute("noRanking",true);
		}else {
			model.addAttribute("topScores",topScores);
		}

		/** スコアを比較してランク付け（ゲストユーザーはランキングに反映しない） */
		List<UserEntity> allUsers = userService.selectAllUsers();
		for(UserEntity user1 : allUsers) {

			// ゲストのランク付けは飛ばす
			if(user1.getId() == ControllerParameter.GUEST_ID) {
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
		if(ctrParam.getPlayer().getId() == ControllerParameter.GUEST_ID) {
			model.addAttribute("isGuest",true);
		}
		model.addAttribute("newUser",userService.selectOneUserById(ctrParam.getPlayer().getId()));
		model.addAttribute("userScore",ctrParam.getUserScore());
		return "score";
	}

	@PostMapping("start")	
	public String startGame(
			Model model) {
		model.addAttribute("user",ctrParam.getPlayer());
		return "start";
	}

}