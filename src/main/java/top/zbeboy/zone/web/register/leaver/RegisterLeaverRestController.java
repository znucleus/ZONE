package top.zbeboy.zone.web.register.leaver;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterScopeRecord;
import top.zbeboy.zone.domain.tables.records.StudentRecord;
import top.zbeboy.zone.service.data.*;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.register.LeaverRegisterDataService;
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
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseEditVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

@RestController
public class RegisterLeaverRestController {

    @Resource
    private LeaverRegisterReleaseService leaverRegisterReleaseService;

    @Resource
    private LeaverRegisterScopeService leaverRegisterScopeService;

    @Resource
    private LeaverRegisterOptionService leaverRegisterOptionService;

    @Resource
    private LeaverRegisterDataService leaverRegisterDataService;

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

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

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
                bean.setCanOperator(BooleanUtil.toByte(registerConditionCommon.leaverOperator(bean.getLeaverRegisterReleaseId())));
                bean.setCanReview(BooleanUtil.toByte(registerConditionCommon.leaverReview(bean.getLeaverRegisterReleaseId())));
                bean.setIsStudent(BooleanUtil.toByte(isStudent()));
                bean.setIsRegister(BooleanUtil.toByte(isRegister(bean.getLeaverRegisterReleaseId())));

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

    /**
     * 选项删除
     *
     * @param leaverRegisterOptionId 选项id
     * @return true or false
     */
    @PostMapping("/web/register/leaver/option/delete")
    public ResponseEntity<Map<String, Object>> optionDelete(@RequestParam("leaverRegisterOptionId") String leaverRegisterOptionId,
                                                            @RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.leaverOperator(leaverRegisterReleaseId)) {
            leaverRegisterOptionService.deleteById(leaverRegisterOptionId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 选项内容更新
     *
     * @param leaverRegisterOptionId  选项id
     * @param leaverRegisterReleaseId 发布id
     * @param optionContent           选项内容
     * @return true or false
     */
    @PostMapping("/web/register/leaver/option/update")
    public ResponseEntity<Map<String, Object>> optionUpdate(@RequestParam("leaverRegisterOptionId") String leaverRegisterOptionId,
                                                            @RequestParam("leaverRegisterReleaseId") String leaverRegisterReleaseId,
                                                            @RequestParam("optionContent") String optionContent) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.leaverOperator(leaverRegisterReleaseId)) {
            LeaverRegisterOption leaverRegisterOption = leaverRegisterOptionService.findById(leaverRegisterOptionId);
            if (Objects.nonNull(leaverRegisterOption)) {
                leaverRegisterOption.setOptionContent(optionContent);
                leaverRegisterOptionService.update(leaverRegisterOption);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("未查询到选项信息");
            }
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param leaverRegisterReleaseEditVo 数据
     * @param bindingResult               检验
     * @return true or false
     */
    @PostMapping("/web/register/leaver/release/update")
    public ResponseEntity<Map<String, Object>> update(@Valid LeaverRegisterReleaseEditVo leaverRegisterReleaseEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(leaverRegisterReleaseEditVo.getLeaverRegisterReleaseId());
            leaverRegisterRelease.setTitle(leaverRegisterReleaseEditVo.getTitle());
            leaverRegisterRelease.setDataScope(leaverRegisterReleaseEditVo.getDataScope());
            leaverRegisterReleaseService.update(leaverRegisterRelease);

            leaverRegisterScopeService.deleteByLeaverRegisterReleaseId(leaverRegisterRelease.getLeaverRegisterReleaseId());
            String dataIdStr = leaverRegisterReleaseEditVo.getDataId();
            String[] dataIds = dataIdStr.split(",");
            for (String dataId : dataIds) {
                LeaverRegisterScope leaverRegisterScope = new LeaverRegisterScope();
                leaverRegisterScope.setLeaverRegisterReleaseId(leaverRegisterRelease.getLeaverRegisterReleaseId());
                leaverRegisterScope.setDataId(NumberUtils.toInt(dataId));

                leaverRegisterScopeService.save(leaverRegisterScope);
            }

            String optionContentStr = leaverRegisterReleaseEditVo.getOptionContent();
            if(StringUtils.isNotBlank(optionContentStr)){
                byte maxSort = leaverRegisterOptionService.findMaxSortByLeaverRegisterReleaseId(leaverRegisterRelease.getLeaverRegisterReleaseId());

                String[] optionContents = optionContentStr.split("\\|");
                List<LeaverRegisterOption> leaverRegisterOptions = new ArrayList<>();
                for (String optionContent : optionContents) {
                    maxSort++;
                    LeaverRegisterOption leaverRegisterOption = new LeaverRegisterOption();
                    leaverRegisterOption.setLeaverRegisterOptionId(UUIDUtil.getUUID());
                    leaverRegisterOption.setLeaverRegisterReleaseId(leaverRegisterRelease.getLeaverRegisterReleaseId());
                    leaverRegisterOption.setOptionContent(optionContent);
                    leaverRegisterOption.setSort(maxSort);

                    leaverRegisterOptions.add(leaverRegisterOption);
                }

                leaverRegisterOptionService.batchSave(leaverRegisterOptions);
            }
            ajaxUtil.success().msg("更新成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    private boolean isRegister(String leaverRegisterReleaseId) {
        boolean isRegister = false;
        Users users = usersService.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StudentRecord> studentRecord = studentService.findByUsername(users.getUsername());
                if (studentRecord.isPresent()) {
                    isRegister = leaverRegisterDataService.findByLeaverRegisterReleaseIdAndStudentId(leaverRegisterReleaseId, studentRecord.get().getStudentId()).isPresent();
                }
            }
        }
        return isRegister;
    }

    private boolean isStudent() {
        boolean isStudent = false;
        Users users = usersService.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                isStudent = true;
            }
        }
        return isStudent;
    }
}
