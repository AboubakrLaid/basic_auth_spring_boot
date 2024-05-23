package com.example.registrationlogindemo.entity;



import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private Double price;

    @Column(nullable=false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="supplier_id")
    private User supplier;

    @Column(name="expiration_date")
    private LocalDate expirationDate;

    @Column(name="created_at")
    private LocalDate createdAt;

}
