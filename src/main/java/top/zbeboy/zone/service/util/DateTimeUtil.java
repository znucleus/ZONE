package top.zbeboy.zone.service.util;

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd";
    public static final String HOUR_MINUTE_FORMAT = "HH:mm";

    /**
     * timestamp
     *
     * @param timestamp java.sql.timestamp
     * @return java.util.date
     */
    public static java.util.Date sqlTimestampToUtilDate(java.sql.Timestamp timestamp) {
        return new java.util.Date(timestamp.getTime());
    }

    /**
     * timestamp to string
     *
     * @param timestamp sql
     * @param format    格式
     * @return string
     */
    public static String formatSqlTimestamp(java.sql.Timestamp timestamp, String format) {
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * util date to sql date
     *
     * @param date util date
     * @return sql date
     */
    public static java.sql.Date utilDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * util date to sql timestamp
     *
     * @param date util date
     * @return sql timestamp
     */
    public static java.sql.Timestamp utilDateToSqlTimestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    /**
     * 格式化date
     *
     * @param date   日期
     * @param format 格式
     * @return 格式化后的时间
     */
    public static String formatUtilDate(java.util.Date date, String format) {
        return date.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 格式化date
     *
     * @param date   日期
     * @param format 格式
     * @return 格式化后的时间
     */
    public static String formatSqlDate(java.sql.Date date, String format) {
        return date.toLocalDate().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 格式化time
     *
     * @param time   时间
     * @param format 格式
     * @return 格式化后的时间
     */
    public static String formatSqlTime(java.sql.Time time, String format) {
        return time.toLocalTime().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 格式化date
     *
     * @param date 日期
     * @return 格式化后的时间
     */
    public static String defaultFormatUtilDate(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(STANDARD_FORMAT));
    }

    /**
     * 格式化Timestamp
     *
     * @param timestamp 日期
     * @return 格式化后的时间
     */
    public static String defaultFormatSqlTimestamp(java.sql.Timestamp timestamp) {
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(STANDARD_FORMAT));
    }

    /**
     * 格式化成sql date
     *
     * @param date 日期
     * @return sql date
     */
    public static java.sql.Date defaultParseSqlDate(String date) {
        return new java.sql.Date(java.sql.Date.from(LocalDate.parse(StringUtils.trim(date), DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_FORMAT)).atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
    }

    /**
     * 格式化成sql date
     *
     * @param date   date 日期
     * @param format 格式
     * @return sql date
     */
    public static java.sql.Date parseSqlDate(String date, String format) {
        return new java.sql.Date(java.sql.Date.from(LocalDate.parse(StringUtils.trim(date), DateTimeFormatter.ofPattern(format)).atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
    }

    /**
     * 格式化成util date
     *
     * @param date   date 日期
     * @param format 格式
     * @return util date
     */
    public static java.util.Date parseUtilDate(String date, String format) {
        return new java.util.Date(java.sql.Date.from(LocalDateTime.parse(StringUtils.trim(date), DateTimeFormatter.ofPattern(format)).atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }

    /**
     * 格式化成sql date
     *
     * @param date 日期
     * @return sql date
     */
    public static java.sql.Timestamp defaultParseSqlTimestamp(String date) {
        return new java.sql.Timestamp(java.sql.Timestamp.from(LocalDateTime.parse(StringUtils.trim(date), DateTimeFormatter.ofPattern(STANDARD_FORMAT)).atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }

    /**
     * 格式化成sql date
     *
     * @param date   date 日期
     * @param format 格式
     * @return sql date
     */
    public static java.sql.Timestamp parseSqlTimestamp(String date, String format) {
        return new java.sql.Timestamp(java.sql.Timestamp.from(LocalDateTime.parse(StringUtils.trim(date), DateTimeFormatter.ofPattern(format)).atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }

    /**
     * 格式化成sql time
     *
     * @param time 时间
     * @return sql date
     */
    public static java.sql.Time defaultParseSqlTime(String time) {
        return new java.sql.Time(java.sql.Time.from(LocalTime.parse(StringUtils.trim(time), DateTimeFormatter.ofPattern(HOUR_MINUTE_FORMAT)).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }

    /**
     * 格式化成sql time
     *
     * @param time   时间
     * @param format 格式
     * @return sql time
     */
    public static java.sql.Time parseSqlTime(String time, String format) {
        return new java.sql.Time(java.sql.Time.from(LocalTime.parse(StringUtils.trim(time), DateTimeFormatter.ofPattern(format)).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()).getTime());
    }

    /**
     * 当前时间是否在时间范围
     *
     * @param after  之前时间
     * @param before 之后时间
     * @return true or false
     */
    public static Boolean nowRangeSqlTimestamp(java.sql.Timestamp after, java.sql.Timestamp before) {
        java.sql.Timestamp now = new java.sql.Timestamp(Clock.systemDefaultZone().millis());
        return now.after(after) && now.before(before);
    }

    /**
     * 当前时间大于某一时间
     *
     * @param after 某一时间
     * @return true or false
     */
    public static Boolean nowAfterSqlTimestamp(java.sql.Timestamp after) {
        java.sql.Timestamp now = new java.sql.Timestamp(Clock.systemDefaultZone().millis());
        return now.after(after);
    }

    /**
     * 当前时间小于某一时间
     *
     * @param before 某一时间
     * @return true or false
     */
    public static Boolean nowBeforeSqlTimestamp(java.sql.Timestamp before) {
        java.sql.Timestamp now = new java.sql.Timestamp(Clock.systemDefaultZone().millis());
        return now.before(before);
    }

    /**
     * 得到当前时间
     *
     * @return 当前时间
     */
    public static java.sql.Timestamp getNowSqlTimestamp() {
        return new java.sql.Timestamp(Clock.systemDefaultZone().millis());
    }

    /**
     * 得到当前时间
     *
     * @return 当前时间
     */
    public static java.sql.Date getNowSqlDate() {
        return new java.sql.Date(Clock.systemDefaultZone().millis());
    }

    /**
     * 得到当前时间
     *
     * @return 当前时间
     */
    public static String getLocalDateTime(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 得到当前年份
     *
     * @return 年份
     */
    public static int getNowYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
}
