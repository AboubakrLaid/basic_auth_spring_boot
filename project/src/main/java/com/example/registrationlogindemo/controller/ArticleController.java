package com.example.registrationlogindemo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.registrationlogindemo.dto.LoginDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.ArticleService;
import com.example.registrationlogindemo.service.UserService;
import com.example.registrationlogindemo.dto.ArticleDto;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin("*")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;

    private ResponseEntity<?> isSupplier(User user, LoginDto loginRequest) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "You are not authorized to view this page");
        if (user != null) {
            boolean valid = userService.checkIfValidOldPassword(user, loginRequest.getPassword());
            if (valid && !user.getRole().getName().equals("SUPPLIER")) {
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

        }
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addArticle(@RequestBody ArticleDto article) {

        System.out.println("..................." + article.getSupplier());
        System.out.println("..................." + article.getSupplier().getUsername());
        try {
            Map<String, Object> result = articleService.saveArticle(article);
            if (result.get("success").equals(false)) {
                if(result.get("message").equals("Article already exists")){
                    return new ResponseEntity<>(result, HttpStatus.CONFLICT);
                }
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
           
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "An error occurred while saving the article " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "An error occurred while deleting the article " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/article/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Long id, @RequestBody ArticleDto updatedArticle) {
        try {
            String response = articleService.updateArticle(id, updatedArticle);
            Map<String, Object> responses = new HashMap<>();
            responses.put("success", true ? response.equals("Article updated successfully") : false);
            responses.put("message", response);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "An error occurred while updating the article " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(@RequestBody Map<String, Object> body) {
        try {
            Long id = Long.parseLong(body.get("id").toString());
            return new ResponseEntity<>(articleService.getAllArticles(id), HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
