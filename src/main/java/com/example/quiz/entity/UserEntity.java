package com.example.quiz.entity;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
 * @param loggedin ユーザーのログイン状態　※loggedInに絶対にしないでください。
			JpaリポジトリーのUser findByLoggedin(Boolean loggedIn)でエラーが起きます。findByLoggedInでも同様です。
 * @author Haruki Ueo
 * @author Yuma Matui
 * @version 1.0.0
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="userinfo")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;


	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "score", nullable = false)
	private Integer score;

	@Column(name = "rank", nullable = false)
	private Integer rank;

	/**
	 * @param loggedin userのログイン状態 ※loggedInに絶対にしないでください！！
      			JpaリポジトリのUser findByLoggedin(Boolean loggedIn)でエラーが起きます。findByLoggedInでも同様です。
	 * @param password userのパスワード 					
	 * @author 浜田真由美
	 */
	@Column(name = "loggedIn")
	private Boolean loggedin;

	@Column(name = "password")
	private String password;

	/**
	 * userTimeのリストを追加
	 */
	@OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
	private List<UserTimeEntity> times;
}