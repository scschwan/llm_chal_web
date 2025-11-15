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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    @Column(name = "file_name")
    private String name;
    @Column(name = "file_path")
    private String path;
    @Column(name = "file_size")
    private Long size;
    @Column(name = "vector_indexed")
    private boolean indexed;
    @Column(name = "use_yn")
    private boolean used;
    private LocalDateTime createdAt;

    public Manual(Product product, String name, String path, long size) {
        this.product = product;
        this.name = name;
        this.path = path;
        this.size = size;
        this.indexed = false;
        this.used = true;
        this.createdAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.used = false;
    }
}
