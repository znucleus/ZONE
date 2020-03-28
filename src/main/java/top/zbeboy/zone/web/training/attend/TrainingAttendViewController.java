package top.zbeboy.zone.web.training.attend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrainingAttendViewController {

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/attend")
    public String index() {
        return "web/training/attend/training_attend::#page-wrapper";
    }
}
