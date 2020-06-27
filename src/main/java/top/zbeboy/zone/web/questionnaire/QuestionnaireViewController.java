package top.zbeboy.zone.web.questionnaire;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionnaireViewController {

    /**
     * 外链添加页
     *
     * @return 外链添加页
     */
    @GetMapping("/anyone/questionnaire/add")
    public String anyoneQuestionnaireAdd() {
        return "web/questionnaire/questionnaire_outer_add";
    }
}
