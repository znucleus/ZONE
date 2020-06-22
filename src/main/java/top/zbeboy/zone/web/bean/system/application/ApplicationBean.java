package top.zbeboy.zone.web.bean.system.application;

import top.zbeboy.zbase.domain.tables.pojos.Application;

public class ApplicationBean extends Application {
    private String applicationPidName;

    public String getApplicationPidName() {
        return applicationPidName;
    }

    public void setApplicationPidName(String applicationPidName) {
        this.applicationPidName = applicationPidName;
    }
}
