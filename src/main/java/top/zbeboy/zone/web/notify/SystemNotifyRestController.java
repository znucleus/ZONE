package top.zbeboy.zone.web.notify;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.SystemNotify;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.SystemNotifyRecord;
import top.zbeboy.zone.feign.platform.UsersService;
import top.zbeboy.zone.service.notify.SystemNotifyService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.notify.SystemNotifyBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.system.notify.SystemNotifyAddVo;
import top.zbeboy.zone.web.vo.system.notify.SystemNotifyEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SystemNotifyRestController {

    @Resource
    private SystemNotifyService systemNotifyService;

    @Resource
    private UsersService usersService;

    /**
     * 获取数据
     *
     * @return 数据
     */
    @GetMapping("/users/system/notify")
    public ResponseEntity<Map<String, Object>> userSystemNotify() {
        AjaxUtil<SystemNotify> ajaxUtil = AjaxUtil.of();
        List<SystemNotify> systemNotifies = new ArrayList<>();
        Result<SystemNotifyRecord> records = systemNotifyService.findByEffective();
        if (records.isNotEmpty()) {
            systemNotifies = records.into(SystemNotify.class);
        }
        ajaxUtil.success().list(systemNotifies).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/notify/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("notifyTitle");
        headers.add("notifyContent");
        headers.add("notifyType");
        headers.add("validDateStr");
        headers.add("expireDateStr");
        headers.add("realName");
        headers.add("createDateStr");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = systemNotifyService.findAllByPage(dataTablesUtil);
        List<SystemNotifyBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(SystemNotifyBean.class);
            beans.forEach(bean -> {
                bean.setValidDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getValidDate()));
                bean.setExpireDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getExpireDate()));
                bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate()));
            });
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(systemNotifyService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(systemNotifyService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param systemNotifyAddVo 数据
     * @param bindingResult     检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/notify/save")
    public ResponseEntity<Map<String, Object>> save(@Valid SystemNotifyAddVo systemNotifyAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            SystemNotify systemNotify = new SystemNotify();
            systemNotify.setSystemNotifyId(UUIDUtil.getUUID());
            systemNotify.setNotifyTitle(systemNotifyAddVo.getNotifyTitle());
            systemNotify.setNotifyContent(systemNotifyAddVo.getNotifyContent());
            systemNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
            systemNotify.setValidDate(DateTimeUtil.defaultParseSqlTimestamp(systemNotifyAddVo.getValidDate()));
            systemNotify.setExpireDate(DateTimeUtil.defaultParseSqlTimestamp(systemNotifyAddVo.getExpireDate()));
            systemNotify.setNotifyType(systemNotifyAddVo.getNotifyType());
            Users users = SessionUtil.getUserFromSession();
            systemNotify.setSendUser(users.getUsername());

            systemNotifyService.save(systemNotify);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param systemNotifyEditVo 数据
     * @param bindingResult      检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/notify/update")
    public ResponseEntity<Map<String, Object>> update(@Valid SystemNotifyEditVo systemNotifyEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            SystemNotify systemNotify = systemNotifyService.findById(systemNotifyEditVo.getSystemNotifyId());
            if (Objects.nonNull(systemNotify)) {
                systemNotify.setNotifyTitle(systemNotifyEditVo.getNotifyTitle());
                systemNotify.setNotifyContent(systemNotifyEditVo.getNotifyContent());
                systemNotify.setValidDate(DateTimeUtil.defaultParseSqlTimestamp(systemNotifyEditVo.getValidDate()));
                systemNotify.setExpireDate(DateTimeUtil.defaultParseSqlTimestamp(systemNotifyEditVo.getExpireDate()));
                systemNotify.setNotifyType(systemNotifyEditVo.getNotifyType());

                systemNotifyService.update(systemNotify);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("未查询到通知数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量删除
     *
     * @param systemNotifyIds ids
     * @return true
     */
    @PostMapping("/web/system/notify/delete")
    public ResponseEntity<Map<String, Object>> delete(String systemNotifyIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(systemNotifyIds)) {
            List<String> ids = SmallPropsUtil.StringIdsToStringList(systemNotifyIds);
            ids.forEach(id -> systemNotifyService.deleteById(id));
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("请选择通知");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
