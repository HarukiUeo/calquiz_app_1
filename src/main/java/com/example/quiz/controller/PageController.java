package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.User;
import com.example.quiz.form.UserForm;
import com.example.quiz.form.UserLogForm;
import com.example.quiz.form.UserNewLogForm;
import com.example.quiz.repository.UserRepository;
import com.example.quiz.service.ImplUserService;

/**
 * @author Anzu Kobayasi
 * @vertion 1.0.1
 */
@Controller
public class PageController {
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private ImplUserService userService;
	
	@ModelAttribute
	public UserForm setUpForm() {
		return new UserForm();
	}
	
	@ModelAttribute
	public UserLogForm setUpLogForm() {
		return new UserLogForm();
	}
	
	@ModelAttribute
	public UserNewLogForm setUpNewLogForm() {
		return new UserNewLogForm();
	}

	@GetMapping("showForm")
	public String showForm() {
//		User user = new User();
//		user=repository.findByLoggedinAndId(true,1);
//			user.setScore(0);
//			user.setRank(0);
		
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
		user.setLoggedin(false);
		
		repository.save(user);
		//Modelに格納する
		model.addAttribute("user", user);
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
	public String showQuizView(@RequestParam("userId") String id, Model model,
			RedirectAttributes redirectAttributes) {
		Integer userId = Integer.parseInt(id);
		redirectAttributes.addAttribute("userId", userId);
		//１問目から表示されるように
		redirectAttributes.addAttribute("quizNum", 1);
		User user = userService.selectOneUserById(userId);
		user.setScore(0);
		repository.save(user);
		return "redirect:/show";
	}
	
	
	/**
	 * ユーザー新規登録機能、ログイン機能を実装
	 * @author Mayumi Hamada
	 */
	 // 新規登録処理
	@PostMapping("registerPage")
	public String registerShow() {
		return "registerPage";
	}
	
    @PostMapping("register")
    public String registerUser(@Validated UserNewLogForm form,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "registerPage";
        }
        
        /**
         * ユーザー名がデータベースに存在しないか確認
         * @author Haruki Ueo
         */
        // データベースに同じユーザー名が存在できないようにしました。
        if (repository.findByName(form.getName()) != null) {
        	model.addAttribute("errorMessage", "既に存在するユーザー名です");
        	return "registerPage";
        }
        
        // パスワードと確認用パスワードの一致を確認
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("errorMessage", "パスワードが一致しません");
            return "registerPage";
        }
        
        

        // 新しいユーザーを登録
        User user=new User();
        user.setId(user.getId());
        user.setLoggedin(true);
        user.setName(form.getName());
        user.setPassword(form.getPassword());
        user.setRank(0);
        user.setScore(0);
        repository.save(user);

        // ログイン状態になり、名前入力画面に戻る
        model.addAttribute("user", user);
        return "start";
    }

    // ログイン処理
    @PostMapping("login")
    public String loginUser(@Validated UserLogForm form,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return "top";
        }

        // ユーザー名とパスワードの組み合わせが存在するか確認
        User user = repository.findByNameAndPassword(form.getName(), form.getPassword());
        if (user == null) {
            model.addAttribute("errorMessage", "ユーザー名もしくはパスワードが違います");
            return "top";
        }

        // ログイン状態になり、名前入力画面に戻る
        user.setLoggedin(true);
        repository.save(user);
        model.addAttribute("user", user);
        return "start";
    }
    
    /**
	 * ログアウト機能を実装しました
	 * @author 小林杏
	 */
    // ログアウト処理
    @PostMapping("logout")
    public String logoutUser(Model model) {
        User user = repository.findByLoggedin(true);
        if (user != null) {
            // ログイン状態を解除
            user.setLoggedin(false);
            repository.save(user);
        }
        // 最初の名前入力画面に戻る
        return "top";
    }
}

