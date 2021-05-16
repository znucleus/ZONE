package top.zbeboy.zone.test;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

public class TestIcal {

    @Test
    public void testExample() throws IOException {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", "net.fortuna.ical4j.util.MapTimeZoneCache");
        // Create a TimeZone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
        VTimeZone tz = timezone.getVTimeZone();

        // Start Date is on: April 1, 2008, 9:00 am
        java.util.Calendar startDate = new GregorianCalendar();
        startDate.setTimeZone(timezone);
        startDate.set(java.util.Calendar.MONTH, java.util.Calendar.MARCH);
        startDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
        startDate.set(java.util.Calendar.YEAR, 2021);
        startDate.set(java.util.Calendar.HOUR_OF_DAY, 9);
        startDate.set(java.util.Calendar.MINUTE, 0);
        startDate.set(java.util.Calendar.SECOND, 0);

        // End Date is on: April 1, 2008, 13:00
        java.util.Calendar endDate = new GregorianCalendar();
        endDate.setTimeZone(timezone);
        endDate.set(java.util.Calendar.MONTH, java.util.Calendar.MARCH);
        endDate.set(java.util.Calendar.DAY_OF_MONTH, 5);
        endDate.set(java.util.Calendar.YEAR, 2021);
        endDate.set(java.util.Calendar.HOUR_OF_DAY, 13);
        endDate.set(java.util.Calendar.MINUTE, 0);
        endDate.set(java.util.Calendar.SECOND, 0);

        // Create the event
        String eventName = "计算机科学与技术";
        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent meeting = new VEvent(start, end, eventName);
        meeting.getProperties().add(new Location("明虹楼503"));
        meeting.getProperties().add(new Description("1-12周 08:00:00-09:35:00"));

        // 重复事件
        Recur recur = new Recur(Recur.Frequency.WEEKLY, 5);
        recur.getDayList().add(WeekDay.MO);
        /*recur.getDayList().add(WeekDay.TU);
        recur.getDayList().add(WeekDay.WE);
        recur.getDayList().add(WeekDay.TH);
        recur.getDayList().add(WeekDay.FR);*/
        RRule rule = new RRule(recur);
        meeting.getProperties().add(rule);
        // 提醒,提前20分钟
        VAlarm valarm = new VAlarm(java.time.Duration.ofMinutes(-20));
        valarm.getProperties().add(new Summary("计算机科学与技术"));
        valarm.getProperties().add(Action.DISPLAY);
        valarm.getProperties().add(new Location("明虹楼503"));
        valarm.getProperties().add(new Description("1-12周 08:00:00-09:35:00"));
        // 将VAlarm加入VEvent
        meeting.getAlarms().add(valarm);

        // add timezone info..
        meeting.getProperties().add(tz.getTimeZoneId());

        // generate unique identifier..
        UidGenerator ug = new RandomUidGenerator();
        Uid uid = ug.generateUid();
        meeting.getProperties().add(uid);


        // Create a calendar
        net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Timetable Calendar//iCal4j 1.0//EN"));
        icsCalendar.getProperties().add(Version.VERSION_2_0);
        icsCalendar.getProperties().add(CalScale.GREGORIAN);


        // Add the event and print
        icsCalendar.getComponents().add(meeting);
        // 验证
        icsCalendar.validate();
        FileOutputStream fout = new FileOutputStream("e://1.ics");
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(icsCalendar, fout);
    }


}
