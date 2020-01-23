package top.zbeboy.zone.web.vo.data.organize;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrganizeAddVo {
    @NotBlank(message = "班级名不能为空")
    @Size(max = 200, message = "班级名200个字符以内")
    private String organizeName;
    private Byte organizeIsDel;
    @NotNull(message = "专业不能为空")
    @Min(value = 1, message = "专业不正确")
    private int scienceId;
    @NotNull(message = "年级不能为空")
    private int grade;

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public Byte getOrganizeIsDel() {
        return organizeIsDel;
    }

    public void setOrganizeIsDel(Byte organizeIsDel) {
        this.organizeIsDel = organizeIsDel;
    }

    public int getScienceId() {
        return scienceId;
    }

    public void setScienceId(int scienceId) {
        this.scienceId = scienceId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
