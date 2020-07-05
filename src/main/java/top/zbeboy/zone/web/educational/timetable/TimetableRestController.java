package top.zbeboy.zone.web.educational.timetable;

import com.google.common.collect.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.elastic.TimetableUniqueElastic;
import top.zbeboy.zbase.feign.city.TimetableService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@RestController
public class TimetableRestController {

    @Resource
    private TimetableService timetableService;

    /**
     * 标识数据
     *
     * @return 数据
     */
    @GetMapping("/web/educational/timetable/uniques")
    public ResponseEntity<Map<String, Object>> uniques() {
        AjaxUtil<TimetableUniqueElastic> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().msg("获取数据成功").list(timetableService.uniques());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
