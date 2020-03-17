package top.zbeboy.zone.web.training.release;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrainingReleaseViewController {

    /**
     * 发布主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/release")
    public String index() {
        return "web/training/release/training_release::#page-wrapper";
    }

    /**
     * 添加页面
     *
     * @return 添加页面
     */
    @GetMapping("/web/training/release/add")
    public String add() {
        return "web/training/release/training_release_add::#page-wrapper";
    }
}
