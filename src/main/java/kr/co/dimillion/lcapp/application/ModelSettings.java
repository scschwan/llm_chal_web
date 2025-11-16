package kr.co.dimillion.lcapp.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ModelSettings {
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


    @Getter
    public enum DefectInspection {
        ANOMALY("anomaly", true),
        YOLO("YOLO", false),
        ROLE_BASED("Rule-based", false);

        private final String name;
        private final boolean ready;

        DefectInspection(String name, boolean ready) {
            this.name = name;
            this.ready = ready;
        }
    }

    @Getter
    public enum SimilarityMatch {
        CLIP("CLIP", true), BLIP("BLIP", false), VIT("VIT", false);

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
        GPT("GPT", false),
        Gemini("Gemini", false);

        private final String name;
        private final boolean ready;

        GenAi(String name, boolean ready) {
            this.name = name;
            this.ready = ready;
        }
    }

}
