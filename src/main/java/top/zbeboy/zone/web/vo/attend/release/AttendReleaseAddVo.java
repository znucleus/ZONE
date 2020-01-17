package top.zbeboy.zone.web.vo.attend.release;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AttendReleaseAddVo {
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题100个字符以内")
    private String title;
    @NotBlank(message = "签到开始时间不能为空")
    private String attendStartTime;
    @NotBlank(message = "签到结束时间不能为空")
    private String attendEndTime;
    private Byte isAuto;
    @NotBlank(message = "生成截止日期不能为空")
    private String expireDate;
    @NotNull(message = "班级ID不能为空")
    @Min(value = 1, message = "班级不正确")
    private int organizeId;

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

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(int organizeId) {
        this.organizeId = organizeId;
    }
}
