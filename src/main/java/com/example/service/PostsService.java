package com.example.service;

import com.example.dao.PostsDAO;
import com.example.model.Posts;

import java.sql.SQLException;
import java.util.List;

public class PostsService {

    private final PostsDAO postsDAO = new PostsDAO();

    public List<Posts> getAllPosts() throws SQLException {
        return postsDAO.getAllPosts();
    }

    public List<Posts> getPostsByUserId(Long userId) throws SQLException {
        return postsDAO.getPostsByUserId(userId);
    }

    public Posts getPostById(Long id) throws SQLException {
        return postsDAO.getPostById(id).orElse(null);
    }

    public Posts createPost(Long userId, String title, String content) throws SQLException {
        Posts post = new Posts();
        post.setUserId(userId);
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedAt(java.time.LocalDateTime.now());

        // Return the created post (which includes the generated ID)
        return postsDAO.createPost(post);
    }

    public boolean updatePost(Long id, String title, String content) throws SQLException {
        return postsDAO.updatePost(id, title, content);
    }

    public boolean deletePost(Long id) throws SQLException {
        return postsDAO.deletePost(id);
    }
}
