package top.zbeboy.zone.api.campus.roster;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.campus.roster.RosterDataBean;
import top.zbeboy.zbase.bean.campus.roster.RosterReleaseBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.roster.CampusRosterService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.campus.roster.RosterDataAddVo;
import top.zbeboy.zbase.vo.campus.roster.RosterDataEditVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.campus.common.CampusUrlCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class CampusRosterApiController {

    @Resource
    private StudentService studentService;

    @Resource
    private CampusRosterService campusRosterService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "校园花名册数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/campus/roster/release/paging")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<RosterReleaseBean> ajaxUtil = campusRosterService.data(simplePaginationUtil);
        if (Objects.nonNull(ajaxUtil.getListResult())) {
            for (RosterReleaseBean bean : ajaxUtil.getListResult()) {
                bean.setPublicLink(RequestUtil.getBaseUrl(request) + CampusUrlCommon.ANYONE_ROSTER_DATE_ADD_URL + bean.getRosterReleaseId());
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据保存
     *
     * @param rosterDataAddVo 数据
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园开学内部数据保存", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/campus/roster/data/save")
    public ResponseEntity<Map<String, Object>> dataInsideSave(@Valid RosterDataAddVo rosterDataAddVo, BindingResult bindingResult, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (campusRosterService.canRegister(users.getUsername(), rosterDataAddVo.getRosterReleaseId()) &&
                    !campusRosterService.canDataEdit(users.getUsername(), rosterDataAddVo.getRosterReleaseId())) {
                ajaxUtil = campusRosterService.dataSave(rosterDataAddVo);
            } else {
                ajaxUtil.fail().msg("保存失败，无权限操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据更新
     *
     * @param rosterDataEditVo 数据
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园开学内部数据更新", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/campus/roster/data/update")
    public ResponseEntity<Map<String, Object>> dataUpdate(RosterDataEditVo rosterDataEditVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        rosterDataEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = campusRosterService.dataUpdate(rosterDataEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据删除
     *
     * @param id 发布id
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园开学内部数据删除", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/campus/roster/data/delete")
    public ResponseEntity<Map<String, Object>> dataDelete(@RequestParam("id") String id, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = campusRosterService.dataDelete(users.getUsername(), id);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据查询
     *
     * @param rosterReleaseId 发布id
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园开学内部数据查询", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/campus/roster/data/query-with-roster-release-id")
    public ResponseEntity<Map<String, Object>> dataQuery(@RequestParam("rosterReleaseId") String rosterReleaseId, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = new AjaxUtil<>();
        Users users = SessionUtil.getUserFromOauth(principal);
        Optional<StudentBean> optionalStudentBean = studentService.findByUsername(users.getUsername());
        if (optionalStudentBean.isPresent()) {
            Optional<RosterDataBean> optionalRosterDataBean = campusRosterService.findRosterDataByStudentNumberAndRosterReleaseIdRelation(optionalStudentBean.get().getStudentNumber(), rosterReleaseId);
            if(optionalRosterDataBean.isPresent()){
                RosterDataBean rosterDataBean = optionalRosterDataBean.get();
                rosterDataBean.setBusSection(rosterDataBean.getBusSection().substring(rosterDataBean.getBusSection().indexOf("-") + 1));
                ajaxUtil.success().msg("数据获取成功").put("rosterData", rosterDataBean);
            } else {
                ajaxUtil.fail().msg("未查询到花名册数据");
            }

        } else {
            ajaxUtil.fail().msg("未查询到学生信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
