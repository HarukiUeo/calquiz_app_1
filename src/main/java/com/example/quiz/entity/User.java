package com.example.quiz.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ユーザー情報を管理するEntityクラスです。
* @param id データベース上のユーザーの主キー
* @param name ユーザーの名前
* @param score ユーザーの点数
* @param rank ユーザーのランキング 
* @author Haruki Ueo
* @author Yuma Matui
* @version 1.0.0
*/
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="userinfo")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Column(name = "score", nullable = false)
    private Integer score;
    
    @Column(name = "rank", nullable = false)
    private Integer rank;
}
