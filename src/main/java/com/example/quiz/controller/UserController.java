package com.example.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.quiz.entity.User;
import com.example.quiz.repository.UserRepository;

/**
* @author Haruki Ueo
* @author Yuma Matui
* @version 1.0.0
 */
@Controller
public class UserController {
	@Autowired
	private UserRepository repository;
	
	@PostMapping("save")
	public String saveTest(@ModelAttribute("user") User user) {
		user=repository.findFirstByOrderByIdDesc();
		user.setId(user.getId());
		user.setName(user.getName());
		user.setScore(user.getScore());
		user.setRank(user.getRank());
		
		repository.save(user);
		
		return "redirect:score";
	}
	
	@GetMapping("score")
	public String showTopScores(Model model) {
		List<User> topScores = repository.findTop10ByOrderByScoreDesc();
		model.addAttribute("topScores",topScores);
		
		User newUser = repository.findFirstByOrderByIdDesc();
		
		if(newUser != null) {
			if(newUser.getRank()==0) {
				List<User> allUsers = repository.findAll();
				int newScore = newUser.getScore();
				int newRank = 1;
				
				for(User user : allUsers) {
					if(user.getScore()>newScore) {
						newRank++;
					}else if(user.getScore()<newScore) {
						int otherRank = user.getRank()+1;
						user.setRank(otherRank);
						repository.save(user);
					}
				}
				newUser.setRank(newRank);
				repository.save(newUser);
			}
			
			model.addAttribute("newUser",newUser);
		}
		return "score";
	}
	
}