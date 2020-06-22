package top.zbeboy.zone.web.system.configure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.system.config.SystemConfigureEditVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SystemConfigureRestController {

    @Resource
    private SystemConfigureService systemConfigureService;

    /**
     * 获取对外静态参数
     *
     * @return 对外静态参数
     */
    @GetMapping("/anyone/data/configure")
    public ResponseEntity<Map<String, Object>> anyoneConfigure() {
        AjaxUtil<Map<String, Object>> ajaxUtil = systemConfigureService.anyoneConfigure();
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/config/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("dataKey");
        headers.add("dataValue");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(systemConfigureService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存更改
     *
     * @param systemConfigureEditVo 数据
     * @param bindingResult         检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/system/config/update")
    public ResponseEntity<Map<String, Object>> save(@Valid SystemConfigureEditVo systemConfigureEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = systemConfigureService.save(systemConfigureEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
