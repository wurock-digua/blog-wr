package cn.wurock.blog.auth.dto;

import lombok.Data;

@Data
public class LogoutRequest {
	private String refreshToken; // 可选：用于同时吊销 refresh token
}
