package com.batch22bd.BackEnd.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "foods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MANY FOODS -> ONE CATEGORY
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 150)
    private String name;

    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
}
