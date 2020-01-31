package top.zbeboy.zone.web.vo.attend.data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AttendDataAddVo {
    @NotNull(message = "签到发布子表ID不能为空")
    @Min(value = 1, message = "签到发布子表ID不正确")
    private int attendReleaseSubId;
    private String location;
    private String address;

    public int getAttendReleaseSubId() {
        return attendReleaseSubId;
    }

    public void setAttendReleaseSubId(int attendReleaseSubId) {
        this.attendReleaseSubId = attendReleaseSubId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
