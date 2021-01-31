package top.zbeboy.zone.api.campus.opens;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.campus.opens.SchoolOpensBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SchoolOpens;
import top.zbeboy.zbase.domain.tables.pojos.SchoolOpensContent;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.opens.CampusOpensService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
public class CampusOpensApiController {

    @Resource
    private CampusOpensService campusOpensService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "校园开学数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/campus/opens/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<SchoolOpensBean> ajaxUtil = campusOpensService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param id 发布id
     * @return 数据
     */
    @GetMapping("/api/campus/opens/look/{id}")
    public ResponseEntity<Map<String, Object>> look(@PathVariable("id") String id) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Optional<SchoolOpens> optionalSchoolOpens = campusOpensService.findById(id);
        Optional<SchoolOpensContent> optionalSchoolOpensContent = campusOpensService.content(id);
        if (optionalSchoolOpens.isPresent() && optionalSchoolOpensContent.isPresent()) {
            ajaxUtil.success().put("schoolOpens", optionalSchoolOpens.get()).put("schoolOpensContent", optionalSchoolOpensContent.get());
        } else {
            ajaxUtil.fail().msg("未查询到开学内容");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
