package top.zbeboy.zone.web.vo.data.school;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SchoolEditVo {
    @NotNull(message = "学校ID不能为空")
    private int schoolId;
    @NotBlank(message = "学校名不能为空")
    @Size(max = 200, message = "学校名200个字符以内")
    private String schoolName;
    private Byte schoolIsDel;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Byte getSchoolIsDel() {
        return schoolIsDel;
    }

    public void setSchoolIsDel(Byte schoolIsDel) {
        this.schoolIsDel = schoolIsDel;
    }
}
