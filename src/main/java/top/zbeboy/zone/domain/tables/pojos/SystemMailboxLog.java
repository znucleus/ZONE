/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;
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
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SystemMailboxLog implements Serializable {

    private static final long serialVersionUID = -1978766942;

    private String    logId;
    private Timestamp sendTime;
    private String    acceptMail;
    private String    sendCondition;

    public SystemMailboxLog() {}

    public SystemMailboxLog(SystemMailboxLog value) {
        this.logId = value.logId;
        this.sendTime = value.sendTime;
        this.acceptMail = value.acceptMail;
        this.sendCondition = value.sendCondition;
    }

    public SystemMailboxLog(
        String    logId,
        Timestamp sendTime,
        String    acceptMail,
        String    sendCondition
    ) {
        this.logId = logId;
        this.sendTime = sendTime;
        this.acceptMail = acceptMail;
        this.sendCondition = sendCondition;
    }

    @NotNull
    @Size(max = 64)
    public String getLogId() {
        return this.logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Timestamp getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    @Size(max = 200)
    public String getAcceptMail() {
        return this.acceptMail;
    }

    public void setAcceptMail(String acceptMail) {
        this.acceptMail = acceptMail;
    }

    @Size(max = 500)
    public String getSendCondition() {
        return this.sendCondition;
    }

    public void setSendCondition(String sendCondition) {
        this.sendCondition = sendCondition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SystemMailboxLog (");

        sb.append(logId);
        sb.append(", ").append(sendTime);
        sb.append(", ").append(acceptMail);
        sb.append(", ").append(sendCondition);

        sb.append(")");
        return sb.toString();
    }
}
