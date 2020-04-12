package top.zbeboy.zone.web.bean.register.leaver;

import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;

public class LeaverRegisterOptionBean extends LeaverRegisterOption {
    private Byte isChecked;

    public Byte getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Byte isChecked) {
        this.isChecked = isChecked;
    }
}
