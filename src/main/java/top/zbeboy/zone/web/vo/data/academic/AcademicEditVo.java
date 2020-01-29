package top.zbeboy.zone.web.vo.data.academic;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AcademicEditVo {
    @NotNull(message = "职称ID不能为空")
    @Min(value = 1,message = "职称ID不正确")
    private int academicTitleId;
    @NotBlank(message = "职称不能为空")
    @Size(max = 30,message = "职称30个字符以内")
    private String academicTitleName;

    public int getAcademicTitleId() {
        return academicTitleId;
    }

    public void setAcademicTitleId(int academicTitleId) {
        this.academicTitleId = academicTitleId;
    }

    public String getAcademicTitleName() {
        return academicTitleName;
    }

    public void setAcademicTitleName(String academicTitleName) {
        this.academicTitleName = academicTitleName;
    }
}
