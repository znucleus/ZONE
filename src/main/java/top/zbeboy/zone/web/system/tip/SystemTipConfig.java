package top.zbeboy.zone.web.system.tip;

import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

public class SystemTipConfig {

    private boolean successTip;
    private boolean infoTip;
    private boolean warningTip;
    private boolean dangerTip;

    private String title;
    private String subTitle;

    private List<Button> buttons = new ArrayList<>();

    public SystemTipConfig() {
    }

    public SystemTipConfig(String title, String subTitle) {
        this.successTip = true;
        this.title = title;
        this.subTitle = subTitle;
    }

    public SystemTipConfig(boolean successTip, boolean infoTip, boolean warningTip, boolean dangerTip, String title, String subTitle) {
        this.successTip = successTip;
        this.infoTip = infoTip;
        this.warningTip = warningTip;
        this.dangerTip = dangerTip;
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
     * 添加登录按钮
     */
    public void addLoginButton() {
        buttons.add(new Button(ButtonClass.OUTLINE_PRIMARY, "立即登录", ButtonLink.WEB_LOGIN));
    }

    /**
     * 添加首页按钮
     */
    public void addHomeButton() {
        buttons.add(new Button(ButtonClass.OUTLINE_SECONDARY, "返回首页", ButtonLink.WEB_HOME));
    }

    /**
     * 添加自定义按钮
     *
     * @param btnClass 按钮class
     * @param btnText  按钮text
     * @param btnLink  按钮链接
     */
    public void addCustomButton(String btnClass, String btnText, String btnLink) {
        buttons.add(new Button(btnClass, btnText, btnLink));
    }

    public class Button {
        private String btnClass;
        private String btnText;
        private String btnLink;

        public Button(String btnClass, String btnText, String btnLink) {
            this.btnClass = btnClass;
            this.btnText = btnText;
            this.btnLink = btnLink;
        }

        public String getBtnClass() {
            return btnClass;
        }

        public void setBtnClass(String btnClass) {
            this.btnClass = btnClass;
        }

        public String getBtnText() {
            return btnText;
        }

        public void setBtnText(String btnText) {
            this.btnText = btnText;
        }

        public String getBtnLink() {
            return btnLink;
        }

        public void setBtnLink(String btnLink) {
            this.btnLink = btnLink;
        }
    }

    public class ButtonClass {
        public static final String OUTLINE_PRIMARY = "btn btn-outline-primary";
        public static final String OUTLINE_SECONDARY = "btn btn-outline-secondary";
        public static final String OUTLINE_WARNING = "btn btn-outline-warning";
    }

    public class ButtonLink {
        public static final String WEB_HOME = "/";
        public static final String WEB_LOGIN = "/login";
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
        modelMap.addAttribute("buttons", buttons);
    }

    public boolean isSuccessTip() {
        return successTip;
    }

    public void setSuccessTip(boolean successTip) {
        this.successTip = successTip;
    }

    public boolean isInfoTip() {
        return infoTip;
    }

    public void setInfoTip(boolean infoTip) {
        this.infoTip = infoTip;
    }

    public boolean isWarningTip() {
        return warningTip;
    }

    public void setWarningTip(boolean warningTip) {
        this.warningTip = warningTip;
    }

    public boolean isDangerTip() {
        return dangerTip;
    }

    public void setDangerTip(boolean dangerTip) {
        this.dangerTip = dangerTip;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
