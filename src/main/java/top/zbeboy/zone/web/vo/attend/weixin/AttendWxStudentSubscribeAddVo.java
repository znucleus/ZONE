package top.zbeboy.zone.web.vo.attend.weixin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AttendWxStudentSubscribeAddVo {
    @NotBlank(message = "模板ID不能为空")
    @Size(max = 64, message = "模板ID64个字符以内")
    private String templateId;
    @NotBlank(message = "签到发布主表ID不能为空")
    @Size(max = 64, message = "签到发布主表ID不正确")
    private String attendReleaseId;
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

    public String getAttendReleaseId() {
        return attendReleaseId;
    }

    public void setAttendReleaseId(String attendReleaseId) {
        this.attendReleaseId = attendReleaseId;
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
