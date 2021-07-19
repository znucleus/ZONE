package top.zbeboy.zone.web.data.school;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.domain.tables.pojos.School;
import top.zbeboy.zbase.feign.data.SchoolService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class SchoolViewController {

    @Resource
    private SchoolService schoolService;

    /**
     * 学校数据
     *
     * @return 学校数据页面
     */
    @GetMapping("/web/menu/data/school")
    public String index() {
        return "web/data/school/school_data::#page-wrapper";
    }

    /**
     * 学校数据添加
     *
     * @return 添加页面
     */
    @GetMapping("/web/data/school/add")
    public String add() {
        return "web/data/school/school_add::#page-wrapper";
    }

    /**
     * 学校数据编辑
     *
     * @param id       学校id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/data/school/edit/{id}")
    public String edit(@PathVariable("id") int id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<School> result = schoolService.findById(id);
        if (result.isPresent()) {
            modelMap.addAttribute("school", result.get());
            page = "web/data/school/school_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到学校数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
