package top.zbeboy.zone.web.educational.examination;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.educational.examination.EducationalExaminationService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;

@Controller
public class ExaminationViewController {

    @Resource
    private EducationalExaminationService educationalExaminationService;

    /**
     * 考试通知
     *
     * @return 考试通知页面
     */
    @GetMapping("/web/menu/educational/examination")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.addAttribute("canRelease", educationalExaminationService.canRelease(users.getUsername()));
        return "web/educational/examination/examination_release::#page-wrapper";
    }
}
