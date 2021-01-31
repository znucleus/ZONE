package top.zbeboy.zone.web.data.school;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.School;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.SchoolService;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.school.SchoolAddVo;
import top.zbeboy.zbase.vo.data.school.SchoolEditVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SchoolRestController {

    @Resource
    private SchoolService schoolService;

    /**
     * 获取全部有效学校
     *
     * @return 学校数据
     */
    @ApiLoggingRecord(remark = "学校数据", channel = Workbook.channel.WEB)
    @GetMapping("/anyone/data/school")
    public ResponseEntity<Map<String, Object>> anyoneData(HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        Optional<List<School>> result = schoolService.findNormal();
        result.ifPresent(schools -> schools.forEach(school -> select2Data.add(school.getSchoolId().toString(), school.getSchoolName())));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/school/paging")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        HashMap<String, String> paramMap = RequestUtil.addValue(request, RequestUtil.commonUseKey.username.name(), users.getUsername());
        Optional<DataTablesUtil> result = schoolService.data(paramMap);
        return new ResponseEntity<>(result.orElseGet(() -> new DataTablesUtil(request)), HttpStatus.OK);
    }

    /**
     * 保存时检验学校名是否重复
     *
     * @param schoolName 学校名
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/school/check-add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("schoolName") String schoolName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolService.checkAddName(schoolName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存学校信息
     *
     * @param schoolAddVo 学校
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/school/save")
    public ResponseEntity<Map<String, Object>> save(SchoolAddVo schoolAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolService.save(schoolAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时学校名重复
     *
     * @param id         学校id
     * @param schoolName 学校名
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/school/check-edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("schoolId") int id,
                                                             @RequestParam("schoolName") String schoolName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolService.checkEditName(id, schoolName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存学校更改
     *
     * @param schoolEditVo 学校
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/school/update")
    public ResponseEntity<Map<String, Object>> update(SchoolEditVo schoolEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolService.update(schoolEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更改学校状态
     *
     * @param schoolIds 学校ids
     * @param isDel     is_del
     * @return true注销成功
     */
    @PostMapping("/web/data/school/update-status")
    public ResponseEntity<Map<String, Object>> status(String schoolIds, Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = schoolService.status(schoolIds, isDel);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
