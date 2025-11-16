package kr.co.dimillion.lcapp.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "defect_types")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DefectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "defect_type_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    @Column(name = "defect_code", length = 50)
    private String code;

    @Column(name = "defect_name_ko", length = 100)
    private String nameKo;

    @Column(name = "defect_name_en", length = 100)
    private String nameEn;

    @Column(name = "full_name_ko", length = 200)
    private String fullNameKo;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "use_yn")
    private boolean used;

    private LocalDateTime createdAt;

    public DefectType(Product product, String code, String nameKo, String nameEn, String fullNameKo, String description) {
        this.product = product;
        this.code = code;
        this.nameKo = nameKo;
        this.nameEn = nameEn;
        this.fullNameKo = fullNameKo;
        this.description = description;
        this.active = true;
        this.used = true;
        this.createdAt = LocalDateTime.now();
    }
}
