package kr.co.dimillion.lcapp.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "products")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;
    @Column(name = "product_code", length = 50)
    private String code;
    @Column(name = "product_name", length = 100)
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "is_active")
    private boolean active;
}
