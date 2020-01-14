package top.zbeboy.zone.web.vo.data.college;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CollegeEditVo {
    @NotNull(message = "院不能为空")
    @Min(value = 1, message = "院不正确")
    private int collegeId;
    @NotBlank(message = "院名不能为空")
    @Size(max = 200, message = "院名200个字符以内")
    private String collegeName;
    private Byte collegeIsDel;
    @NotNull(message = "学校不能为空")
    @Min(value = 1, message = "学校不正确")
    private int schoolId;
    @NotBlank(message = "院地址不能为空")
    @Size(max = 500, message = "院地址500个字符以内")
    private String collegeAddress;
    @NotBlank(message = "院代码不能为空")
    @Size(max = 20, message = "院代码20个字符以内")
    private String collegeCode;

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Byte getCollegeIsDel() {
        return collegeIsDel;
    }

    public void setCollegeIsDel(Byte collegeIsDel) {
        this.collegeIsDel = collegeIsDel;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getCollegeAddress() {
        return collegeAddress;
    }

    public void setCollegeAddress(String collegeAddress) {
        this.collegeAddress = collegeAddress;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }
}
