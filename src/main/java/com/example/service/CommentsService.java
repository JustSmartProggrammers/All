package com.example.service;

import com.example.dao.CommentsDAO;
import com.example.model.Comments;

import java.sql.SQLException;
import java.util.List;

public class CommentsService {

    private final CommentsDAO commentsDAO = new CommentsDAO();

    public List<Comments> getCommentsByPostId(Long postId) throws SQLException {
        return commentsDAO.getCommentsByPostId(postId);
    }

    public List<Comments> getRepliesByCommentId(Long commentId) throws SQLException {
        return commentsDAO.getRepliesByCommentId(commentId);
    }

    public Comments createComment(Long postId, Long parentId, Long userId, String content) throws SQLException {
        Comments comment = new Comments();
        comment.setPostId(postId);
        comment.setParentId(parentId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setCreatedAt(java.time.LocalDateTime.now());

        return commentsDAO.createComment(comment);
    }

    public boolean updateComment(Long commentId, String content) throws SQLException {
        return commentsDAO.updateComment(commentId, content);
    }

    public boolean deleteComment(Long commentId) throws SQLException {
        return commentsDAO.deleteComment(commentId);
    }
}
