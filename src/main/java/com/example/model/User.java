package com.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
	private Long id;
	
	private String email;
	
	private String password;

	private String phoneNumber;

	private String name;
	
	private boolean isDeleted;
	
	private LocalDateTime createdAt;


	

}
