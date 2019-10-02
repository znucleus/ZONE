package top.zbeboy.zone.web.vo.data.student;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class StudentEditVo {
    @Size(max = 20, message = "学号20位字符")
    private String studentNumber;
    private String birthday;
    @Size(max = 2, message = "性别2位字符")
    private String sex;
    @Size(max = 200, message = "家庭居住地200位字符")
    private String familyResidence;
    @Min(value = 1, message = "政治面貌不正确")
    private Integer politicalLandscapeId;
    @Min(value = 1, message = "民族不正确")
    private Integer nationId;
    private String dormitoryNumber;
    @Size(max = 10, message = "家长姓名10位字符")
    private String parentName;
    private String parentContactPhone;
    @Size(max = 200, message = "生源地200位字符")
    private String placeOrigin;
    private Integer organizeId;

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
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

    public String getDormitoryNumber() {
        return dormitoryNumber;
    }

    public void setDormitoryNumber(String dormitoryNumber) {
        this.dormitoryNumber = dormitoryNumber;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentContactPhone() {
        return parentContactPhone;
    }

    public void setParentContactPhone(String parentContactPhone) {
        this.parentContactPhone = parentContactPhone;
    }

    public String getPlaceOrigin() {
        return placeOrigin;
    }

    public void setPlaceOrigin(String placeOrigin) {
        this.placeOrigin = placeOrigin;
    }

    public Integer getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(Integer organizeId) {
        this.organizeId = organizeId;
    }
}
