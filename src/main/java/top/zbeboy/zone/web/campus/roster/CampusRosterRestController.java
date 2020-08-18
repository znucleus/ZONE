package top.zbeboy.zone.web.campus.roster;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.campus.roster.RosterReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.roster.RosterReleaseService;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.QRCodeUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.campus.roster.RosterReleaseAddVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@RestController
public class CampusRosterRestController {

    @Resource
    private RosterReleaseService rosterReleaseService;

    private final String ANYONE_DATE_ADD_URL = "/anyone/campus/roster/data/add/";

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/campus/roster/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<RosterReleaseBean> ajaxUtil = rosterReleaseService.data(simplePaginationUtil);
        if(Objects.nonNull(ajaxUtil.getListResult())){
            for(RosterReleaseBean bean : ajaxUtil.getListResult()){
                bean.setPublicLink(RequestUtil.getBaseUrl(request) + ANYONE_DATE_ADD_URL + bean.getRosterReleaseId());
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param rosterReleaseAddVo 数据
     * @return true or false
     */
    @PostMapping("/web/campus/roster/save")
    public ResponseEntity<Map<String, Object>> save(RosterReleaseAddVo rosterReleaseAddVo, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            rosterReleaseAddVo.setUsername(users.getUsername());
            String id = UUIDUtil.getUUID();
            rosterReleaseAddVo.setRosterReleaseId(id);
            String realPath = RequestUtil.getRealPath(request);
            String path = Workbook.campusRosterQrCodeFilePath() + id + ".jpg";
            String logoPath = Workbook.SYSTEM_LOGO_PATH;
            //生成二维码
            String text = RequestUtil.getBaseUrl(request) + ANYONE_DATE_ADD_URL + id;
            QRCodeUtil.encode(text, logoPath, realPath + path, true);
            rosterReleaseAddVo.setQrCodeUrl(path);
            ajaxUtil = rosterReleaseService.save(rosterReleaseAddVo);
        } catch (Exception e) {
            ajaxUtil.fail().msg("保存失败: 异常: " + e.getMessage());
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
