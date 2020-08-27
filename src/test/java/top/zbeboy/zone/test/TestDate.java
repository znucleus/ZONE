package top.zbeboy.zone.test;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.junit.Test;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class TestDate {

    @Test
    public void testDate() {


        DateTime time = new DateTime(DateTimeUtil.defaultParseSqlDate("2020-08-26"));

        time = time.withDayOfWeek(2);

        System.out.println(DateFormatUtils.format(time.toDate(),  "yyyyMMdd"));

        java.sql.Date date = DateTimeUtil.parseSqlDate(new Date());
        System.out.println(date);

        LocalDate d = LocalDate.now();
        System.out.println(calcNextFriday(d));

        java.sql.Date next = DateTimeUtil.calculationSqlNextWeekDay(date, 6);
        System.out.println(next);
    }

    private LocalDate calcNextFriday(LocalDate d) {
        return d.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
    }
}
