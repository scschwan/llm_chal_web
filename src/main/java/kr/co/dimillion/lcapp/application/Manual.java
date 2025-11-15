package kr.co.dimillion.lcapp.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "manuals")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Manual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manual_id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    @Column(name = "file_name")
    private String name;
    @Column(name = "file_path")
    private String path;
    @Column(name = "vector_indexed")
    private boolean indexed;
    private LocalDateTime createdAt;

    public Manual(Product product, String name, String path) {
        this.product = product;
        this.name = name;
        this.path = path;
        this.indexed = false;
        this.createdAt = LocalDateTime.now();
    }
}
