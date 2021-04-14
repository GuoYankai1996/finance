package com.binance.web.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class DateUtil {

    public static final String YMD_FORMAT = "yyyy-MM-dd";

    public static final String YMDHMS_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static final String YMDHMS_FORMAT_24 = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<SimpleDateFormat> YMD_FORMATTER = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> YMD_HM_FORMATTER = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };

    private static ThreadLocal<DateFormat> mdFormatter = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };

    private static ThreadLocal<DateFormat> todayFormatter = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("今天HH:mm");
        }
    };

    private static ThreadLocal<DateFormat> tomorrowFormatter = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("明天HH:mm");
        }
    };

    private static String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private static String todayString = "(今天)";

    private static String yesterdayString = "(昨天)";

    /**
     * String格式（yyyy-MM-dd）date日期增加操作
     */
    public static String dateStringAdd(String date, int addNum) throws ParseException {

        if (null == date) {
            return null;
        }

        Date result = YMD_FORMATTER.get().parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        calendar.add(calendar.DATE, addNum);

        return YMD_FORMATTER.get().format(calendar.getTime());
    }

    public static String formatTimestamp(long timestamp,String formatter){
        return formatDate(new Date(timestamp),formatter);
    }

    /**
     * String格式（yyyy-MM-dd）date日期增加操作
     */
    public static String dateStringAddReturnWithoutYear(String date, int addNum) throws ParseException {

        if (null == date) {
            return null;
        }

        Date result = YMD_FORMATTER.get().parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        calendar.add(calendar.DATE, addNum);

        return mdFormatter.get().format(calendar.getTime());
    }

    public static String getDateString(Date date) {
        if (date == null) {
            return null;
        }
        String dateStr = YMD_FORMATTER.get().format(date);
        return dateStr;
    }

    /**
     * 查询传入参数XXXX-XX-XX,返回星期信息 如为今天，昨天则返回(今天)(昨天) 否则返回(周一)，(周)
     */
    public static String getWeekString(String date) throws ParseException {
        String result = null;
        if (date == null) {
            return result;
        }

        String todayString = DateUtil.getDateString(new Date());
        String yesterday = DateUtil.dateStringAdd(todayString, -1);

        if (date.equals(todayString)) {
            result = DateUtil.todayString;
        } else if (date.equals(yesterday)) {
            result = DateUtil.yesterdayString;
        } else {
            Date temp = YMD_FORMATTER.get().parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(temp);

            int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0) {
                w = 0;
            }
            result = "(" + weekOfDays[w] + ")";
        }

        return result;
    }


    /**
     * 获得明天string格式时间
     */
    public static String getTomorrowString() throws ParseException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, +1);

        return YMD_FORMATTER.get().format(cal.getTime());
    }

    /**
     * 截取取日期的 月日字符串格式,如 "0508"
     *
     * @param date 截取目标日期
     * @return 月日
     */
    public static String getMonthAndDay(Date date) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateStr = format.format(date);
        return dateStr.substring(4);
    }

    public static String dateToFormat(Date date) {
        if (null == date) {
            return "";
        }
        return YMD_FORMATTER.get().format(date);
    }


    public static Date parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return YMD_HM_FORMATTER.get().parse(dateStr);
        } catch (ParseException e) {
            log.error("[error]DateUtil.parseDate,Fail to parse date:" + dateStr);
        }
        return null;
    }

    public static Date parseDate(String date, String dateFormat) {
        try {
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            return formatter.parse(date);
        } catch (ParseException e) {
            log.error("fail to parse date,sourceDate=" + date + ",dateFormat=" + dateFormat, e);
        }

        return null;
    }

    /**
     * 添加指定维度的时间
     *
     * @param unit, 添加维度(分钟、小时): eg: Calendar.MINUTE
     */
    public static Date addTime(Date date, int unit, int addNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(unit, addNum);
        return calendar.getTime();
    }

    /**
     * 格式化到分钟秒 mm:ss
     */
    public static String formatHHmm(Date date) {
        if (null == date) {
            return "";
        }
        DateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);

    }


    /**
     * 计算两个日期之间的自然天数
     */
    public static int betweenDay(Date one, Date another) {
        return (int) Math.abs((beginOfDate(one).getTime() - beginOfDate(another).getTime()) / 24 * 60 * 60 * 1000);
    }

    public static Date beginOfDate(Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        return c1.getTime();
    }


    /**
     * 格式化到分钟秒 yy-MM-dd
     */
    public static String formatDate(Date date, String formatter) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatter);
        return dateFormat.format(date);
    }

    /**
     * 格式化到分钟秒 yy-MM-dd
     */
    public static String formatyyMMdd(Date date) {
        if (null == date) {
            return "";
        }

        return YMD_FORMATTER.get().format(date);
    }

    /**
     *
     */
    public static String formatYYYYmmDDHHmm(Date date) {
        if (null == date) {
            return "";
        }
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return format.format(date);
        } catch (Exception e) {
            log.error("format error date=" + date, e);
            return "";
        }

    }

    /**
     *
     */
    public static String formatFetchTimeStr(Date date) {
        if (null == date) {
            return StringUtils.EMPTY;
        }

        try {
            if (isSameDay(new Date(), date)) {
                return todayFormatter.get().format(date);
            } else {
                return YMD_HM_FORMATTER.get().format(date);
            }
        } catch (Exception e) {
            log.error("format error date=" + date, e);
        }

        return StringUtils.EMPTY;
    }


    /**
     * 菜品支持单独配置备餐时间，因此存在跨天场景的取餐时间展示
     */
    public static String crossDayFormatFetchTimeStr(Date date) {
        if (null == date) {
            return StringUtils.EMPTY;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date tomorrow = calendar.getTime();
        try {
            if (isSameDay(new Date(), date)) {
                return todayFormatter.get().format(date);
            } else if (isSameDay(tomorrow, date)) {
                return tomorrowFormatter.get().format(date);
            } else {
                return YMD_HM_FORMATTER.get().format(date);
            }
        } catch (Exception e) {
            log.error("format error date=" + date, e);
        }

        return StringUtils.EMPTY;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(date1);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(date2);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }


    public static Date addDays(Date date, int days) {
        LocalDateTime dateTime = fromDate(date);
        return toDate(dateTime.plusDays(days));
    }

    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }
        LocalDateTime dateTime = fromDate(date);
        return toDate(dateTime.toLocalDate().atStartOfDay());
    }

    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        LocalDateTime dateTime = fromDate(date);
        return toDate(dateTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1));
    }

    public static long diffDaysBetweenDates(Date startDate, Date endDate) {
        return Period.between(fromDate(startDate).toLocalDate(), fromDate(endDate).toLocalDate()).getDays();
    }

    public static long diffDaysBetweenDateTimes(Date startDate, Date endDate) {
        return Duration.between(fromDate(startDate), fromDate(endDate)).toDays();
    }


    public static String getTime(Date date) {
        LocalDateTime dateTime = fromDate(date);
        return dateTime.toLocalTime().format(DateTimeFormatter.ISO_TIME);
    }

    public static LocalDateTime fromDate(Date date) {
        if (date == null) {
            return LocalDateTime.now();
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return new Date();
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static List<String> collectLocalDates(Date startDate, Date endDate, List<Integer> weekDays) {
        return collectLocalDates(date2LocalDate(startDate), date2LocalDate(endDate), weekDays);
    }

    // 连续时间转离散，过滤星期 效率略低
    private static List<String> collectLocalDates(LocalDate start, LocalDate end, List<Integer> weekDays) {
        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .filter(localDate -> weekDays.contains(localDate.getDayOfWeek().getValue()))
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    private static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate localDate = zdt.toLocalDate();
        return localDate;
    }

    /**
     * 把时间戳转为dataString
     */
    public static String timeStamp2DataString(Long timeStamp) {
        if (timeStamp == null){
            return "";
        }
        return formatTimestamp(timeStamp, YMDHMS_FORMAT_24);
    }


    public static String getNowStringDate() {
        Long timeStamp = System.currentTimeMillis();
        return formatTimestamp(timeStamp, YMDHMS_FORMAT_24);
    }
}
