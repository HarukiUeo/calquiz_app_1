package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.quiz.entity.User;
import com.example.quiz.form.UserForm;
import com.example.quiz.repository.UserRepository;

/**
 * @author Anzu Kobayasi
 * @vertion 1.0.0
 */
@Controller
public class PageController {
	@Autowired
	private UserRepository repository;
	
	@ModelAttribute
	public UserForm setUpForm() {
		return new UserForm();
	}

	@GetMapping("showForm")
	public String showForm() {
		return "top";
	}
	
	@PostMapping("confirm")
	public String showConfirm(@Validated UserForm form,
			BindingResult bindingResult,Model model) {
		if(bindingResult.hasErrors()) {
			return "top";
		}
		User user = new User();
		user.setName(form.getName());
		user.setScore(0);
		user.setRank(0);
		repository.save(user);
		//Modelに格納する
		model.addAttribute("name", user.getName());
		return "start";
	}
	
	/**
	 * "/send" への POST リクエストを処理し、"/show" にリダイレクトします。
	 *
	 * @author Haruki Ueo
	 * @return "/show" にリダイレクトするためのビューを表す文字列。
	 */
	//redirectじゃないとplayScreen.htmlのquiz.idにnull値が入るようになっていたので修正しています。
	//恐らくQuizControllerが上手く処理されない為。
	@PostMapping("send")
	public String showQuizView() {
		return "redirect:/show";
	}
}
