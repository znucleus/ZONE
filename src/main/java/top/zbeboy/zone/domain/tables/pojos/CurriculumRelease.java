/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

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
public class CurriculumRelease implements Serializable {

    private static final long serialVersionUID = -876100075;

    private String    curriculumReleaseId;
    private String    title;
    private Integer   startYear;
    private Integer   endYear;
    private Byte      term;
    private Integer   collegeId;
    private Timestamp releaseTime;
    private String    releaseUsername;
    private String    releasePublisher;
    private Byte      weeks;
    private Date      startDate;
    private Date      endDate;

    public CurriculumRelease() {}

    public CurriculumRelease(CurriculumRelease value) {
        this.curriculumReleaseId = value.curriculumReleaseId;
        this.title = value.title;
        this.startYear = value.startYear;
        this.endYear = value.endYear;
        this.term = value.term;
        this.collegeId = value.collegeId;
        this.releaseTime = value.releaseTime;
        this.releaseUsername = value.releaseUsername;
        this.releasePublisher = value.releasePublisher;
        this.weeks = value.weeks;
        this.startDate = value.startDate;
        this.endDate = value.endDate;
    }

    public CurriculumRelease(
        String    curriculumReleaseId,
        String    title,
        Integer   startYear,
        Integer   endYear,
        Byte      term,
        Integer   collegeId,
        Timestamp releaseTime,
        String    releaseUsername,
        String    releasePublisher,
        Byte      weeks,
        Date      startDate,
        Date      endDate
    ) {
        this.curriculumReleaseId = curriculumReleaseId;
        this.title = title;
        this.startYear = startYear;
        this.endYear = endYear;
        this.term = term;
        this.collegeId = collegeId;
        this.releaseTime = releaseTime;
        this.releaseUsername = releaseUsername;
        this.releasePublisher = releasePublisher;
        this.weeks = weeks;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @NotNull
    @Size(max = 64)
    public String getCurriculumReleaseId() {
        return this.curriculumReleaseId;
    }

    public void setCurriculumReleaseId(String curriculumReleaseId) {
        this.curriculumReleaseId = curriculumReleaseId;
    }

    @NotNull
    @Size(max = 200)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    public Integer getStartYear() {
        return this.startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    @NotNull
    public Integer getEndYear() {
        return this.endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    @NotNull
    public Byte getTerm() {
        return this.term;
    }

    public void setTerm(Byte term) {
        this.term = term;
    }

    @NotNull
    public Integer getCollegeId() {
        return this.collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    @NotNull
    public Timestamp getReleaseTime() {
        return this.releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    @NotNull
    @Size(max = 64)
    public String getReleaseUsername() {
        return this.releaseUsername;
    }

    public void setReleaseUsername(String releaseUsername) {
        this.releaseUsername = releaseUsername;
    }

    @NotNull
    @Size(max = 30)
    public String getReleasePublisher() {
        return this.releasePublisher;
    }

    public void setReleasePublisher(String releasePublisher) {
        this.releasePublisher = releasePublisher;
    }

    @NotNull
    public Byte getWeeks() {
        return this.weeks;
    }

    public void setWeeks(Byte weeks) {
        this.weeks = weeks;
    }

    @NotNull
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @NotNull
    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CurriculumRelease (");

        sb.append(curriculumReleaseId);
        sb.append(", ").append(title);
        sb.append(", ").append(startYear);
        sb.append(", ").append(endYear);
        sb.append(", ").append(term);
        sb.append(", ").append(collegeId);
        sb.append(", ").append(releaseTime);
        sb.append(", ").append(releaseUsername);
        sb.append(", ").append(releasePublisher);
        sb.append(", ").append(weeks);
        sb.append(", ").append(startDate);
        sb.append(", ").append(endDate);

        sb.append(")");
        return sb.toString();
    }
}
