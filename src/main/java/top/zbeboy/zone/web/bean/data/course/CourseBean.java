package top.zbeboy.zone.web.bean.data.course;

import top.zbeboy.zone.domain.tables.pojos.Course;

public class CourseBean extends Course {
    private Byte schoolIsDel;
    private Byte collegeIsDel;
    private String collegeName;
    private String schoolName;
    private int schoolId;

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

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}