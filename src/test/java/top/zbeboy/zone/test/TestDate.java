package top.zbeboy.zone.test;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.junit.Test;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;

import java.util.Date;

public class TestDate {

    @Test
    public void testDate() {


        DateTime time = new DateTime(DateTimeUtil.defaultParseSqlDate("2020-08-26"));
        time = time.withDayOfWeek(7);

        System.out.println(DateFormatUtils.format(time.toDate(),  "yyyyMMdd"));

        java.sql.Date date = DateTimeUtil.parseSqlDate(new Date());
        System.out.println(date);
    }
}
