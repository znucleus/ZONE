package top.zbeboy.zone.web.register.epidemic;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.register.EpidemicRegisterReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterReleaseBean;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.register.epidemic.EpidemicRegisterReleaseAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class RegisterEpidemicRestController {

    @Resource
    private EpidemicRegisterReleaseService epidemicRegisterReleaseService;

    @Resource
    private RegisterConditionCommon registerConditionCommon;

    @Resource
    private UsersService usersService;

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
}
