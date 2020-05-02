/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.zbeboy.zone.domain.tables.Channel;
import top.zbeboy.zone.domain.tables.records.ChannelRecord;


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
@Repository
public class ChannelDao extends DAOImpl<ChannelRecord, top.zbeboy.zone.domain.tables.pojos.Channel, Integer> {

    /**
     * Create a new ChannelDao without any configuration
     */
    public ChannelDao() {
        super(Channel.CHANNEL, top.zbeboy.zone.domain.tables.pojos.Channel.class);
    }

    /**
     * Create a new ChannelDao with an attached configuration
     */
    @Autowired
    public ChannelDao(Configuration configuration) {
        super(Channel.CHANNEL, top.zbeboy.zone.domain.tables.pojos.Channel.class, configuration);
    }

    @Override
    public Integer getId(top.zbeboy.zone.domain.tables.pojos.Channel object) {
        return object.getChannelId();
    }

    /**
     * Fetch records that have <code>channel_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.Channel> fetchRangeOfChannelId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Channel.CHANNEL.CHANNEL_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>channel_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.Channel> fetchByChannelId(Integer... values) {
        return fetch(Channel.CHANNEL.CHANNEL_ID, values);
    }

    /**
     * Fetch a unique record that has <code>channel_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.Channel fetchOneByChannelId(Integer value) {
        return fetchOne(Channel.CHANNEL.CHANNEL_ID, value);
    }

    /**
     * Fetch records that have <code>channel_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.Channel> fetchRangeOfChannelName(String lowerInclusive, String upperInclusive) {
        return fetchRange(Channel.CHANNEL.CHANNEL_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>channel_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.Channel> fetchByChannelName(String... values) {
        return fetch(Channel.CHANNEL.CHANNEL_NAME, values);
    }

    /**
     * Fetch a unique record that has <code>channel_name = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.Channel fetchOneByChannelName(String value) {
        return fetchOne(Channel.CHANNEL.CHANNEL_NAME, value);
    }
}
