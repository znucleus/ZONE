package top.zbeboy.zone.web.notify;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.service.notify.SystemNotifyService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.notify.SystemNotifyBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class SystemNotifyViewController {

    @Resource
    private SystemNotifyService systemNotifyService;

    /**
     * 通知
     *
     * @return 页面
     */
    @GetMapping("/web/menu/system/notify")
    public String index() {
        return "web/system/notify/system_notify::#page-wrapper";
    }

    /**
     * 通知添加
     *
     * @return 添加页面
     */
    @GetMapping("/web/system/notify/add")
    public String add() {
        return "web/system/notify/system_notify_add::#page-wrapper";
    }

    /**
     * 通知编辑
     *
     * @param id       通知id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/system/notify/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = systemNotifyService.findByIdRelation(id);
        if (record.isPresent()) {
            SystemNotifyBean bean = record.get().into(SystemNotifyBean.class);
            bean.setValidDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getValidDate()));
            bean.setExpireDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getExpireDate()));
            modelMap.addAttribute("systemNotify", bean);
            page = "web/system/notify/system_notify_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到通知数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
