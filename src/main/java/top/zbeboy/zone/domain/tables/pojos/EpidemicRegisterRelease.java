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
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpidemicRegisterRelease implements Serializable {

    private static final long serialVersionUID = 2050656140;

    private String    epidemicRegisterReleaseId;
    private String    title;
    private String    username;
    private String    publisher;
    private Timestamp releaseTime;

    public EpidemicRegisterRelease() {}

    public EpidemicRegisterRelease(EpidemicRegisterRelease value) {
        this.epidemicRegisterReleaseId = value.epidemicRegisterReleaseId;
        this.title = value.title;
        this.username = value.username;
        this.publisher = value.publisher;
        this.releaseTime = value.releaseTime;
    }

    public EpidemicRegisterRelease(
        String    epidemicRegisterReleaseId,
        String    title,
        String    username,
        String    publisher,
        Timestamp releaseTime
    ) {
        this.epidemicRegisterReleaseId = epidemicRegisterReleaseId;
        this.title = title;
        this.username = username;
        this.publisher = publisher;
        this.releaseTime = releaseTime;
    }

    @NotNull
    @Size(max = 64)
    public String getEpidemicRegisterReleaseId() {
        return this.epidemicRegisterReleaseId;
    }

    public void setEpidemicRegisterReleaseId(String epidemicRegisterReleaseId) {
        this.epidemicRegisterReleaseId = epidemicRegisterReleaseId;
    }

    @NotNull
    @Size(max = 100)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @Size(max = 30)
    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @NotNull
    public Timestamp getReleaseTime() {
        return this.releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EpidemicRegisterRelease (");

        sb.append(epidemicRegisterReleaseId);
        sb.append(", ").append(title);
        sb.append(", ").append(username);
        sb.append(", ").append(publisher);
        sb.append(", ").append(releaseTime);

        sb.append(")");
        return sb.toString();
    }
}
