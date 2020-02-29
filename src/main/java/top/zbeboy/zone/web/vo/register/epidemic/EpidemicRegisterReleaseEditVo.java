package top.zbeboy.zone.web.vo.register.epidemic;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EpidemicRegisterReleaseEditVo {
    @NotBlank(message = "发布ID不能为空")
    @Size(max = 64, message = "发布ID不正确")
    private String epidemicRegisterReleaseId;
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题100个字符以内")
    private String title;

    public String getEpidemicRegisterReleaseId() {
        return epidemicRegisterReleaseId;
    }

    public void setEpidemicRegisterReleaseId(String epidemicRegisterReleaseId) {
        this.epidemicRegisterReleaseId = epidemicRegisterReleaseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
