package com.example.registrationlogindemo.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="supplier_id")
    private User supplier;

    @ManyToOne
    @JoinColumn(name="store_manager_id")
    private User storeManager;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @Column(name="created_at")
    private LocalDate createdAt;

    @Column
    private Status status;

    @Column(nullable = true)
    private Double total;

    @Column(nullable = true)
    @Max(1)
    @Min(0)
    private Double taxes;

    // public Order( User supplier, User storeManager, List<OrderItem> items, LocalDate createdAt) {
    //     this.total = items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    //     this.supplier = supplier;
    //     this.storeManager = storeManager;
    //     this.items = items;
    //     this.createdAt = createdAt;
    //     this.status = Status.PENDING;
    //     this.taxes = 0.0;
    // }

    public enum Status {
        PENDING, //0 store manager has made the order
        ACCEPTED, //1 supplier has accepted the order
        REJECTED, //2 supplier has rejected the order
        CANCELLED, //3 store manager has cancelled the order
        CONFIRMED  //4 store manager has confirmed the order
    }

}

