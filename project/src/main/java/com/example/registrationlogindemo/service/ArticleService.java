package com.example.registrationlogindemo.service;

import java.util.List;
import java.util.Map;

import com.example.registrationlogindemo.dto.ArticleDto;
// import com.example.registrationlogindemo.entity.Article;
import com.example.registrationlogindemo.entity.User;

public interface ArticleService {
    Map<String, Object> saveArticle(ArticleDto article);
    void deleteArticle(Long id);
    String updateArticle(Long id, ArticleDto article);

    List<ArticleDto> getAllArticles(Long id);

    // List<ArticleDto> getAllArticles();
}
