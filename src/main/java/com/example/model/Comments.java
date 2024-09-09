package com.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comments {
    private Long id;
    private Long postId;
    private Long parentId; // 대댓글일 경우 상위 댓글 ID
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}

