package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.registrationlogindemo.entity.User;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    Article findByTitle(String title);
    Article findById(long id);
    List<Article> findBySupplier(User user);
    Boolean existsByTitleAndSupplier(String title, User supplier);

}
