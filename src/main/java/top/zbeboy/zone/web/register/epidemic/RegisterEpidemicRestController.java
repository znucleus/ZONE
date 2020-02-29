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
import top.zbeboy.zone.service.data.ChannelService;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.register.EpidemicRegisterDataService;
import top.zbeboy.zone.service.register.EpidemicRegisterReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterReleaseBean;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterDataAddVo;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterReleaseAddVo;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterReleaseEditVo;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private ChannelService channelService;

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
                Users users = usersService.getUserFromSession();
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
            EpidemicRegisterRelease epidemicRegisterRelease = epidemicRegisterReleaseService.findById(epidemicRegisterDataAddVo.getEpidemicRegisterReleaseId());
            if (Objects.nonNull(epidemicRegisterRelease)) {
                Users users = usersService.getUserFromSession();
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
                        Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            StaffBean bean = record.get().into(StaffBean.class);
                            epidemicRegisterData.setInstitute(bean.getSchoolName() + "-" + bean.getCollegeName() + "-" + bean.getDepartmentName());
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            StudentBean bean = record.get().into(StudentBean.class);
                            epidemicRegisterData.setInstitute(bean.getSchoolName() + "-" + bean.getCollegeName() + "-" + bean.getDepartmentName()
                                    + "-" + bean.getScienceName() + "-" + bean.getGrade() + "-" + bean.getOrganizeName());
                        }
                    } else {
                        epidemicRegisterData.setInstitute("未知");
                    }
                }

                epidemicRegisterDataService.save(epidemicRegisterData);

                ajaxUtil.success().msg("保存成功");
            } else {
                ajaxUtil.fail().msg("未查询到疫情发布数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
