package top.zbeboy.zone.web.training.document;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrainingDocumentViewController {

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/document")
    public String index() {
        return "web/training/document/training_document::#page-wrapper";
    }
}
