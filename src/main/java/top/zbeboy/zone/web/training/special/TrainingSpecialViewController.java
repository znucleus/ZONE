package top.zbeboy.zone.web.training.special;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrainingSpecialViewController {

    /**
     * 专题主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/special")
    public String index() {
        return "web/training/special/training_special::#page-wrapper";
    }
}
