package top.zbeboy.zone.web.vo.data.staff;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StaffEditVo {
    @Size(max = 20, message = "工号20位字符")
    private String staffNumber;
    private String birthday;
    @Size(max = 2, message = "性别2位字符")
    private String sex;
    @Size(max = 200, message = "家庭居住地200位字符")
    private String familyResidence;
    @Min(value = 1, message = "政治面貌不正确")
    private Integer politicalLandscapeId;
    @Min(value = 1, message = "民族不正确")
    private Integer nationId;
    @Size(max = 50, message = "职务50位字符")
    private String post;
    @Min(value = 1, message = "职称不正确")
    private Integer academicTitleId;
    private Integer departmentId;
    @NotBlank(message = "当前用户账号不能为空")
    @Size(max = 64, message = "当前用户账号不正确")
    private String username;

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFamilyResidence() {
        return familyResidence;
    }

    public void setFamilyResidence(String familyResidence) {
        this.familyResidence = familyResidence;
    }

    public Integer getPoliticalLandscapeId() {
        return politicalLandscapeId;
    }

    public void setPoliticalLandscapeId(Integer politicalLandscapeId) {
        this.politicalLandscapeId = politicalLandscapeId;
    }

    public Integer getNationId() {
        return nationId;
    }

    public void setNationId(Integer nationId) {
        this.nationId = nationId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Integer getAcademicTitleId() {
        return academicTitleId;
    }

    public void setAcademicTitleId(Integer academicTitleId) {
        this.academicTitleId = academicTitleId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
