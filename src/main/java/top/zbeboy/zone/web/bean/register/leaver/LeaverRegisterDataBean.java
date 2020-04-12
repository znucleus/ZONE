package top.zbeboy.zone.web.bean.register.leaver;

import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData;

import java.util.List;

public class LeaverRegisterDataBean extends LeaverRegisterData {
    private String realName;
    private String studentNumber;
    private List<LeaverRegisterOptionBean> leaverRegisterOptions;
    private String registerDateStr;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public List<LeaverRegisterOptionBean> getLeaverRegisterOptions() {
        return leaverRegisterOptions;
    }

    public void setLeaverRegisterOptions(List<LeaverRegisterOptionBean> leaverRegisterOptions) {
        this.leaverRegisterOptions = leaverRegisterOptions;
    }

    public String getRegisterDateStr() {
        return registerDateStr;
    }

    public void setRegisterDateStr(String registerDateStr) {
        this.registerDateStr = registerDateStr;
    }
}
