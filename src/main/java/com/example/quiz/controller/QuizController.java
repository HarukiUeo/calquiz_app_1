package com.example.quiz.controller;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.quiz.entity.QuizEntity;
import com.example.quiz.entity.UserEntity;
import com.example.quiz.form.QuizForm;
import com.example.quiz.repository.UserTimeRepository;
import com.example.quiz.service.QuizServiceImpl;
import com.example.quiz.service.UserServiceImpl;
import com.example.quiz.service.UserTimeService;


/**
 * Javadoc用コメントのテストプログラム
 * @author 宮崎・中村
 * @Date 2023_11_16
 * @version 1.0.2
 */

@Controller
public class QuizController {


	@Autowired
	QuizServiceImpl quizService;

	@Autowired
	UserServiceImpl  userService;

	@Autowired
	UserTimeService userTimeService;

	@Autowired
	UserTimeRepository userTimeRepository;
	
	/** クイズの１問目 */
	public static final Integer firstQuiz = 1;
	
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
	
	/**
	 * 	解答の正誤リストを作る（初期値：false）
	 * @return 解答の正誤リスト（String）
	 */
	public List<String> setUpOxList(){
		List<String> ox = new LinkedList<>();
		for(int i=0;i<quizService.getCountQuestion()+1;i++) {
			ox.add("false");
		}
		return ox;
	}
	
	/**
	 * ゲスト以外のプレイヤーがゲームをプレイするとスタートした時間が保存される
	 * @author HarukiUeo
	 */
	public void setTimeExceptGuest(Integer userId) {
		if(!userId.equals(PageController.GUEST_ID)) {
			userTimeService.saveEntityWithStartTime(userId);
		}
	}
	
	
	
	/*
	 * playScreen.htmlメソッド
	 * serviceにセットする
	 * @param quizNum 整数値
	 * @return form
	 */

	@PostMapping("show")
	public String showPlayScreen(
			@RequestParam("userId") String uId,
//			@RequestParam("quizId") String qId,
//			@RequestParam("ox") List<String> ox,
//			@RequestParam("userScore") String uScore,
			Model model) {
		
		//スコア管理(正誤用配列・スコア)は最後にまとめてデータベースに保存するためhtml間でやり取りをする
		List<String> ox = setUpOxList();
		model.addAttribute("ox", ox);
		
		String userScore = "0";
		
//		Integer quizId = Integer.parseInt(qId);
		Integer userId = Integer.parseInt(uId);
//		int userScore = Integer.parseInt(uScore);
		QuizEntity quiz = quizService.selectOneQuizById(firstQuiz);
		UserEntity user = userService.selectOneUserById(userId);
		model.addAttribute("quiz",quiz);
		model.addAttribute("user",user);
		model.addAttribute("ox",ox);
		model.addAttribute("userScore",userScore);
		
		// 1問目のタイマースタート
		setTimeExceptGuest(userId);
		
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
			Model model) {
		Integer quizId = Integer.parseInt(qId);
		Integer userId = Integer.parseInt(uId);
		int userScore = Integer.parseInt(uScore);
		QuizEntity quiz = quizService.selectOneQuizById(quizId);
		UserEntity user = userService.selectOneUserById(userId);
		
		// 正解・不正解関係なくユーザーをモデルに格納
		model.addAttribute("user", user);


		if (quizForm.getQuizAnswer().equals(quiz.getAnswer())) {

			// result画面用の正誤配列にtrueを入れる
			ox.set(quizId,"true");

			// 正解すると以下の点数を加算
			int score = 70;
			userScore += score;

			/**
			 * タイムスコア機能の実装・追加
			 * @author HarukiUeo
			 */
			if(!user.getId().equals(PageController.GUEST_ID)){
				Instant endTime=Instant.now();
				Instant startTime=userTimeService.getStartTimeByUserId(userId);
				Long timeScore=300-userTimeService.getElapsedTimeInSeconds(startTime, endTime);
				if(timeScore>=1) {
					userScore +=Math.toIntExact(timeScore)/10; 
				}
			}

			Integer quizNum = quizId;
			quizNum++;
			
			// 最終問題の場合
			if(quizNum > quizService.getCountQuestion()) {
				model.addAttribute(user);
				ox.remove(0);
				model.addAttribute("ox",ox);
				model.addAttribute("userScore",userScore);
				return "quizResult"; 
			}
			
			QuizEntity nextQuiz = quizService.selectOneQuizById(quizNum);
			model.addAttribute("quiz", nextQuiz);
			model.addAttribute("ox", ox);
			model.addAttribute("userScore", userScore);
			quizForm.setQuizAnswer("");
			
			return "playScreen";
			
		} else { // 不正解の場合
//			redirectAttributes.addFlashAttribute("resultMessage", "不正解です・・・");
			model.addAttribute("quiz", quiz);
			model.addAttribute("ox", ox);
			model.addAttribute("userScore", userScore);
			quizForm.setQuizAnswer("");
			return "playScreen";
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
	public String showHint(@RequestParam("userId") String uId,
			@RequestParam("quizId") String qId, 
			@RequestParam("ox") List<String> ox,
			@RequestParam("userScore") String uScore,
			Model model){
		Integer quizId = Integer.parseInt(qId);
		Integer userId = Integer.parseInt(uId);
		int userScore = Integer.parseInt(uScore);
		QuizEntity quiz = quizService.selectOneQuizById(quizId);
		UserEntity user = userService.selectOneUserById(userId);
		QuizEntity hint = quizService.selectOneQuizById(quizId);
		model.addAttribute("hint",hint.getHint());
		model.addAttribute("quiz", quiz);
		model.addAttribute("user", user);
		model.addAttribute("ox", ox);
		model.addAttribute("userScore", userScore);
		return "playScreen";
	}

	//TOPページへ遷移させる
	@PostMapping("/returnTopPage")
	public String returnTopPage(@RequestParam("userId") String uId, Model model) {
		Integer userId = Integer.parseInt(uId);
		UserEntity user = userService.selectOneUserById(userId);
		model.addAttribute("user", user);
		return "top";//小林さん担当ページ
	}

}