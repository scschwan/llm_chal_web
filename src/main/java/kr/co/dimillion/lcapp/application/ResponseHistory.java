package kr.co.dimillion.lcapp.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ResponseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_Id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SearchHistory searchHistory;

    private String productCode;
    private String defectCode;
    private Double similarityScore;
    private Double anomalyScore;
}
