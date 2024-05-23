package com.example.registrationlogindemo.service.impl;

import com.example.registrationlogindemo.entity.Article;
import com.example.registrationlogindemo.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.registrationlogindemo.repository.ArticleRepository;
// import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.dto.*;
import java.util.List;
import java.util.Map;

// import java.util.function.Supplier;
// import java.util.stream.Collectors;
import com.example.registrationlogindemo.entity.User;
import java.util.ArrayList;
import com.example.registrationlogindemo.service.UserService;

@Service
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;
    @Autowired
    private UserService userService;
    

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticleDto> getAllArticles(Long id) {
        // return all the articles if the user is a store manager
        User user = userService.findById(id);
        if (user == null) {
            return null;
        }

        if (user.getRole().getName().equals(User.ROLE.STORE_MANAGER.toString())) {
            List<Article> articles = articleRepository.findAll();
            List<ArticleDto> articleDtos = new ArrayList<>();
            for (Article article : articles) {
                articleDtos.add(convertToDto(article));
            }
            return articleDtos;
        }
        List<Article> articles = articleRepository.findBySupplier(user);
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (Article article : articles) {
            articleDtos.add(convertToDto(article));
        }
        return articleDtos;
    }

    

    @Override
    public Map<String, Object> saveArticle(ArticleDto article) {
        Boolean exists = articleRepository.existsByTitleAndSupplier(article.getTitle(),
                userService.findByUsername(article.getSupplier().getUsername()));
        if (exists) {
            return Map.of("success", false, "message", "Article already exists");
        }
        Article newArticle = new Article();
        newArticle.setTitle(article.getTitle());
        newArticle.setDescription(article.getDescription());
        newArticle.setExpirationDate(article.getExpirationDate());
        newArticle.setCreatedAt(article.getCreatedAt());
        newArticle.setPrice(article.getPrice());
        newArticle.setQuantity(article.getQuantity());

        User supplier = userService.findByUsername(article.getSupplier().getUsername());
        if (supplier == null) {
            return Map.of("success", false, "message", "Supplier not found");
        }
        newArticle.setSupplier(supplier);

        articleRepository.save(newArticle);
        return Map.of("success", true, "message", "Article created successfully");
    }

    @Override
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id).get();
        articleRepository.delete(article);
    }

    @Override
    public String updateArticle(Long id, ArticleDto updatedArticle) {
        try {
            // 1. Retrieve the article to be updated
            Article articleToUpdate = articleRepository.findById(id).get();
            // 2. Update the article properties
            articleToUpdate.setTitle(updatedArticle.getTitle());
            articleToUpdate.setDescription(updatedArticle.getDescription());
            articleToUpdate.setPrice(updatedArticle.getPrice());
            articleToUpdate.setQuantity(updatedArticle.getQuantity());
            articleToUpdate.setExpirationDate(updatedArticle.getExpirationDate());



            // 3. Save the updated article
            articleRepository.save(articleToUpdate);
            return "Article updated successfully";
        } catch (Exception e) {
            return "Article not found" + id;
        }

    }

    public ArticleDto convertToDto(Article article) {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setDescription(article.getDescription());
        articleDto.setPrice(article.getPrice());
        articleDto.setQuantity(article.getQuantity());
        articleDto.setExpirationDate(article.getExpirationDate());
        articleDto.setCreatedAt(article.getCreatedAt());

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(article.getSupplier().getId());
        supplierDto.setUsername(article.getSupplier().getUsername());

        articleDto.setSupplier(supplierDto);
        return articleDto;

    }

}
