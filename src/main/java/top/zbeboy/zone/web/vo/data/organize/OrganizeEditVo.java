package top.zbeboy.zone.web.vo.data.organize;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrganizeEditVo {
    @NotNull(message = "班级不能为空")
    @Min(value = 1, message = "班级不正确")
    private int organizeId;
    @NotBlank(message = "班级名不能为空")
    @Size(max = 200, message = "班级名200个字符以内")
    private String organizeName;
    private Byte organizeIsDel;
    @NotNull(message = "专业不能为空")
    @Min(value = 1, message = "专业不正确")
    private int scienceId;
    @NotNull(message = "年级不能为空")
    private int gradeId;
    private String staff;

    public int getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(int organizeId) {
        this.organizeId = organizeId;
    }

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

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }
}
