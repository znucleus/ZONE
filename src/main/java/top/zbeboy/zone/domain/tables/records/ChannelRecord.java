/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.Channel;


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
public class ChannelRecord extends UpdatableRecordImpl<ChannelRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1762993898;

    /**
     * Setter for <code>zone.channel.channel_id</code>.
     */
    public void setChannelId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.channel.channel_id</code>.
     */
    public Integer getChannelId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>zone.channel.channel_name</code>.
     */
    public void setChannelName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.channel.channel_name</code>.
     */
    @NotNull
    @Size(max = 100)
    public String getChannelName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Channel.CHANNEL.CHANNEL_ID;
    }

    @Override
    public Field<String> field2() {
        return Channel.CHANNEL.CHANNEL_NAME;
    }

    @Override
    public Integer component1() {
        return getChannelId();
    }

    @Override
    public String component2() {
        return getChannelName();
    }

    @Override
    public Integer value1() {
        return getChannelId();
    }

    @Override
    public String value2() {
        return getChannelName();
    }

    @Override
    public ChannelRecord value1(Integer value) {
        setChannelId(value);
        return this;
    }

    @Override
    public ChannelRecord value2(String value) {
        setChannelName(value);
        return this;
    }

    @Override
    public ChannelRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChannelRecord
     */
    public ChannelRecord() {
        super(Channel.CHANNEL);
    }

    /**
     * Create a detached, initialised ChannelRecord
     */
    public ChannelRecord(Integer channelId, String channelName) {
        super(Channel.CHANNEL);

        set(0, channelId);
        set(1, channelName);
    }
}
