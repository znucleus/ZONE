package top.zbeboy.zone.web.vo.register.epidemic;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EpidemicRegisterReleaseAddVo {
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题100个字符以内")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
