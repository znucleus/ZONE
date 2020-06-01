package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.Grade;
import top.zbeboy.zone.feign.data.GradeService;
import top.zbeboy.zone.web.vo.data.grade.GradeSearchVo;

import java.util.ArrayList;
import java.util.List;

@Component
public class GradeHystrixClientFallbackFactory implements GradeService {
    @Override
    public Grade findById(int id) {
        return new Grade();
    }

    @Override
    public Grade findByScienceIdAndGrade(int scienceId, int grade) {
        return new Grade();
    }

    @Override
    public List<Grade> anyoneData(GradeSearchVo gradeSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public Grade save(Grade grade) {
        return new Grade();
    }
}
