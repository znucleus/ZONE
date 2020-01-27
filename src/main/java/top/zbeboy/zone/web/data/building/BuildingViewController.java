package top.zbeboy.zone.web.data.building;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuildingViewController {

    /**
     * 楼数据
     *
     * @return 楼数据页面
     */
    @GetMapping("/web/menu/data/building")
    public String index() {
        return "web/data/building/building_data::#page-wrapper";
    }
}
