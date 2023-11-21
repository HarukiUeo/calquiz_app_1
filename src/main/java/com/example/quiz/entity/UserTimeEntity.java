package com.example.quiz.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザーのクリアタイムをデータベースに残す際のクラスを正規化して作成しました
 * @author HarukiUeo
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usertime")
public class UserTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="userinfo_id")
	private User user;

	@Column(name="startTime")
	private Instant startTime;

	@Column(name="endTime")
	private Instant endTime;
}
