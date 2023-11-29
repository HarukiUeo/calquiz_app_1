package com.example.quiz.component;

import org.springframework.stereotype.Component;

import com.example.quiz.entity.QuizEntity;
import com.example.quiz.repository.QuizRepository;

import jakarta.annotation.PostConstruct;

/**
 * データベーステーブル初期値設定クラスです。
 * @author Haruki Ueo
 * @version 1.0.0
 */
//自動でテーブルだけが作成されていたので、初期値を設定できるようにしておきました。
@Component
public class QuizDataInitialization {

    private final QuizRepository quizRepository;

    public QuizDataInitialization(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @PostConstruct
    public void init() {
    	 createAndSaveQuizIfNotExists("image1", "かめら","表の形をよく見ると");
         createAndSaveQuizIfNotExists("image2", "ROBOT","色に注目");
         createAndSaveQuizIfNotExists("image3", "8","視点を変えてみて");
         createAndSaveQuizIfNotExists("image4", "FISH","問題文の注意書きがヒント");
         createAndSaveQuizIfNotExists("image5", "タイム","符号に注目");
         createAndSaveQuizIfNotExists("image6", "地球","消防を呼ぶには？");
         createAndSaveQuizIfNotExists("image7", "ピーチ","矢印の向きに注目");
         createAndSaveQuizIfNotExists("image8", "4","年に一度交代します");
         createAndSaveQuizIfNotExists("image9", "えら","共通して足りないものは？");
         createAndSaveQuizIfNotExists("image10", "たんてい","もんだいぶんにちゅうもく");
    }
    /**
     * 引数で受け取ったquizNameとanswerをデータベースに登録するメソッドです。
     * @param name QuizEntityのquizNameに設定する値を受け取ります。
     * @param answer QuizEntityのanswerに設定する値を受け取ります。
     */
    private void createAndSaveQuizIfNotExists(String quizName, String answer,String hint) {
        // 既存のデータを探す
        QuizEntity existingName = quizRepository.findByQuizName(quizName);

        if (existingName == null) {
            // 既存のデータがない場合に新しいデータを作成して保存
            QuizEntity quiz = new QuizEntity();
            quiz.setQuizName(quizName);
            quiz.setAnswer(answer);
            quiz.setHint(hint);

            quizRepository.save(quiz);
        }
    }
}