/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;

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
public class AttendMapKey implements Serializable {

    private static final long serialVersionUID = 844715579;

    private Integer id;
    private String  mapKey;

    public AttendMapKey() {}

    public AttendMapKey(AttendMapKey value) {
        this.id = value.id;
        this.mapKey = value.mapKey;
    }

    public AttendMapKey(
        Integer id,
        String  mapKey
    ) {
        this.id = id;
        this.mapKey = mapKey;
    }

    @NotNull
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 100)
    public String getMapKey() {
        return this.mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AttendMapKey (");

        sb.append(id);
        sb.append(", ").append(mapKey);

        sb.append(")");
        return sb.toString();
    }
}
