package top.zbeboy.zone.web.platform.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.AppService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.platform.app.AppAddVo;
import top.zbeboy.zbase.vo.platform.app.AppEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AppRestController {

    @Resource
    private AppService appService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/app/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("appName");
        headers.add("username");
        headers.add("realName");
        headers.add("clientId");
        headers.add("secret");
        headers.add("webServerRedirectUri");
        headers.add("remark");
        headers.add("createDate");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(appService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param appAddVo 应用
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/app/save")
    public ResponseEntity<Map<String, Object>> save(AppAddVo appAddVo) {
        Users users = SessionUtil.getUserFromSession();
        appAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = appService.save(appAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param appEditVo 应用
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/app/update")
    public ResponseEntity<Map<String, Object>> update(AppEditVo appEditVo) {
        Users users = SessionUtil.getUserFromSession();
        appEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = appService.update(appEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 根据客户端id删除
     *
     * @param clientId 客户端id
     * @return true or false
     */
    @PostMapping("/web/platform/app/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("clientId") String clientId) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = appService.delete(users.getUsername(), clientId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 备注
     *
     * @param clientId id
     * @param remark   数据
     * @return 备注
     */
    @PostMapping("/web/platform/app/remark")
    public ResponseEntity<Map<String, Object>> remark(@RequestParam("clientId") String clientId, String remark) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = appService.remark(users.getUsername(), clientId, remark);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
