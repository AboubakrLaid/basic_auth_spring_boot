package com.example.registrationlogindemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// import com.example.registrationlogindemo.entity.Article;
import com.example.registrationlogindemo.entity.OrderItem;
import com.example.registrationlogindemo.repository.ArticleRepository;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderItemDto {
   
   
    private Long id;
    private Integer quantity;
    // private Double price;
    private Long articleId;

    public OrderItem ConvertToOrderItem(ArticleRepository articleRepository){
        OrderItem orderItem = new OrderItem();
        System.out.println("Article ID: " + this.articleId);
        System.out.println("Article: " + articleRepository.findById(this.articleId).get());
        orderItem.setId(this.id);
        orderItem.setQuantity(this.quantity);
        orderItem.setArticle(articleRepository.findById(this.articleId).get());
        return orderItem;
    }
}
