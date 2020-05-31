package top.zbeboy.zone.api.register.epidemic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Channel;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterData;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.EpidemicRegisterDataRecord;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.data.ChannelService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.register.EpidemicRegisterDataService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterDataAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class RegisterEpidemicApiController {

    @Resource
    private EpidemicRegisterDataService epidemicRegisterDataService;

    @Resource
    private UsersService usersService;

    @Resource
    private ChannelService channelService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    /**
     * 登记
     *
     * @param epidemicRegisterDataAddVo 数据
     * @param bindingResult             检验
     * @return true or false
     */
    @PostMapping("/api/register/epidemic/data/save")
    public ResponseEntity<Map<String, Object>> dataSave(@Valid EpidemicRegisterDataAddVo epidemicRegisterDataAddVo, BindingResult bindingResult, Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Users users = usersService.getUserFromOauth(principal);
            if (Objects.nonNull(users)) {
                Optional<EpidemicRegisterDataRecord> registerDataRecord =
                        epidemicRegisterDataService.findTodayByUsernameAndEpidemicRegisterReleaseId(users.getUsername(),
                                epidemicRegisterDataAddVo.getEpidemicRegisterReleaseId());
                if (!registerDataRecord.isPresent()) {
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

                    Channel channel = channelService.findByChannelName(Workbook.channel.API.name());
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
                    ajaxUtil.fail().msg("今日已登记");
                }
            } else {
                ajaxUtil.fail().msg("获取用户信息失败");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
