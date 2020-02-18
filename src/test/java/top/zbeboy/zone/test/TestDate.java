package top.zbeboy.zone.test;

import org.junit.Test;
import top.zbeboy.zone.service.util.DateTimeUtil;

import java.util.Date;

public class TestDate {

    @Test
    public void testDate(){
        System.out.println(DateTimeUtil.formatSqlTimestamp(DateTimeUtil.getNowSqlTimestamp(), DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
    }
}
