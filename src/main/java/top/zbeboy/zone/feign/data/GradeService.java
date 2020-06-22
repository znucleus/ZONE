package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.domain.tables.pojos.Grade;
import top.zbeboy.zone.hystrix.data.GradeHystrixClientFallbackFactory;
import top.zbeboy.zbase.vo.data.grade.GradeSearchVo;

import java.util.List;

@FeignClient(value = "base-server", fallback = GradeHystrixClientFallbackFactory.class)
public interface GradeService {

    /**
     * 获取年级
     *
     * @param id 年级主键
     * @return 年级数据
     */
    @GetMapping("/base/data/grade/{id}")
    Grade findById(@PathVariable("id") int id);

    /**
     * 根据专业id与年级查询
     *
     * @param scienceId 专业id
     * @param grade     年级
     * @return 数据
     */
    @PostMapping("/base/data/grade/science_id_and_grade")
    Grade findByScienceIdAndGrade(@RequestParam("scienceId") int scienceId, @RequestParam("grade") int grade);

    /**
     * 获取专业下全部有效年级
     *
     * @param gradeSearchVo 查询参数
     * @return 年级数据
     */
    @PostMapping("/base/data/grades/search")
    List<Grade> findByScienceIdAndGradeIsDel(@RequestBody GradeSearchVo gradeSearchVo);

    /**
     * 保存
     *
     * @param grade 数据
     */
    @PostMapping("/base/data/grade/save")
    Grade save(@RequestBody Grade grade);
}
