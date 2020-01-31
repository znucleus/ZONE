package top.zbeboy.zone.web.vo.system.config;

import javax.validation.constraints.NotBlank;

public class SystemConfigureEditVo {
    @NotBlank(message = "KEY不能为空")
    private String dataKey;
    private String dataValue;

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
}
