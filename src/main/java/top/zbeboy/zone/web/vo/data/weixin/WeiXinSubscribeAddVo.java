package top.zbeboy.zone.web.vo.data.weixin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class WeiXinSubscribeAddVo {
    @NotBlank(message = "模板ID不能为空")
    @Size(max = 64, message = "模板ID64个字符以内")
    private String templateId;
    @NotBlank(message = "类型不能为空")
    @Size(max = 10, message = "类型10个字符以内")
    private String type;
    private String page;
    private String data;
    private String miniProgramState;
    private String lang;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMiniProgramState() {
        return miniProgramState;
    }

    public void setMiniProgramState(String miniProgramState) {
        this.miniProgramState = miniProgramState;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
