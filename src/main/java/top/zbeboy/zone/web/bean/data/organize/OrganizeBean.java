package top.zbeboy.zone.web.bean.data.organize;

import top.zbeboy.zone.domain.tables.pojos.Organize;

public class OrganizeBean extends Organize {
    private Byte schoolIsDel;
    private Byte collegeIsDel;
    private Byte departmentIsDel;
    private Byte scienceIsDel;
    private Byte gradeIsDel;

    public Byte getSchoolIsDel() {
        return schoolIsDel;
    }

    public void setSchoolIsDel(Byte schoolIsDel) {
        this.schoolIsDel = schoolIsDel;
    }

    public Byte getCollegeIsDel() {
        return collegeIsDel;
    }

    public void setCollegeIsDel(Byte collegeIsDel) {
        this.collegeIsDel = collegeIsDel;
    }

    public Byte getDepartmentIsDel() {
        return departmentIsDel;
    }

    public void setDepartmentIsDel(Byte departmentIsDel) {
        this.departmentIsDel = departmentIsDel;
    }

    public Byte getScienceIsDel() {
        return scienceIsDel;
    }

    public void setScienceIsDel(Byte scienceIsDel) {
        this.scienceIsDel = scienceIsDel;
    }

    public Byte getGradeIsDel() {
        return gradeIsDel;
    }

    public void setGradeIsDel(Byte gradeIsDel) {
        this.gradeIsDel = gradeIsDel;
    }
}
