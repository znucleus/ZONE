package top.zbeboy.zone.web.register.leaver;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterScopeRecord;
import top.zbeboy.zone.service.data.*;
import top.zbeboy.zone.service.register.LeaverRegisterReleaseService;
import top.zbeboy.zone.service.register.LeaverRegisterScopeService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterReleaseBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterReleaseBean;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class RegisterLeaverRestController {

    @Resource
    private LeaverRegisterReleaseService leaverRegisterReleaseService;

    @Resource
    private LeaverRegisterScopeService leaverRegisterScopeService;

    @Resource
    private RegisterConditionCommon registerConditionCommon;

    @Resource
    private CollegeService collegeService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ScienceService scienceService;

    @Resource
    private GradeService gradeService;

    @Resource
    private OrganizeService organizeService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/register/leaver/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<LeaverRegisterReleaseBean> ajaxUtil = AjaxUtil.of();
        List<LeaverRegisterReleaseBean> beans = new ArrayList<>();
        Result<Record> records = leaverRegisterReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(LeaverRegisterReleaseBean.class);
            for (LeaverRegisterReleaseBean bean : beans) {
                bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime()));
                bean.setCanOperator(BooleanUtil.toByte(registerConditionCommon.epidemicOperator()));
                bean.setCanReview(BooleanUtil.toByte(registerConditionCommon.epidemicReview()));

                List<String> dataName = new ArrayList<>();
                Result<LeaverRegisterScopeRecord> registerScopeRecords = leaverRegisterScopeService.findByLeaverRegisterReleaseId(bean.getLeaverRegisterReleaseId());
                if (registerScopeRecords.isNotEmpty()) {
                    for (LeaverRegisterScopeRecord record : registerScopeRecords) {
                        switch (bean.getDataScope()) {
                            case 1:
                                // 院
                                College college = collegeService.findById(record.getDataId());
                                if(Objects.nonNull(college)){
                                    dataName.add(college.getCollegeName());
                                }
                                break;
                            case 2:
                                // 系
                                Department department = departmentService.findById(record.getDataId());
                                if(Objects.nonNull(department)){
                                    dataName.add(department.getDepartmentName());
                                }
                                break;
                            case 3:
                                // 专业
                                Science science = scienceService.findById(record.getDataId());
                                if(Objects.nonNull(science)){
                                    dataName.add(science.getScienceName());
                                }
                                break;
                            case 4:
                                // 年级
                                Grade grade = gradeService.findById(record.getDataId());
                                if(Objects.nonNull(grade)){
                                    dataName.add(grade.getGrade()+"");
                                }
                                break;
                            case 5:
                                // 班级
                                Organize organize = organizeService.findById(record.getDataId());
                                if(Objects.nonNull(organize)){
                                    dataName.add(organize.getOrganizeName());
                                }
                                break;
                        }
                    }
                }

                bean.setDataName(dataName);
            }
        }
        simplePaginationUtil.setTotalSize(leaverRegisterReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
