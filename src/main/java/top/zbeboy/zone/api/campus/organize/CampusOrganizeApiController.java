package top.zbeboy.zone.api.campus.organize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Organize;
import top.zbeboy.zbase.feign.data.OrganizeService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@RestController
public class CampusOrganizeApiController {

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private OrganizeService organizeService;

    /**
     * 班级数据
     *
     * @param principal 用户
     * @return 数据
     */
    @ApiLoggingRecord(remark = "校园班级数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/campus/organize/{organizeId}")
    public ResponseEntity<Map<String, Object>> organize(@PathVariable("organizeId") int organizeId, Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Optional<Organize> optionalOrganize = organizeService.findById(organizeId);
        if (optionalOrganize.isPresent()) {
            String clientId = ((OAuth2Authentication) principal).getOAuth2Request().getClientId();
            Map<String, Object> outPut = new HashMap<>();
            Organize organize = optionalOrganize.get();
            outPut.put("organizeName", organize.getOrganizeName());
            // 班主任
            Optional<StaffBean> optionalStaffBean = staffService.findByIdRelation(organize.getStaffId());
            if (optionalStaffBean.isPresent()) {
                StaffBean staffBean = optionalStaffBean.get();
                outPut.put("realName", staffBean.getRealName());
                if (StringUtils.isNotBlank(clientId) && Workbook.advancedApp().contains(clientId)) {
                    outPut.put("mobile", staffBean.getMobile());
                    outPut.put("staffId", staffBean.getStaffId());
                }
            }
            // 班级学生
            Optional<List<StudentBean>> optionalStudentBeans = studentService.findNormalByOrganizeId(organizeId);
            if (optionalStudentBeans.isPresent()) {
                List<StudentBean> studentBeans = optionalStudentBeans.get();
                List<StudentBean> outList = new ArrayList<>();
                for (StudentBean bean : studentBeans) {
                    StudentBean out = new StudentBean();
                    out.setRealName(bean.getRealName());
                    out.setStudentNumber(bean.getStudentNumber());
                    if (StringUtils.isNotBlank(clientId) && Workbook.advancedApp().contains(clientId)) {
                        out.setMobile(bean.getMobile());
                        out.setEmail(bean.getEmail());
                    }
                    outList.add(out);
                }

                outPut.put("students", outList);
            }
            ajaxUtil.success().msg("获取数据成功").map(outPut);
        } else {
            ajaxUtil.fail().msg("根据班级ID未查询到班级信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
