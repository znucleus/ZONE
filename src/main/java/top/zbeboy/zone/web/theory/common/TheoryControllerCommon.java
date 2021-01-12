package top.zbeboy.zone.web.theory.common;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.bean.theory.release.TheoryReleaseBean;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.service.theory.TheoryReleaseService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class TheoryControllerCommon {

    @Resource
    private TheoryReleaseService theoryReleaseService;

    public AjaxUtil<TheoryReleaseBean> theoryData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<TheoryReleaseBean> ajaxUtil = AjaxUtil.of();
        List<TheoryReleaseBean> beans = new ArrayList<>();
        Result<Record> records = theoryReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(TheoryReleaseBean.class);
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
        }
        simplePaginationUtil.setTotalSize(theoryReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return ajaxUtil;
    }
}
