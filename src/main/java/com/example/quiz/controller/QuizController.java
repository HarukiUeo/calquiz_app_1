package com.example.quiz.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		QuizEntity quiz = quizService.selectOneQuizById(quizNum);
		UserEntity user = userService.selectOneUserById(userId);
		model.addAttribute("quiz",quiz);
		model.addAttribute("user",user);
		model.addAttribute("ox",ox);
		model.addAttribute("userScore",userScore);

		/**
		 * ゲスト以外のプレイヤーがゲームをプレイするとスタートした時間が保存される
		 * @author HarukiUeo
		 */
		if(!user.getId().equals(1)) {

			userTimeService.saveEntityWithStartTime(userId);

		}


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
		QuizEntity quiz = quizService.selectOneQuizById(quizId);
		UserEntity user = userService.selectOneUserById(userId);


		if (quizForm.getQuizAnswer().equals(quiz.getAnswer())) {
			//		    redirectAttributes.addFlashAttribute("resultMessage","正解です！");

			// result画面用の正誤配列にtrueを入れる
			ox.set(quizId,"true");

			//			ox[quizId-1] = "true";

			// 正解すると以下の点数を加算
			int score = 70;
			userScore += score;

			/**
			 * タイムスコア機能の実装・追加
			 * @author HarukiUeo
			 */
			if(!user.getId().equals(1)){
				Instant endTime=Instant.now();
				Instant startTime=userTimeService.getStartTimeByUserId(userId);
				Long timeScore=300-userTimeService.getElapsedTimeInSeconds(startTime, endTime);
				if(timeScore>=1) {
					System.out.println(userScore);
					System.out.println(timeScore);
					userScore +=Math.toIntExact(timeScore); 
					System.out.println(userScore);
				}
			}

			Integer quizNum = quizId;
			quizNum++;

			if(quizNum > quizService.getCountQuestion()) {
				//		    	User user = userService.selectOneUserById(userId); submitAnswerのインスタンスに変えました
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
		QuizEntity hint = quizService.selectOneQuizById(quizForm.getQuizId());
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

}