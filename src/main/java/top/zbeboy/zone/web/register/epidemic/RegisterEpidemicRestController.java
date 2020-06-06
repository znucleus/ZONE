package top.zbeboy.zone.web.register.epidemic;

import org.apache.commons.lang3.StringUtils;
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
import top.zbeboy.zone.feign.data.ChannelService;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.export.EpidemicRegisterDataExport;
import top.zbeboy.zone.service.register.EpidemicRegisterDataService;
import top.zbeboy.zone.service.register.EpidemicRegisterReleaseService;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterDataBean;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterReleaseBean;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.pagination.ExportInfo;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterDataAddVo;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterReleaseAddVo;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterReleaseEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
public class RegisterEpidemicRestController {

    @Resource
    private EpidemicRegisterReleaseService epidemicRegisterReleaseService;

    @Resource
    private EpidemicRegisterDataService epidemicRegisterDataService;

    @Resource
    private RegisterConditionCommon registerConditionCommon;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private ChannelService channelService;

    @Resource
    private UploadService uploadService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/register/epidemic/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<EpidemicRegisterReleaseBean> ajaxUtil = AjaxUtil.of();
        List<EpidemicRegisterReleaseBean> beans = new ArrayList<>();
        Result<Record> records = epidemicRegisterReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(EpidemicRegisterReleaseBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(registerConditionCommon.epidemicOperator())));
            beans.forEach(bean -> bean.setCanReview(BooleanUtil.toByte(registerConditionCommon.epidemicReview())));
        }
        simplePaginationUtil.setTotalSize(epidemicRegisterReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param epidemicRegisterReleaseAddVo 数据
     * @param bindingResult                检验
     * @return true or false
     */
    @PostMapping("/web/register/epidemic/release/save")
    public ResponseEntity<Map<String, Object>> save(@Valid EpidemicRegisterReleaseAddVo epidemicRegisterReleaseAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (registerConditionCommon.epidemicOperator()) {
                Users users = SessionUtil.getUserFromSession();
                EpidemicRegisterRelease epidemicRegisterRelease = new EpidemicRegisterRelease();
                epidemicRegisterRelease.setEpidemicRegisterReleaseId(UUIDUtil.getUUID());
                epidemicRegisterRelease.setTitle(epidemicRegisterReleaseAddVo.getTitle());
                epidemicRegisterRelease.setUsername(users.getUsername());
                epidemicRegisterRelease.setPublisher(users.getRealName());
                epidemicRegisterRelease.setReleaseTime(DateTimeUtil.getNowSqlTimestamp());

                epidemicRegisterReleaseService.save(epidemicRegisterRelease);
                ajaxUtil.success().msg("保存成功");
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param epidemicRegisterReleaseEditVo 数据
     * @param bindingResult                 检验
     * @return true or false
     */
    @PostMapping("/web/register/epidemic/release/update")
    public ResponseEntity<Map<String, Object>> update(@Valid EpidemicRegisterReleaseEditVo epidemicRegisterReleaseEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (registerConditionCommon.epidemicOperator()) {
                EpidemicRegisterRelease epidemicRegisterRelease = epidemicRegisterReleaseService.findById(epidemicRegisterReleaseEditVo.getEpidemicRegisterReleaseId());
                if (Objects.nonNull(epidemicRegisterRelease)) {
                    epidemicRegisterRelease.setTitle(epidemicRegisterReleaseEditVo.getTitle());
                    epidemicRegisterReleaseService.update(epidemicRegisterRelease);
                    ajaxUtil.success().msg("更新成功");
                } else {
                    ajaxUtil.fail().msg("未查询到疫情发布数据");
                }
            } else {
                ajaxUtil.fail().msg("您无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param epidemicRegisterReleaseId id
     * @return true or false
     */
    @PostMapping("/web/register/epidemic/release/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("epidemicRegisterReleaseId") String epidemicRegisterReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.epidemicOperator()) {
            epidemicRegisterReleaseService.deleteById(epidemicRegisterReleaseId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 登记
     *
     * @param epidemicRegisterDataAddVo 数据
     * @param bindingResult             检验
     * @return true or false
     */
    @PostMapping("/web/register/epidemic/data/save")
    public ResponseEntity<Map<String, Object>> dataSave(@Valid EpidemicRegisterDataAddVo epidemicRegisterDataAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (StringUtils.isBlank(epidemicRegisterDataAddVo.getEpidemicRegisterDataId())) {
                Users users = SessionUtil.getUserFromSession();
                EpidemicRegisterData epidemicRegisterData = new EpidemicRegisterData();
                epidemicRegisterData.setEpidemicRegisterDataId(UUIDUtil.getUUID());
                epidemicRegisterData.setEpidemicRegisterReleaseId(epidemicRegisterDataAddVo.getEpidemicRegisterReleaseId());
                epidemicRegisterData.setLocation(epidemicRegisterDataAddVo.getLocation());
                epidemicRegisterData.setAddress(epidemicRegisterDataAddVo.getAddress());
                epidemicRegisterData.setEpidemicStatus(epidemicRegisterDataAddVo.getEpidemicStatus());
                epidemicRegisterData.setRegisterRealName(users.getRealName());
                epidemicRegisterData.setRegisterDate(DateTimeUtil.getNowSqlTimestamp());
                epidemicRegisterData.setRegisterUsername(users.getUsername());
                epidemicRegisterData.setRemark(epidemicRegisterDataAddVo.getRemark());

                Channel channel = channelService.findByChannelName(Workbook.channel.WEB.name());
                epidemicRegisterData.setChannelId(channel.getChannelId());

                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    epidemicRegisterData.setRegisterType(usersType.getUsersTypeName());
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                            epidemicRegisterData.setInstitute(bean.getSchoolName() + "-" + bean.getCollegeName() + "-" + bean.getDepartmentName());
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                            epidemicRegisterData.setInstitute(studentBean.getSchoolName() + "-" + studentBean.getCollegeName() + "-" + studentBean.getDepartmentName()
                                    + "-" + studentBean.getScienceName() + "-" + studentBean.getGrade() + "-" + studentBean.getOrganizeName());
                        }
                    } else {
                        epidemicRegisterData.setInstitute("未知");
                    }
                }

                epidemicRegisterDataService.save(epidemicRegisterData);
                ajaxUtil.success().msg("保存成功");
            } else {
                EpidemicRegisterData epidemicRegisterData = epidemicRegisterDataService.findById(epidemicRegisterDataAddVo.getEpidemicRegisterDataId());
                if (Objects.nonNull(epidemicRegisterData)) {
                    Users users = SessionUtil.getUserFromSession();
                    if (StringUtils.equals(users.getUsername(), epidemicRegisterData.getRegisterUsername())) {
                        epidemicRegisterData.setLocation(epidemicRegisterDataAddVo.getLocation());
                        epidemicRegisterData.setAddress(epidemicRegisterDataAddVo.getAddress());
                        epidemicRegisterData.setEpidemicStatus(epidemicRegisterDataAddVo.getEpidemicStatus());
                        epidemicRegisterData.setRegisterDate(DateTimeUtil.getNowSqlTimestamp());
                        epidemicRegisterData.setRemark(epidemicRegisterDataAddVo.getRemark());

                        Channel channel = channelService.findByChannelName(Workbook.channel.WEB.name());
                        epidemicRegisterData.setChannelId(channel.getChannelId());

                        epidemicRegisterDataService.update(epidemicRegisterData);
                        ajaxUtil.success().msg("更新成功");
                    } else {
                        ajaxUtil.fail().msg("非本人，不允许操作");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到登记数据");
                }
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/register/epidemic/data/list")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("registerRealName");
        headers.add("registerUsername");
        headers.add("registerType");
        headers.add("epidemicStatus");
        headers.add("address");
        headers.add("institute");
        headers.add("channelName");
        headers.add("remark");
        headers.add("registerDateStr");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = epidemicRegisterDataService.findAllByPage(dataTablesUtil);
        List<EpidemicRegisterDataBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(EpidemicRegisterDataBean.class);
            beans.forEach(bean -> bean.setRegisterDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getRegisterDate())));
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(epidemicRegisterDataService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(epidemicRegisterDataService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 登记删除
     *
     * @param epidemicRegisterDataId 发布id
     * @return true or false
     */
    @PostMapping("/web/register/epidemic/data/delete")
    public ResponseEntity<Map<String, Object>> dataDelete(@RequestParam("epidemicRegisterDataId") String epidemicRegisterDataId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (registerConditionCommon.epidemicDelete(epidemicRegisterDataId)) {
            epidemicRegisterDataService.deleteById(epidemicRegisterDataId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 导出 列表 数据
     *
     * @param request 请求
     */
    @GetMapping("/web/register/epidemic/data/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "registerDateStr", "desc",
                "疫情登记数据表", Workbook.registerFilePath());
        Result<Record> records = epidemicRegisterDataService.export(dataTablesUtil);
        List<EpidemicRegisterDataBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(EpidemicRegisterDataBean.class);
            beans.forEach(bean -> bean.setRegisterDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getRegisterDate())));
        }

        EpidemicRegisterDataExport export = new EpidemicRegisterDataExport(beans);
        ExportInfo exportInfo = dataTablesUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }

    }
}
