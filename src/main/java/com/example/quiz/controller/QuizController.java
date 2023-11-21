package com.example.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.User;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.ImplQuizService;
import com.example.quiz.service.ImplUserService;


/**
* Javadoc用コメントのテストプログラム
* @author 宮崎・中村
* @Date 2023_11_16
* @version 1.0.2
*/

@Controller
public class QuizController {
	
//	boolean[]ox = new boolean[10];
	
//	private Integer quizNum=1;
//	
//	private Integer score=0;

	@Autowired
	ImplQuizService quizService;
	
	@Autowired
	ImplUserService  userService;
	
//	@Autowired
//	private UserRepository repository;
//	
	  /*
	   * QuizIdsetメソッド
	   * formにquizNumをセットする
	   * @param quizNum 整数値
	   * @return form
	   */
	
	@ModelAttribute
	public QuizForm setUpForm() {
		QuizForm form = new QuizForm();
		return form;
	}
	
	/*
	   * playScreen.htmlメソッド
	   * serviceにセットする
	   * @param quizNum 整数値
	   * @return form
	   */
	
	@GetMapping("show")
	public String showPlayScreen(
			@RequestParam Integer userId,
			@RequestParam Integer quizNum,
			@RequestParam List<String> ox,
			@RequestParam String userScore,
			Model model) {
		Quiz quiz = quizService.selectOneQuizById(quizNum);
		User user = userService.selectOneUserById(userId);
		model.addAttribute("quiz",quiz);
		model.addAttribute("user",user);
		model.addAttribute("ox",ox);
		model.addAttribute("userScore",userScore);
		return "playScreen";
	}
	
	//解答チェック機能
	@PostMapping("/submitAnswer")
    public String submitAnswer(
    		@RequestParam("userId") String uId,
    		@RequestParam("quizId") String qId,
    		@RequestParam("ox") List<String> ox,
    		@RequestParam("userScore") String uScore,
    		QuizForm quizForm,
    		Model model,
    		RedirectAttributes redirectAttributes) {
		Integer quizId = Integer.parseInt(qId);
		Integer userId = Integer.parseInt(uId);
		int userScore = Integer.parseInt(uScore);
		redirectAttributes.addAttribute("userId", userId);
		Quiz quiz = quizService.selectOneQuizById(quizId);
		
		if (quizForm.getQuizAnswer().equals(quiz.getAnswer())) {
//		    redirectAttributes.addFlashAttribute("resultMessage","正解です！");

			// result画面用の正誤配列にtrueを入れる
			ox.set(quizId,"true");
			
//			ox[quizId-1] = "true";
			
			// 正解すると以下の点数を加算
			int score = 70;
			userScore += score;
		    
		    Integer quizNum = quizId;
		    quizNum++;
		    
		    if(quizNum > quizService.getCountQuestion()) {
		    	User user = userService.selectOneUserById(userId);
		    	model.addAttribute(user);
		    	ox.remove(0);
		    	model.addAttribute("ox",ox);
		    	model.addAttribute("userScore",userScore);
		    	return "quizResult"; 
		    }
		    redirectAttributes.addAttribute("quizNum", quizNum);
		    redirectAttributes.addAttribute("ox", ox);
		    redirectAttributes.addAttribute("userScore", userScore);
		    return "redirect:/show";
		} else {
		    redirectAttributes.addFlashAttribute("resultMessage", "不正解です・・・");
		    redirectAttributes.addAttribute("quizNum", quizId);
		    redirectAttributes.addAttribute("ox", ox);
		    redirectAttributes.addAttribute("userScore", userScore);
		    return "redirect:/show";
		}
	}
	
	//問題ごとにヒントを表示させる機能
	/**
	 * @Author Kohei Nakamura
	 * @Date 2023_11_16
	 * @param quizForm
	 * @param model
	 * @param redirectAttributes
	 * @return 引数で受け取ったクイズフォームからIDを取得してヒントを返します
	 */
	@PostMapping("/showHint")
	public String showHint(QuizForm quizForm, Model model,RedirectAttributes redirectAttributes){
		Quiz hint = quizService.selectOneQuizById(quizForm.getQuizId());
		redirectAttributes.addFlashAttribute("hint",hint.getHint());
		model.addAttribute("quizId", quizForm.getQuizId());

		return "redirect:/show";
	}
	
	//TOPページへ遷移させる
	@PostMapping("/returnTopPage")
	public String returnTopPage() {
		//userのscoreを全クリアする処理
		return "redirect:showForm";//小林さん担当ページ
	}
	
	
	// ↓これ要る？？
	//スコア・ランキング表示画面へ遷移させる
	@PostMapping("/Result")
	public  String showResult() {
		return "score";//松井さん担当ページ
	}
}