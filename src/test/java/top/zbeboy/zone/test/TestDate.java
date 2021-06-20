package top.zbeboy.zone.test;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.junit.jupiter.api.Test;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class TestDate {

    @Test
    public void testDate() {


        DateTime time = new DateTime(DateTimeUtil.defaultParseSqlDate("2020-08-26"));

        time = time.withDayOfWeek(2);

        System.out.println(DateFormatUtils.format(time.toDate(), "yyyyMMdd"));

        java.sql.Date date = DateTimeUtil.parseSqlDate(new Date());
        System.out.println(date);

        LocalDate d = LocalDate.now();
        System.out.println(calcNextFriday(d));

        java.sql.Date next = DateTimeUtil.calculationSqlNextWeekDay(date, 6);
        System.out.println(next);
    }

    @Test
    public void calcMinutes(){
        LocalDateTime date1 = DateTimeUtil.defaultParseLocalDateTime("2021-06-20 09:30:00");
        LocalDateTime date2 = DateTimeUtil.defaultParseLocalDateTime("2021-06-20 09:00:00");
        System.out.println("Difference is before : " + date1.isBefore(date2));
        System.out.println("Difference in milliseconds : " + Duration.between(date1, date2).abs().toMinutes());
    }

    @Test
    public void calcWeeks() {
        java.sql.Date data1 = DateTimeUtil.defaultParseSqlDate("2020-08-24");
        java.sql.Date data2 = DateTimeUtil.defaultParseSqlDate("2020-08-26");
        DateTime dt1 = new DateTime(data1);
        DateTime dt2 = new DateTime(data2);
        System.out.print(Weeks.weeksBetween(dt1, dt2).getWeeks() + 1 + " 周 ");
    }

    @Test
    public void calcWeekNextDay(){
        java.sql.Date data1 = DateTimeUtil.defaultParseSqlDate("2021-03-01");
        DateTime dt1 = new DateTime(data1);
        dt1 = dt1.plusWeeks(4);
        dt1 = dt1.withDayOfWeek(4);
        System.out.println(DateFormatUtils.format(dt1.toDate(), DateTimeUtil.STANDARD_FORMAT));
    }

    private LocalDate calcNextFriday(LocalDate d) {
        return d.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
    }
}
