package top.zbeboy.zone.web.training.users;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrainingUsersViewController {

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/users")
    public String index() {
        return "web/training/users/training_users::#page-wrapper";
    }
}
