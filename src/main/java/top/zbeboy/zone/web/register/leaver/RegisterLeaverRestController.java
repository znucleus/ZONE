package top.zbeboy.zone.web.register.leaver;

import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterScopeRecord;
import top.zbeboy.zone.service.data.*;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.register.LeaverRegisterOptionService;
import top.zbeboy.zone.service.register.LeaverRegisterReleaseService;
import top.zbeboy.zone.service.register.LeaverRegisterScopeService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterReleaseBean;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    private LeaverRegisterOptionService leaverRegisterOptionService;

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

    @Resource
    private UsersService usersService;

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

                switch (bean.getDataScope()) {
                    case 1:
                        // 院
                        bean.setDataScopeName("院");
                        break;
                    case 2:
                        // 系
                        bean.setDataScopeName("系");
                        break;
                    case 3:
                        // 专业
                        bean.setDataScopeName("专业");
                        break;
                    case 4:
                        // 年级
                        bean.setDataScopeName("年级");
                        break;
                    case 5:
                        // 班级
                        bean.setDataScopeName("班级");
                        break;
                }

                List<String> dataName = new ArrayList<>();
                Result<LeaverRegisterScopeRecord> registerScopeRecords = leaverRegisterScopeService.findByLeaverRegisterReleaseId(bean.getLeaverRegisterReleaseId());
                if (registerScopeRecords.isNotEmpty()) {
                    for (LeaverRegisterScopeRecord record : registerScopeRecords) {
                        switch (bean.getDataScope()) {
                            case 1:
                                // 院
                                College college = collegeService.findById(record.getDataId());
                                if (Objects.nonNull(college)) {
                                    dataName.add(college.getCollegeName());
                                }
                                break;
                            case 2:
                                // 系
                                Department department = departmentService.findById(record.getDataId());
                                if (Objects.nonNull(department)) {
                                    dataName.add(department.getDepartmentName());
                                }
                                break;
                            case 3:
                                // 专业
                                Science science = scienceService.findById(record.getDataId());
                                if (Objects.nonNull(science)) {
                                    dataName.add(science.getScienceName());
                                }
                                break;
                            case 4:
                                // 年级
                                Grade grade = gradeService.findById(record.getDataId());
                                if (Objects.nonNull(grade)) {
                                    dataName.add(grade.getGrade() + "");
                                }
                                break;
                            case 5:
                                // 班级
                                Organize organize = organizeService.findById(record.getDataId());
                                if (Objects.nonNull(organize)) {
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

    /**
     * 保存
     *
     * @param leaverRegisterReleaseAddVo 数据
     * @param bindingResult              检验
     * @return true or false
     */
    @PostMapping("/web/register/leaver/release/save")
    public ResponseEntity<Map<String, Object>> save(@Valid LeaverRegisterReleaseAddVo leaverRegisterReleaseAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Users users = usersService.getUserFromSession();
            LeaverRegisterRelease leaverRegisterRelease = new LeaverRegisterRelease();
            String leaverRegisterReleaseId = UUIDUtil.getUUID();
            leaverRegisterRelease.setLeaverRegisterReleaseId(leaverRegisterReleaseId);
            leaverRegisterRelease.setTitle(leaverRegisterReleaseAddVo.getTitle());
            leaverRegisterRelease.setUsername(users.getUsername());
            leaverRegisterRelease.setPublisher(users.getRealName());
            leaverRegisterRelease.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());
            leaverRegisterRelease.setDataScope(leaverRegisterReleaseAddVo.getDataScope());

            leaverRegisterReleaseService.save(leaverRegisterRelease);

            String dataIdStr = leaverRegisterReleaseAddVo.getDataId();
            String[] dataIds = dataIdStr.split(",");
            for (String dataId : dataIds) {
                LeaverRegisterScope leaverRegisterScope = new LeaverRegisterScope();
                leaverRegisterScope.setLeaverRegisterReleaseId(leaverRegisterReleaseId);
                leaverRegisterScope.setDataId(NumberUtils.toInt(dataId));

                leaverRegisterScopeService.save(leaverRegisterScope);
            }

            String optionContentStr = leaverRegisterReleaseAddVo.getOptionContent();
            String[] optionContents = optionContentStr.split("\\|");
            List<LeaverRegisterOption> leaverRegisterOptions = new ArrayList<>();
            for (int i = 0; i < optionContents.length; i++) {
                LeaverRegisterOption leaverRegisterOption = new LeaverRegisterOption();
                leaverRegisterOption.setLeaverRegisterOptionId(UUIDUtil.getUUID());
                leaverRegisterOption.setLeaverRegisterReleaseId(leaverRegisterReleaseId);
                leaverRegisterOption.setOptionContent(optionContents[i]);
                leaverRegisterOption.setSort(ByteUtil.toByte(i));

                leaverRegisterOptions.add(leaverRegisterOption);
            }

            leaverRegisterOptionService.batchSave(leaverRegisterOptions);

            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
