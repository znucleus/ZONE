package top.zbeboy.zone.web.notify;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.SystemNotify;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.notify.SystemNotifyService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.system.notify.SystemNotifyAddVo;
import top.zbeboy.zbase.vo.system.notify.SystemNotifyEditVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SystemNotifyRestController {

    @Resource
    private SystemNotifyService systemNotifyService;

    /**
     * 获取数据
     *
     * @return 数据
     */
    @GetMapping("/users/system/notify")
    public ResponseEntity<Map<String, Object>> userSystemNotify() {
        AjaxUtil<SystemNotify> ajaxUtil = AjaxUtil.of();
        List<SystemNotify> list = new ArrayList<>();
        Optional<List<SystemNotify>> optionalSystemNotifies = systemNotifyService.findByEffective();
        if(optionalSystemNotifies.isPresent()){
            list = optionalSystemNotifies.get();
        }
        ajaxUtil.success().list(list).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/notify/paging")
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
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(systemNotifyService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param systemNotifyAddVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/notify/save")
    public ResponseEntity<Map<String, Object>> save(SystemNotifyAddVo systemNotifyAddVo) {
        Users users = SessionUtil.getUserFromSession();
        systemNotifyAddVo.setSendUser(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = systemNotifyService.save(systemNotifyAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param systemNotifyEditVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/system/notify/update")
    public ResponseEntity<Map<String, Object>> update(SystemNotifyEditVo systemNotifyEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = systemNotifyService.update(systemNotifyEditVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = systemNotifyService.delete(systemNotifyIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
