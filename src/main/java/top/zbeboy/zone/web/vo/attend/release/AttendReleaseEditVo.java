package top.zbeboy.zone.web.vo.attend.release;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AttendReleaseEditVo {
    @NotNull(message = "签到发布子表ID不能为空")
    @Min(value = 1, message = "签到发布子表ID不正确")
    private int   attendReleaseSubId;
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题100个字符以内")
    private String title;
    @NotBlank(message = "签到开始时间不能为空")
    private String attendStartTime;
    @NotBlank(message = "签到结束时间不能为空")
    private String attendEndTime;
    @NotNull(message = "是否自动生成不能为空")
    private Byte isAuto;
    private String validDate;
    private String expireDate;

    public int getAttendReleaseSubId() {
        return attendReleaseSubId;
    }

    public void setAttendReleaseSubId(int attendReleaseSubId) {
        this.attendReleaseSubId = attendReleaseSubId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttendStartTime() {
        return attendStartTime;
    }

    public void setAttendStartTime(String attendStartTime) {
        this.attendStartTime = attendStartTime;
    }

    public String getAttendEndTime() {
        return attendEndTime;
    }

    public void setAttendEndTime(String attendEndTime) {
        this.attendEndTime = attendEndTime;
    }

    public Byte getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Byte isAuto) {
        this.isAuto = isAuto;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
