package top.zbeboy.zone.web.vo.data.academic;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AcademicAddVo {
    @NotBlank(message = "职称不能为空")
    @Size(max = 30, message = "职称30个字符以内")
    private String academicTitleName;

    public String getAcademicTitleName() {
        return academicTitleName;
    }

    public void setAcademicTitleName(String academicTitleName) {
        this.academicTitleName = academicTitleName;
    }
}
