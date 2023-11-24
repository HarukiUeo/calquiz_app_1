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
        createAndSaveQuizIfNotExists("image1", "かめら","hint1");
        createAndSaveQuizIfNotExists("image2", "ROBOT","hint2");
        createAndSaveQuizIfNotExists("image3", "8","hint3");
        createAndSaveQuizIfNotExists("image4", "FISH","hint4");
        createAndSaveQuizIfNotExists("image5", "タイム","hint5");
        createAndSaveQuizIfNotExists("image6", "地球","hint6");
        createAndSaveQuizIfNotExists("image7", "ピーチ","hint7");
        createAndSaveQuizIfNotExists("image8", "PERFECT","hint8");
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