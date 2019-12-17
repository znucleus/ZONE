package top.zbeboy.zone.web.bean.platform.role;

import top.zbeboy.zone.domain.tables.pojos.Role;

public class RoleBean extends Role {
    private String collegeName;
    private String schoolName;
    private int collegeId;

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }
}
