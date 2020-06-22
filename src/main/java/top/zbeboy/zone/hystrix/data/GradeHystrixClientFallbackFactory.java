package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Grade;
import top.zbeboy.zone.feign.data.GradeService;
import top.zbeboy.zbase.vo.data.grade.GradeSearchVo;

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
    public List<Grade> findByScienceIdAndGradeIsDel(GradeSearchVo gradeSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public Grade save(Grade grade) {
        return new Grade();
    }
}
