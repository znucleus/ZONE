package top.zbeboy.zone.web.system.tip;

import org.springframework.ui.ModelMap;

public class SystemInlineTipConfig {
    private boolean successTip;
    private boolean infoTip;
    private boolean warningTip;
    private boolean dangerTip;

    private String title;
    private String subTitle;

    public SystemInlineTipConfig() {
    }

    public SystemInlineTipConfig(String title, String subTitle) {
        this.successTip = true;
        this.title = title;
        this.subTitle = subTitle;
    }

    public void buildSuccessTip(String title, String subTitle) {
        this.successTip = true;
        this.title = title;
        this.subTitle = subTitle;
    }

    public void buildInfoTip(String title, String subTitle) {
        this.infoTip = true;
        this.title = title;
        this.subTitle = subTitle;
    }

    public void buildWarningTip(String title, String subTitle) {
        this.warningTip = true;
        this.title = title;
        this.subTitle = subTitle;
    }

    public void buildDangerTip(String title, String subTitle) {
        this.dangerTip = true;
        this.title = title;
        this.subTitle = subTitle;
    }

    /**
     * 最后的数据合并
     *
     * @param modelMap 页面数据
     */
    public void dataMerging(ModelMap modelMap) {
        modelMap.addAttribute("successTip", successTip);
        modelMap.addAttribute("infoTip", infoTip);
        modelMap.addAttribute("warningTip", warningTip);
        modelMap.addAttribute("dangerTip", dangerTip);
        modelMap.addAttribute("title", title);
        modelMap.addAttribute("subTitle", subTitle);
    }
}
