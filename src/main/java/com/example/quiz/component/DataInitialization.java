package com.example.quiz.component;

import org.springframework.stereotype.Component;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;

import jakarta.annotation.PostConstruct;

/**
 * データベーステーブル初期値設定クラスです。
 * @author Haruki Ueo
 * @version 1.0.0
 */
//自動でテーブルだけが作成されていたので、初期値を設定できるようにしておきました。
@Component
public class DataInitialization {

    private final QuizRepository quizRepository;

    public DataInitialization(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @PostConstruct
    public void init() {
        createAndSaveQuizIfNotExists("image1", "かめら");
        createAndSaveQuizIfNotExists("image2", "ROBOT");
        createAndSaveQuizIfNotExists("image3", "8");
        createAndSaveQuizIfNotExists("image4", "FISH");
        createAndSaveQuizIfNotExists("image5", "タイム");
        createAndSaveQuizIfNotExists("image6", "地球");
        createAndSaveQuizIfNotExists("image7", "ピーチ");
        createAndSaveQuizIfNotExists("image8", "PERFECT");
    }
    /**
     * 引数で受け取ったquizNameとanswerをデータベースに登録するメソッドです。
     * @param name QuizEntityのquizNameに設定する値を受け取ります。
     * @param answer QuizEntityのanswerに設定する値を受け取ります。
     */
    private void createAndSaveQuizIfNotExists(String quizName, String answer) {
        // 既存のデータを探す
        Quiz existingName = quizRepository.findByQuizName(quizName);

        if (existingName == null) {
            // 既存のデータがない場合に新しいデータを作成して保存
            Quiz quiz = new Quiz();
            quiz.setQuizName(quizName);
            quiz.setAnswer(answer);

            quizRepository.save(quiz);
        }
    }
}