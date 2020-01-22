package top.zbeboy.zone.web.vo.data.science;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ScienceEditVo {
    @NotNull(message = "专业不能为空")
    @Min(value = 1, message = "专业不正确")
    private int scienceId;
    @NotBlank(message = "专业名不能为空")
    @Size(max = 200, message = "专业名200个字符以内")
    private String scienceName;
    private Byte scienceIsDel;
    @NotNull(message = "系不能为空")
    @Min(value = 1, message = "系不正确")
    private int departmentId;
    @NotBlank(message = "专业代码不能为空")
    @Size(max = 20, message = "专业代码20个字符以内")
    private String scienceCode;

    public int getScienceId() {
        return scienceId;
    }

    public void setScienceId(int scienceId) {
        this.scienceId = scienceId;
    }

    public String getScienceName() {
        return scienceName;
    }

    public void setScienceName(String scienceName) {
        this.scienceName = scienceName;
    }

    public Byte getScienceIsDel() {
        return scienceIsDel;
    }

    public void setScienceIsDel(Byte scienceIsDel) {
        this.scienceIsDel = scienceIsDel;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getScienceCode() {
        return scienceCode;
    }

    public void setScienceCode(String scienceCode) {
        this.scienceCode = scienceCode;
    }
}
