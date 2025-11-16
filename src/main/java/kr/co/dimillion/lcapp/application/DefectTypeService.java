package kr.co.dimillion.lcapp.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefectTypeService {
    private final DefectTypeRepository defectTypeRepository;

    @Transactional
    public void update(Integer id, String nameKo) {
        DefectType defectType = defectTypeRepository.findById(id)
                .orElseThrow();
        defectType.updateNameKo(nameKo);
    }

    @Transactional
    public void delete(Integer id) {
        defectTypeRepository.findById(id)
                .ifPresent(defectTypeRepository::delete);
    }
}
