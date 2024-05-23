package com.example.registrationlogindemo.dto;


import java.time.LocalDate;

// import com.fasterxml.jackson.annotation.JsonIgnore;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderDto {
    private Long id;
    private SupplierDto supplier;
    private StoreManagerDto storeManager;
    private LocalDate createdAt;
    private String status;
    private Double total;
    private List<NewOrderItemDto> items;
}
