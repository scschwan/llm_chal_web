package kr.co.dimillion.lcapp.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String modelType;
    private String guideContent;
    private LocalDateTime guideGeneratedAt;
    private Double processingTime;
    private Integer feedbackRating;
    private String feedbackText;
    private String feedbackUser;
    private LocalDateTime feedbackAt;

    private LocalDateTime executedAt;
}
