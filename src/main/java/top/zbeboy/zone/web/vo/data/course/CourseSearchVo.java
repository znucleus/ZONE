package top.zbeboy.zone.web.vo.data.course;

public class CourseSearchVo {
    private Byte schoolYear;
    private Byte term;
    private Integer collegeId;

    public Byte getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Byte schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Byte getTerm() {
        return term;
    }

    public void setTerm(Byte term) {
        this.term = term;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }
}
