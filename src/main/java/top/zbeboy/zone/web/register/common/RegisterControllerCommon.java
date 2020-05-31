package top.zbeboy.zone.web.register.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterDataOptionRecord;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterOptionRecord;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterScopeRecord;
import top.zbeboy.zone.feign.data.CollegeService;
import top.zbeboy.zone.feign.data.DepartmentService;
import top.zbeboy.zone.feign.data.ScienceService;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.UsersService;
import top.zbeboy.zone.service.data.*;
import top.zbeboy.zone.service.export.LeaverRegisterDataExport;
import top.zbeboy.zone.service.register.*;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterDataBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterOptionBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterReleaseBean;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterScopeBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.util.pagination.ExportInfo;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterDataVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseAddVo;
import top.zbeboy.zone.web.vo.register.leaver.LeaverRegisterReleaseEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Component
public class RegisterControllerCommon {

    @Resource
    private LeaverRegisterReleaseService leaverRegisterReleaseService;

    @Resource
    private LeaverRegisterScopeService leaverRegisterScopeService;

    @Resource
    private LeaverRegisterOptionService leaverRegisterOptionService;

    @Resource
    private LeaverRegisterDataService leaverRegisterDataService;

    @Resource
    private LeaverRegisterDataOptionService leaverRegisterDataOptionService;

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
    private StudentService studentService;

    @Resource
    private UploadService uploadService;

    /**
     * 发布列表数据
     *
     * @param simplePaginationUtil 工具
     * @param channel              渠道
     * @param principal            用户
     * @return 数据
     */
    public AjaxUtil<LeaverRegisterReleaseBean> data(SimplePaginationUtil simplePaginationUtil, String channel, Principal principal) {
        AjaxUtil<LeaverRegisterReleaseBean> ajaxUtil = AjaxUtil.of();
        List<LeaverRegisterReleaseBean> beans = new ArrayList<>();
        Result<Record> records = leaverRegisterReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(LeaverRegisterReleaseBean.class);
            for (LeaverRegisterReleaseBean bean : beans) {
                bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime()));
                bean.setCanOperator(BooleanUtil.toByte(registerConditionCommon.leaverOperator(bean.getLeaverRegisterReleaseId(), channel, principal)));
                bean.setCanReview(BooleanUtil.toByte(registerConditionCommon.leaverReview(bean.getLeaverRegisterReleaseId(), channel, principal)));
                bean.setCanRegister(BooleanUtil.toByte(registerConditionCommon.leaverRegister(bean.getLeaverRegisterReleaseId(), channel, principal)));
                bean.setIsRegister(BooleanUtil.toByte(registerConditionCommon.isRegisterLeaver(bean.getLeaverRegisterReleaseId(), channel, principal)));

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
                                if (Objects.nonNull(college) && college.getCollegeId() > 0) {
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
        return ajaxUtil;
    }

    /**
     * 发布保存
     *
     * @param leaverRegisterReleaseAddVo 数据
     * @param bindingResult              检验
     * @param principal                  用户
     * @return true or false
     */
    public AjaxUtil<Map<String, Object>> save(LeaverRegisterReleaseAddVo leaverRegisterReleaseAddVo, BindingResult bindingResult, String channel, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Users users = SessionUtil.getUserByChannel(channel, principal);
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
        return ajaxUtil;
    }

    /**
     * 选项删除
     *
     * @param leaverRegisterOptionId  选项id
     * @param leaverRegisterReleaseId 发布id
     * @param channel                 渠道
     * @param principal               用户
     * @return true or false
     */
    public AjaxUtil<Map<String, Object>> optionDelete(String leaverRegisterOptionId, String leaverRegisterReleaseId,
                                                      String channel, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.leaverOperator(leaverRegisterReleaseId, channel, principal)) {
            leaverRegisterOptionService.deleteById(leaverRegisterOptionId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return ajaxUtil;
    }

    /**
     * 选项内容更新
     *
     * @param leaverRegisterOptionId  选项id
     * @param leaverRegisterReleaseId 发布id
     * @param optionContent           选项内容
     * @return true or false
     */
    public AjaxUtil<Map<String, Object>> optionUpdate(String leaverRegisterOptionId,
                                                      String leaverRegisterReleaseId,
                                                      String optionContent, String channel,
                                                      Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.leaverOperator(leaverRegisterReleaseId, channel, principal)) {
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
        return ajaxUtil;
    }

    /**
     * 更新
     *
     * @param leaverRegisterReleaseEditVo 数据
     * @param bindingResult               检验
     * @return true or false
     */
    public AjaxUtil<Map<String, Object>> update(LeaverRegisterReleaseEditVo leaverRegisterReleaseEditVo,
                                                BindingResult bindingResult, String channel, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (registerConditionCommon.leaverOperator(leaverRegisterReleaseEditVo.getLeaverRegisterReleaseId(), channel, principal)) {
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
                if (StringUtils.isNotBlank(optionContentStr)) {
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
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ajaxUtil;
    }

    /**
     * 删除
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    public AjaxUtil<Map<String, Object>> delete(String leaverRegisterReleaseId, String channel,
                                                Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.leaverOperator(leaverRegisterReleaseId, channel, principal)) {
            leaverRegisterReleaseService.deleteById(leaverRegisterReleaseId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return ajaxUtil;
    }

    /**
     * 保存
     *
     * @param leaverRegisterDataVo 数据
     * @param bindingResult        检验
     * @return true or false
     */
    public AjaxUtil<Map<String, Object>> dataSave(LeaverRegisterDataVo leaverRegisterDataVo, BindingResult bindingResult,
                                                  String channel, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (registerConditionCommon.leaverRegister(leaverRegisterDataVo.getLeaverRegisterReleaseId(), channel, principal)) {
                Users users = SessionUtil.getUserByChannel(channel, principal);
                StudentBean studentBean = studentService.findByUsername(users.getUsername());
                if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                    Optional<Record> leaverRegisterDataRecord = leaverRegisterDataService.findByLeaverRegisterReleaseIdAndStudentId(leaverRegisterDataVo.getLeaverRegisterReleaseId(), studentBean.getStudentId());
                    if (!leaverRegisterDataRecord.isPresent()) {
                        LeaverRegisterData leaverRegisterData = new LeaverRegisterData();
                        String leaverRegisterDataId = UUIDUtil.getUUID();
                        leaverRegisterData.setLeaverRegisterDataId(leaverRegisterDataId);
                        leaverRegisterData.setStudentId(studentBean.getStudentId());
                        leaverRegisterData.setLeaverRegisterReleaseId(leaverRegisterDataVo.getLeaverRegisterReleaseId());
                        leaverRegisterData.setLeaverAddress(leaverRegisterDataVo.getLeaverAddress());
                        leaverRegisterData.setRegisterDate(DateTimeUtil.getNowSqlTimestamp());
                        leaverRegisterData.setRemark(leaverRegisterDataVo.getRemark());

                        leaverRegisterDataService.save(leaverRegisterData);

                        String[] leaverRegisterOptionIds = leaverRegisterDataVo.getLeaverRegisterOptionId();
                        if (Objects.nonNull(leaverRegisterOptionIds) && leaverRegisterOptionIds.length > 0) {
                            for (String id : leaverRegisterOptionIds) {
                                LeaverRegisterDataOption leaverRegisterDataOption = new LeaverRegisterDataOption();
                                leaverRegisterDataOption.setLeaverRegisterDataId(leaverRegisterDataId);
                                leaverRegisterDataOption.setLeaverRegisterOptionId(id);

                                leaverRegisterDataOptionService.save(leaverRegisterDataOption);
                            }
                        }

                        ajaxUtil.success().msg("保存成功");
                    } else {
                        ajaxUtil.fail().msg("已登记，不能重复登记");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到学生数据");
                }
            } else {
                ajaxUtil.fail().msg("您不满足登记条件");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ajaxUtil;
    }

    /**
     * 删除
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    public AjaxUtil<Map<String, Object>> dataDelete(String leaverRegisterReleaseId, String channel,
                                                    Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.leaverRegister(leaverRegisterReleaseId, channel, principal)) {
            Users users = SessionUtil.getUserByChannel(channel, principal);
            StudentBean studentBean = studentService.findByUsername(users.getUsername());
            if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                leaverRegisterDataService.deleteByLeaverRegisterReleaseIdAndStudentId(leaverRegisterReleaseId, studentBean.getStudentId());
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("未查询到学生数据");
            }
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return ajaxUtil;
    }

    /**
     * 删除登记
     *
     * @param leaverRegisterReleaseId 发布id
     * @return true or false
     */
    public AjaxUtil<Map<String, Object>> dataListDelete(String leaverRegisterReleaseId,
                                                        String leaverRegisterDataId, String channel,
                                                        Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.leaverReview(leaverRegisterReleaseId, channel, principal)) {
            leaverRegisterDataService.deleteById(leaverRegisterDataId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return ajaxUtil;
    }

    /**
     * 统计列表数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    public AjaxUtil<LeaverRegisterDataBean> dataList(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<LeaverRegisterDataBean> ajaxUtil = AjaxUtil.of();
        List<LeaverRegisterDataBean> beans = new ArrayList<>();
        Result<Record> records = leaverRegisterDataService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(LeaverRegisterDataBean.class);
            beans.forEach(bean -> bean.setRegisterDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getRegisterDate())));
            beans.forEach(this::mergeOption);
        }
        simplePaginationUtil.setTotalSize(leaverRegisterDataService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return ajaxUtil;
    }

    /**
     * 统计列表数据导出
     *
     * @param request 请求
     */
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SimplePaginationUtil simplePaginationUtil = new SimplePaginationUtil(request, "studentNumber", "asc",
                "离校登记数据表", Workbook.registerFilePath());
        Result<Record> records = leaverRegisterDataService.export(simplePaginationUtil);
        List<LeaverRegisterDataBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(LeaverRegisterDataBean.class);
            beans.forEach(this::mergeOption);
        }

        LeaverRegisterDataExport export = new LeaverRegisterDataExport(beans);
        ExportInfo exportInfo = simplePaginationUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }

    /**
     * 获取选项数据
     *
     * @param id 发布id
     * @return 数据
     */
    public List<LeaverRegisterOption> leaverRegisterOptions(String id) {
        List<LeaverRegisterOption> leaverRegisterOptions = new ArrayList<>();
        Result<LeaverRegisterOptionRecord> leaverRegisterOptionRecords =
                leaverRegisterOptionService.findByLeaverRegisterReleaseId(id);
        if (leaverRegisterOptionRecords.isNotEmpty()) {
            leaverRegisterOptions = leaverRegisterOptionRecords.into(LeaverRegisterOption.class);
        }
        return leaverRegisterOptions;
    }

    /**
     * 获取数据域
     *
     * @param id        发布id
     * @param dataScope 域
     * @return 数据
     */
    public List<LeaverRegisterScopeBean> leaverRegisterScopes(String id, int dataScope) {
        List<LeaverRegisterScopeBean> leaverRegisterScopes = new ArrayList<>();
        Result<LeaverRegisterScopeRecord> leaverRegisterScopeRecords =
                leaverRegisterScopeService.findByLeaverRegisterReleaseId(id);
        if (leaverRegisterScopeRecords.isNotEmpty()) {
            leaverRegisterScopes = leaverRegisterScopeRecords.into(LeaverRegisterScopeBean.class);
            for (LeaverRegisterScopeBean bean : leaverRegisterScopes) {
                switch (dataScope) {
                    case 1:
                        // 院
                        College college = collegeService.findById(bean.getDataId());
                        if (Objects.nonNull(college) && college.getCollegeId() > 0) {
                            bean.setDataName(college.getCollegeName());
                        }
                        break;
                    case 2:
                        // 系
                        Department department = departmentService.findById(bean.getDataId());
                        if (Objects.nonNull(department)) {
                            bean.setDataName(department.getDepartmentName());
                        }
                        break;
                    case 3:
                        // 专业
                        Science science = scienceService.findById(bean.getDataId());
                        if (Objects.nonNull(science)) {
                            bean.setDataName(science.getScienceName());
                        }
                        break;
                    case 4:
                        // 年级
                        Grade grade = gradeService.findById(bean.getDataId());
                        if (Objects.nonNull(grade)) {
                            bean.setDataName(grade.getGrade() + "");
                        }
                        break;
                    case 5:
                        // 班级
                        Organize organize = organizeService.findById(bean.getDataId());
                        if (Objects.nonNull(organize)) {
                            bean.setDataName(organize.getOrganizeName());
                        }
                        break;
                }
            }
        }
        return leaverRegisterScopes;
    }

    /**
     * 合并选项数据
     *
     * @param bean 数据
     */
    private void mergeOption(LeaverRegisterDataBean bean) {
        // 查询全部选项
        List<LeaverRegisterOptionBean> leaverRegisterOptionBeans = new ArrayList<>();
        Result<LeaverRegisterOptionRecord> leaverRegisterOptionRecords =
                leaverRegisterOptionService.findByLeaverRegisterReleaseId(bean.getLeaverRegisterReleaseId());
        if (leaverRegisterOptionRecords.isNotEmpty()) {
            leaverRegisterOptionBeans = leaverRegisterOptionRecords.into(LeaverRegisterOptionBean.class);
            for (LeaverRegisterOptionBean leaverRegisterOptionBean : leaverRegisterOptionBeans) {
                // 查询用户选择
                Optional<LeaverRegisterDataOptionRecord> leaverRegisterDataOptionRecord =
                        leaverRegisterDataOptionService.findByLeaverRegisterDataIdAndLeaverRegisterOptionId(bean.getLeaverRegisterDataId(), leaverRegisterOptionBean.getLeaverRegisterOptionId());
                if (leaverRegisterDataOptionRecord.isPresent()) {
                    leaverRegisterOptionBean.setIsChecked(BooleanUtil.toByte(true));
                }
            }
        }

        bean.setLeaverRegisterOptions(leaverRegisterOptionBeans);
    }
}
