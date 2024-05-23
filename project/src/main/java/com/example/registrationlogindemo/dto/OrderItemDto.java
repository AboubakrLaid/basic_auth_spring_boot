package com.example.registrationlogindemo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
   
   
    private Long id;
    private Integer quantity;
    // private Double price;
    private ArticleDto article;

    
}
