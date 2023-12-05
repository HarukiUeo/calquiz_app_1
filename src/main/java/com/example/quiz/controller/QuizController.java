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
	public static final Integer FIRST_QUIZ = 1;

	/** 1問あたりの基礎配点 */
	public static final int BASE_SCORE = 100;

	/** 1問あたりの時間配点(500秒) */
	public static final int TIME_BASE_SCORE = 600;

	/** 減点スピード（1秒で1点マイナス） */
	public static final int DIVISION_TIME = 1;

	/** 現在の問題番号 */
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
		List<String> isTrue = new LinkedList<>();
		for(int i=0;i<quizService.getCountQuestion()+1;i++) {
			isTrue.add("false");
		}
		return isTrue;
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
			Model model) {

		//スコア管理(正誤用配列・スコア)は最後にまとめてデータベースに保存するためhtml間でやり取りをする
		ctrParam.setIsTrue(setUpOxList());
		ctrParam.setUserScore(0);

		QuizEntity quiz = quizService.selectOneQuizById(FIRST_QUIZ);
		this.quizId = FIRST_QUIZ;
		model.addAttribute("quiz",quiz);
		model.addAttribute("user",ctrParam.getPlayer());
		setTimeExceptGuest(ctrParam.getPlayer().getId());
		return "playScreen";
	}

	//解答チェック機能
	@PostMapping("/submitAnswer")
	public String submitAnswer(
			QuizForm quizForm,
			Model model) {
		QuizEntity quiz = quizService.selectOneQuizById(this.quizId);

		// 正解・不正解関係なくユーザーをモデルに格納
		model.addAttribute("user", ctrParam.getPlayer());

		if (quizForm.getQuizAnswer().equals(quiz.getAnswer())) {

			// result画面用の正誤配列にtrueを入れる
			ctrParam.getIsTrue().set(this.quizId,"true");

			// 正解すると以下の点数を加算
			ctrParam.setUserScore(ctrParam.getUserScore()+BASE_SCORE);

			/**
			 * タイムスコア機能の実装・追加
			 * @author HarukiUeo
			 */
			if(!ctrParam.getPlayer().getId().equals(ControllerParameter.GUEST_ID)){
				Instant endTime=Instant.now();
				Instant startTime=userTimeService.getStartTimeByUserId(ctrParam.getPlayer().getId());
				Long timeScore=TIME_BASE_SCORE - userTimeService.getElapsedTimeInSeconds(startTime, endTime);
				if(timeScore>=1) {
					ctrParam.setUserScore(ctrParam.getUserScore()+Math.toIntExact(timeScore)/DIVISION_TIME);
				}
			}

			this.quizId += 1;

			// 最終問題の場合
			if(this.quizId > quizService.getCountQuestion()) {
				ctrParam.getIsTrue().remove(0);
				model.addAttribute("isTrue",ctrParam.getIsTrue());
				model.addAttribute("userScore",ctrParam.getUserScore());
				return "quizResult"; 
			}

			QuizEntity nextQuiz = quizService.selectOneQuizById(this.quizId);
			model.addAttribute("quiz", nextQuiz);
			quizForm.setQuizAnswer("");
			setTimeExceptGuest(ctrParam.getPlayer().getId());
			return "playScreen";

		} else { // 不正解の場合
			model.addAttribute("resultMessage", "不正解です・・・");
			model.addAttribute("quiz", quiz);
			quizForm.setQuizAnswer("");
			return "playScreen";
		}
	}


	/**
	 * パス機能の実装
	 * @Author Yuma Matsui
	 */
	@PostMapping("pass")
	public String passTheQuestion(
			QuizForm quizForm,
			Model model) {
		ctrParam.getIsTrue().set(this.quizId,"false");
		model.addAttribute("user", ctrParam.getPlayer());
		this.quizId += 1;

		// 最終問題の場合
		if(this.quizId > quizService.getCountQuestion()) {
			ctrParam.getIsTrue().remove(0);
			model.addAttribute("isTrue",ctrParam.getIsTrue());
			model.addAttribute("userScore",ctrParam.getUserScore());
			return "quizResult"; 
		}

		QuizEntity nextQuiz = quizService.selectOneQuizById(this.quizId);
		model.addAttribute("quiz", nextQuiz);
		quizForm.setQuizAnswer("");
		return "playScreen";
	}

}