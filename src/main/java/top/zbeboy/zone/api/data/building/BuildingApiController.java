package top.zbeboy.zone.api.data.building;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.building.BuildingBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.feign.data.BuildingService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.data.building.BuildingSearchVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class BuildingApiController {

    @Resource
    private BuildingService buildingService;

    /**
     * 根据院获取全部有效楼
     *
     * @param buildingSearchVo 院id
     * @return 楼数据
     */
    @ApiLoggingRecord(remark = "楼数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/data/building")
    public ResponseEntity<Map<String, Object>> data(BuildingSearchVo buildingSearchVo,
                                                    Principal principal, HttpServletRequest request) {
        AjaxUtil<BuildingBean> ajaxUtil = AjaxUtil.of();
        List<BuildingBean> buildings = new ArrayList<>();
        Optional<List<BuildingBean>> optionalBuildingBeans = buildingService.search(buildingSearchVo);
        if(optionalBuildingBeans.isPresent()){
            buildings = optionalBuildingBeans.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(buildings);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
