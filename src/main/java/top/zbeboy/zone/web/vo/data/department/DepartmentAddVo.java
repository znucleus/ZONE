package top.zbeboy.zone.web.vo.data.department;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DepartmentAddVo {
    @NotBlank(message = "系名不能为空")
    @Size(max = 200,message = "系名200个字符以内")
    private String departmentName;
    private Byte departmentIsDel;
    @NotNull(message = "院不能为空")
    @Min(value = 1, message = "院不正确")
    private int collegeId;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Byte getDepartmentIsDel() {
        return departmentIsDel;
    }

    public void setDepartmentIsDel(Byte departmentIsDel) {
        this.departmentIsDel = departmentIsDel;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }
}
