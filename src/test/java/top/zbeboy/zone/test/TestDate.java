package top.zbeboy.zone.test;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.junit.Test;
import top.zbeboy.zone.service.util.DateTimeUtil;

import java.util.Date;

import static top.zbeboy.zone.domain.Tables.ATTEND_RELEASE_SUB;

public class TestDate {

    @Test
    public void testDate(){
        DateTime now = DateTime.now();
        DateTime startTime = new DateTime(DateTimeUtil.defaultParseSqlTimestamp("2020-02-19 22:00:00"));
        System.out.println(Minutes.minutesBetween(now, startTime).getMinutes());
    }
}
