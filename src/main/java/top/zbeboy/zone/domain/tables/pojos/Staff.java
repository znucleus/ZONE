/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;
import java.sql.Date;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Staff implements Serializable {

    private static final long serialVersionUID = -1865141397;

    private Integer staffId;
    private String  staffNumber;
    private Date    birthday;
    private String  sex;
    private String  familyResidence;
    private Integer politicalLandscapeId;
    private Integer nationId;
    private String  post;
    private Integer academicTitleId;
    private Integer departmentId;
    private String  username;

    public Staff() {}

    public Staff(Staff value) {
        this.staffId = value.staffId;
        this.staffNumber = value.staffNumber;
        this.birthday = value.birthday;
        this.sex = value.sex;
        this.familyResidence = value.familyResidence;
        this.politicalLandscapeId = value.politicalLandscapeId;
        this.nationId = value.nationId;
        this.post = value.post;
        this.academicTitleId = value.academicTitleId;
        this.departmentId = value.departmentId;
        this.username = value.username;
    }

    public Staff(
        Integer staffId,
        String  staffNumber,
        Date    birthday,
        String  sex,
        String  familyResidence,
        Integer politicalLandscapeId,
        Integer nationId,
        String  post,
        Integer academicTitleId,
        Integer departmentId,
        String  username
    ) {
        this.staffId = staffId;
        this.staffNumber = staffNumber;
        this.birthday = birthday;
        this.sex = sex;
        this.familyResidence = familyResidence;
        this.politicalLandscapeId = politicalLandscapeId;
        this.nationId = nationId;
        this.post = post;
        this.academicTitleId = academicTitleId;
        this.departmentId = departmentId;
        this.username = username;
    }

    public Integer getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    @NotNull
    @Size(max = 20)
    public String getStaffNumber() {
        return this.staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Size(max = 2)
    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Size(max = 200)
    public String getFamilyResidence() {
        return this.familyResidence;
    }

    public void setFamilyResidence(String familyResidence) {
        this.familyResidence = familyResidence;
    }

    public Integer getPoliticalLandscapeId() {
        return this.politicalLandscapeId;
    }

    public void setPoliticalLandscapeId(Integer politicalLandscapeId) {
        this.politicalLandscapeId = politicalLandscapeId;
    }

    public Integer getNationId() {
        return this.nationId;
    }

    public void setNationId(Integer nationId) {
        this.nationId = nationId;
    }

    @Size(max = 50)
    public String getPost() {
        return this.post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Integer getAcademicTitleId() {
        return this.academicTitleId;
    }

    public void setAcademicTitleId(Integer academicTitleId) {
        this.academicTitleId = academicTitleId;
    }

    @NotNull
    public Integer getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Staff (");

        sb.append(staffId);
        sb.append(", ").append(staffNumber);
        sb.append(", ").append(birthday);
        sb.append(", ").append(sex);
        sb.append(", ").append(familyResidence);
        sb.append(", ").append(politicalLandscapeId);
        sb.append(", ").append(nationId);
        sb.append(", ").append(post);
        sb.append(", ").append(academicTitleId);
        sb.append(", ").append(departmentId);
        sb.append(", ").append(username);

        sb.append(")");
        return sb.toString();
    }
}
