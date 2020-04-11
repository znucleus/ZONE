package top.zbeboy.zone.web.vo.register.leaver;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LeaverRegisterReleaseEditVo {
    @NotBlank(message = "离校登记发布ID不能为空")
    @Size(max = 64, message = "离校登记发布ID不正确")
    private String leaverRegisterReleaseId;
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题100个字符以内")
    private String title;
    private String optionContent;
    @NotNull(message = "数据域不能为空")
    private int dataScope;
    @NotBlank(message = "数据不能为空")
    private String dataId;

    public String getLeaverRegisterReleaseId() {
        return leaverRegisterReleaseId;
    }

    public void setLeaverRegisterReleaseId(String leaverRegisterReleaseId) {
        this.leaverRegisterReleaseId = leaverRegisterReleaseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

    public int getDataScope() {
        return dataScope;
    }

    public void setDataScope(int dataScope) {
        this.dataScope = dataScope;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
