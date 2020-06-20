package top.zbeboy.zone.web.data.college;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.feign.data.CollegeService;
import top.zbeboy.zone.web.bean.data.college.CollegeBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class CollegeViewController {

    @Resource
    private CollegeService collegeService;

    /**
     * 院数据
     *
     * @return 院数据页面
     */
    @GetMapping("/web/menu/data/college")
    public String index() {
        return "web/data/college/college_data::#page-wrapper";
    }

    /**
     * 院数据添加
     *
     * @return 添加页面
     */
    @GetMapping("/web/data/college/add")
    public String add() {
        return "web/data/college/college_add::#page-wrapper";
    }

    /**
     * 院数据编辑
     *
     * @param id       院id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/data/college/edit/{id}")
    public String edit(@PathVariable("id") int id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        College college = collegeService.findById(id);
        if (Objects.nonNull(college.getCollegeId()) && college.getCollegeId() > 0) {
            modelMap.addAttribute("college", college);
            page = "web/data/college/college_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到院数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 应用挂载
     *
     * @return 应用挂载页面
     */
    @GetMapping("/web/data/college/mount/{id}")
    public String mount(@PathVariable("id") int id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        CollegeBean bean = collegeService.findByIdRelation(id);
        if (Objects.nonNull(bean.getCollegeId()) && bean.getCollegeId() > 0) {
            modelMap.addAttribute("college", bean);
            page = "web/data/college/college_mount::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到院数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

}
