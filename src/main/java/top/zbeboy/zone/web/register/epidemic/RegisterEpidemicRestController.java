package top.zbeboy.zone.web.register.epidemic;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.service.register.EpidemicRegisterReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.register.epidemic.EpidemicRegisterReleaseBean;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RegisterEpidemicRestController {

    @Resource
    private EpidemicRegisterReleaseService epidemicRegisterReleaseService;

    @Resource
    private RegisterConditionCommon registerConditionCommon;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/register/epidemic/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<EpidemicRegisterReleaseBean> ajaxUtil = AjaxUtil.of();
        List<EpidemicRegisterReleaseBean> beans = new ArrayList<>();
        Result<Record> records = epidemicRegisterReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(EpidemicRegisterReleaseBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(registerConditionCommon.epidemicOperator())));
            beans.forEach(bean -> bean.setCanReview(BooleanUtil.toByte(registerConditionCommon.epidemicReview())));
        }
        simplePaginationUtil.setTotalSize(epidemicRegisterReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
