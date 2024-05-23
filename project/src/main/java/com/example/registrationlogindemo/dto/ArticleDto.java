package com.example.registrationlogindemo.dto;


import java.time.LocalDate;

import jakarta.persistence.Convert;

// import com.fasterxml.jackson.annotation.JsonIgnore;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.registrationlogindemo.entity.Article;
// import com.example.registrationlogindemo.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Integer quantity;
    private SupplierDto supplier;
    private LocalDate expirationDate;
    private LocalDate createdAt;

    // public Article ConvertToArticle(ArticleDto articleDto){
    //     Article article = new Article();
    //     article.setId(articleDto.getId());
    //     article.setTitle(articleDto.getTitle());
    //     article.setDescription(articleDto.getDescription());
    //     article.setPrice(articleDto.getPrice());
    //     article.setQuantity(articleDto.getQuantity());
    //     article.setSupplier(articleDto.getSupplier().ConvertToSupplier());
    //     article.setExpirationDate(articleDto.getExpirationDate());
    //     article.setCreatedAt(articleDto.getCreatedAt());
    //     return article;
        
    // }
}
