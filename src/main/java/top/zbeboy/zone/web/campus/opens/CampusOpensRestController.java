package top.zbeboy.zone.web.campus.opens;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.campus.opens.SchoolOpensAuthoritiesBean;
import top.zbeboy.zbase.bean.campus.opens.SchoolOpensBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.opens.SchoolOpensService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.tools.web.util.pagination.TableSawUtil;
import top.zbeboy.zbase.vo.campus.opens.SchoolOpensAddVo;
import top.zbeboy.zbase.vo.campus.opens.SchoolOpensAuthoritiesAddVo;
import top.zbeboy.zbase.vo.campus.opens.SchoolOpensEditVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class CampusOpensRestController {

    @Resource
    private SchoolOpensService schoolOpensService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "校园开学数据", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/campus/opens/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<SchoolOpensBean> ajaxUtil = schoolOpensService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param schoolOpensAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/campus/opens/save")
    public ResponseEntity<Map<String, Object>> save(SchoolOpensAddVo schoolOpensAddVo) {
        Users users = SessionUtil.getUserFromSession();
        schoolOpensAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolOpensService.save(schoolOpensAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param schoolOpensEditVo 数据
     * @return true or false
     */
    @PostMapping("/web/campus/opens/update")
    public ResponseEntity<Map<String, Object>> update(SchoolOpensEditVo schoolOpensEditVo) {
        Users users = SessionUtil.getUserFromSession();
        schoolOpensEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolOpensService.update(schoolOpensEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 发布id
     * @return true or false
     */
    @PostMapping("/web/campus/opens/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("id") String id) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolOpensService.delete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 权限数据
     *
     * @param tableSawUtil 请求
     * @return 数据
     */
    @GetMapping("/web/campus/opens/authorize/data")
    public ResponseEntity<Map<String, Object>> authorizeData(TableSawUtil tableSawUtil) {
        Users users = SessionUtil.getUserFromSession();
        tableSawUtil.setUsername(users.getUsername());
        AjaxUtil<SchoolOpensAuthoritiesBean> ajaxUtil = schoolOpensService.authorizeData(tableSawUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param schoolOpensAuthoritiesAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/campus/opens/authorize/save")
    public ResponseEntity<Map<String, Object>> authorizeSave(SchoolOpensAuthoritiesAddVo schoolOpensAuthoritiesAddVo) {
        Users users = SessionUtil.getUserFromSession();
        schoolOpensAuthoritiesAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolOpensService.authorizeSave(schoolOpensAuthoritiesAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 权限id
     * @return true or false
     */
    @PostMapping("/web/campus/opens/authorize/delete")
    public ResponseEntity<Map<String, Object>> authorizeDelete(@RequestParam("id") String id) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolOpensService.authorizeDelete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
