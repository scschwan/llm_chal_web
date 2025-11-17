package kr.co.dimillion.lcapp.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ModelSettings extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_settings_id")
    private Integer id;
    @Enumerated(EnumType.STRING)
    private DefectInspection defectInspection;
    @Enumerated(EnumType.STRING)
    private SimilarityMatch similarityMatch;
    @Enumerated(EnumType.STRING)
    private GenAi genAi;

    public ModelSettings(DefectInspection defectInspection, SimilarityMatch similarityMatch, GenAi genAi) {
        this.defectInspection = defectInspection;
        this.similarityMatch = similarityMatch;
        this.genAi = genAi;
    }

    @Getter
    public enum DefectInspection {
        ANOMALY("anomaly", true),
        YOLO("YOLO(준비중)", false),
        ROLE_BASED("Rule-based(준비중)", false);

        private final String name;
        private final boolean ready;

        DefectInspection(String name, boolean ready) {
            this.name = name;
            this.ready = ready;
        }
    }

    @Getter
    public enum SimilarityMatch {
        CLIP("CLIP", true), BLIP("BLIP(준비중)", false), VIT("VIT(준비중)", false);

        private final String name;
        private final boolean ready;

        SimilarityMatch(String name, boolean ready) {
            this.name = name;
            this.ready = ready;
        }
    }

    @Getter
    public enum GenAi {
        HYPER_CLOVA("hyperClova", true),
        EXAONE("exaone", true),
        LLAVA("Llava", true),
        GPT("GPT(준비중)", false),
        Gemini("Gemini(준비중)", false);

        private final String name;
        private final boolean ready;

        GenAi(String name, boolean ready) {
            this.name = name;
            this.ready = ready;
        }
    }

}
