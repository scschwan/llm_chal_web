package kr.co.dimillion.lcapp.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defect_type_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DefectType defectType;

    @Column(name = "image_type")
    private String type; // defect, normal

    @Column(name = "file_name")
    private String name;
    @Column(name = "storage_url")
    private String path;
    @Column(name = "file_size")
    private Long size;

    @Column(name = "use_yn")
    private boolean used;
    @Column(name = "sync_yn")
    private boolean synced;
    private LocalDateTime uploadedAt;

    public Image(Product product, String type, String name, String path, Long size) {
        this.product = product;
        this.type = type;
        this.name = name;
        this.path = path;
        this.size = size;
        this.used = true;
        this.synced = false;
        this.uploadedAt = LocalDateTime.now();
    }

    public Image(Product product, DefectType defectType, String type, String name, String path, Long size) {
        this.product = product;
        this.defectType = defectType;
        this.type = type;
        this.name = name;
        this.path = path;
        this.size = size;
        this.used = true;
        this.synced = false;
        this.uploadedAt = LocalDateTime.now();
    }

    public void delete() {
        used = false;
    }
}
