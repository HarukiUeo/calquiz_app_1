package com.example.quiz.controller;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.quiz.component.ControllerParameter;
import com.example.quiz.entity.QuizEntity;
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
	
	@Autowired
	ControllerParameter ctrParam;
	
	/** クイズの１問目 */
	public static final Integer firstQuiz = 1;
	
	/** 1問あたりの基礎配点 */
	public static final int BASE_SCORE = 70;
	
	private int quizId;
	
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
		if(!userId.equals(ControllerParameter.GUEST_ID)) {
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
//			@RequestParam("userId") String uId,
//			@RequestParam("quizId") String qId,
//			@RequestParam("ox") List<String> ox,
//			@RequestParam("userScore") String uScore,
			Model model) {
		
		//スコア管理(正誤用配列・スコア)は最後にまとめてデータベースに保存するためhtml間でやり取りをする
//		List<String> ox = 
		ctrParam.setOx(setUpOxList());
		
//		String userScore = "0";
		ctrParam.setUserScore(0);
		
//		Integer quizId = Integer.parseInt(qId);
//		Integer userId = Integer.parseInt(uId);
//		int userScore = Integer.parseInt(uScore);
		QuizEntity quiz = quizService.selectOneQuizById(firstQuiz);
		this.quizId = firstQuiz;
//		UserEntity user = userService.selectOneUserById(userId);
		model.addAttribute("quiz",quiz);
		model.addAttribute("user",ctrParam.getPlayer());
//		model.addAttribute("ox",ox);
		model.addAttribute("ox", ctrParam.getOx());
		model.addAttribute("userScore",ctrParam.getUserScore());
		
		// 1問目のタイマースタート
		setTimeExceptGuest(ctrParam.getPlayer().getId());
		
		return "playScreen";
	}

	//解答チェック機能
	@PostMapping("/submitAnswer")
	public String submitAnswer(
//			@RequestParam("userId") String uId,
//			@RequestParam("quizId") String qId,
//			@RequestParam("ox") List<String> ox,
//			@RequestParam("userScore") String uScore,
//			@RequestParam("hintCount") String hintCount,
			QuizForm quizForm,
			Model model) {
//		Integer quizId = Integer.parseInt(qId);
//		Integer userId = Integer.parseInt(uId);
//		int userScore = Integer.parseInt(uScore);
		QuizEntity quiz = quizService.selectOneQuizById(this.quizId);
//		UserEntity user = userService.selectOneUserById(userId);
		
		// 正解・不正解関係なくユーザーをモデルに格納
		model.addAttribute("user", ctrParam.getPlayer());


		if (quizForm.getQuizAnswer().equals(quiz.getAnswer())) {

			// result画面用の正誤配列にtrueを入れる
			ctrParam.getOx().set(this.quizId,"true");
//			List<String> ox = ctrParam.getOx();
//			ox.set(this.quizId,"true");
//			ctrParam.setOx(ox);
			
//			int hintC = Integer.parseInt(hintCount);
			
			// 正解すると以下の点数を加算
			ctrParam.setUserScore(ctrParam.getUserScore()+BASE_SCORE);

			/**
			 * タイムスコア機能の実装・追加
			 * @author HarukiUeo
			 */
			if(!ctrParam.getPlayer().getId().equals(ControllerParameter.GUEST_ID)){
				Instant endTime=Instant.now();
				Instant startTime=userTimeService.getStartTimeByUserId(ctrParam.getPlayer().getId());
				Long timeScore=300-userTimeService.getElapsedTimeInSeconds(startTime, endTime);
				if(timeScore>=1) {
					ctrParam.setUserScore(ctrParam.getUserScore()+Math.toIntExact(timeScore)/10);
				}
			}

			this.quizId += 1;
			
			// 最終問題の場合
			if(this.quizId > quizService.getCountQuestion()) {
				ctrParam.getOx().remove(0);
				model.addAttribute("ox",ctrParam.getOx());
				model.addAttribute("userScore",ctrParam.getUserScore());
				return "quizResult"; 
			}
			
			
			QuizEntity nextQuiz = quizService.selectOneQuizById(this.quizId);
			model.addAttribute("quiz", nextQuiz);
			model.addAttribute("ox", ctrParam.getOx());
			model.addAttribute("userScore", ctrParam.getUserScore());
			quizForm.setQuizAnswer("");
			setTimeExceptGuest(ctrParam.getPlayer().getId());
			
			return "playScreen";
			
		} else { // 不正解の場合
			model.addAttribute("resultMessage", "不正解です・・・");
			model.addAttribute("quiz", quiz);
			model.addAttribute("ox", ctrParam.getOx());
			model.addAttribute("userScore", ctrParam.getUserScore());
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
	public String showHint(
//			@RequestParam("userId") String uId,
//			@RequestParam("quizId") String qId, 
//			@RequestParam("ox") List<String> ox,
//			@RequestParam("userScore") String uScore,
			Model model){
//		Integer quizId = Integer.parseInt(qId);
//		Integer userId = Integer.parseInt(uId);
//		int userScore = Integer.parseInt(uScore);
		QuizEntity quiz = quizService.selectOneQuizById(this.quizId);
//		UserEntity user = userService.selectOneUserById(userId);
		QuizEntity hint = quizService.selectOneQuizById(this.quizId);
		model.addAttribute("hint",hint.getHint());
		model.addAttribute("quiz", quiz);
		model.addAttribute("user", ctrParam.getPlayer());
		model.addAttribute("ox", ctrParam.getOx());
		model.addAttribute("userScore", ctrParam.getUserScore());
		return "playScreen";
	}

//	//TOPページへ遷移させる
//	@PostMapping("/returnTopPage")
//	public String returnTopPage(
//			@RequestParam("userId") String uId,
//			Model model) {
//		Integer userId = Integer.parseInt(uId);
//		UserEntity user = userService.selectOneUserById(userId);
//		model.addAttribute("user", user);
//		return "top";//小林さん担当ページ
//	}
	
	/**
	 * パス機能の実装
	 * @Author Yuma Matsui
	 */
	@PostMapping("pass")
	public String passTheQuestion(
//			@RequestParam("userId") String uId,
//			@RequestParam("quizId") String qId,
//			@RequestParam("ox") List<String> ox,
//			@RequestParam("userScore") String uScore,
			QuizForm quizForm,
			Model model) {
//		Integer quizId = Integer.parseInt(qId);
//		Integer userId = Integer.parseInt(uId);
//		int userScore = Integer.parseInt(uScore);
//		UserEntity user = userService.selectOneUserById(userId);
		
		ctrParam.getOx().set(this.quizId,"false");
		model.addAttribute("user", ctrParam.getPlayer());
		
		this.quizId += 1;
		
		// 最終問題の場合
		if(this.quizId > quizService.getCountQuestion()) {
			ctrParam.getOx().remove(0);
			model.addAttribute("ox",ctrParam.getOx());
			model.addAttribute("userScore",ctrParam.getUserScore());
			return "quizResult"; 
		}
		
		QuizEntity nextQuiz = quizService.selectOneQuizById(this.quizId);
		model.addAttribute("quiz", nextQuiz);
		model.addAttribute("ox", ctrParam.getOx());
		model.addAttribute("userScore", ctrParam.getUserScore());
		quizForm.setQuizAnswer("");
		
		return "playScreen";
	}

}