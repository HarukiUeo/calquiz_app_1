package com.example.quiz.component;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.quiz.entity.UserEntity;

import lombok.Data;

@Data
@Component
public class ControllerParameter {
	
	public static final int GUEST_ID=1;
	
	private UserEntity player;
	
	private List<String> isTrue ;
	
	private int userScore ;
	
}
