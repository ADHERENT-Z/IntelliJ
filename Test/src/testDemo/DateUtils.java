package testDemo;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtils {

    /**
     * 常用变量
     */
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_HMS = "HH:mm:ss";
    public static final String DATE_FORMAT_HM = "HH:mm";
    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YMDHMS = "yyyyMMddHHmmss";
    public static final long ONE_DAY_MILLS = 3600000 * 24;
    public static final int WEEK_DAYS = 7;
    private static final int dateLength = DATE_FORMAT_YMDHM.length();

    public static String formatDateToString(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * 字符串转换为制定格式日期
     * (注意：当你输入的日期是2014-12-21 12:12，format对应的应为yyyy-MM-dd HH:mm
     * 否则异常抛出)
     * @param date
     * @param format
     * @return
     * @throws ParseException
     * @
     */
    public static Date formatStringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        }
    }

    /**
     * 判断一个日期是否属于两个时段内
     * @param time
     * @param timeRange
     * @return
     */
    public static boolean isTimeInRange(Date time, Date[] timeRange) {
        return (!time.before(timeRange[0]) && !time.after(timeRange[1]));
    }

    /**
     * 从完整的时间截取精确到分的时间
     *
     * @param fullDateStr
     * @return
     */
    public static String getDateToMinute(String fullDateStr) {
        return fullDateStr == null ? null
                : (fullDateStr.length() >= dateLength ? fullDateStr.substring(
                0, dateLength) : fullDateStr);
    }

    /**
     * 返回指定年度的所有周。List中包含的是String[2]对象 string[0]本周的开始日期,string[1]是本周的结束日期。
     * 日期的格式为YYYY-MM-DD 每年的第一个周，必须包含星期一且是完整的七天。
     * 例如：2009年的第一个周开始日期为2009-01-05，结束日期为2009-01-11。 星期一在哪一年，那么包含这个星期的周就是哪一年的周。
     * 例如：2008-12-29是星期一，2009-01-04是星期日，哪么这个周就是2008年度的最后一个周。
     *
     * @param year
     * 格式 YYYY ，必须大于1900年度 小于9999年
     * @return @
     */
    public static List<String[]> getWeeksByYear(final int year) {
        int weeks = getWeekNumOfYear(year);
        List<String[]> result = new ArrayList<String[]>(weeks);
        int start = 1;
        int end = 7;
        for (int i = 1; i <= weeks; i++) {
            String[] tempWeek = new String[2];
            tempWeek[0] = getDateForDayOfWeek(year, i, start);
            tempWeek[1] = getDateForDayOfWeek(year, i, end);
            result.add(tempWeek);
        }
        return result;
    }

    /**
     * 计算指定年、周的上一年、周
     *
     * @param year
     * @param week
     * @return @
     */
    public static int[] getLastYearWeek(int year, int week) {
        if (week <= 0) {
            throw new IllegalArgumentException("周序号不能小于1！！");
        }
        int[] result = { week, year };
        if (week == 1) {
            // 上一年
            result[1] -= 1;
            // 最后一周
            result[0] = getWeekNumOfYear(result[1]);
        } else {
            result[0] -= 1;
        }
        return result;
    }

    /**
     * 下一个[周，年]
     *
     * @param year
     * @param week
     * @return @
     */
    public static int[] getNextYearWeek(int year, int week) {
        if (week <= 0) {
            throw new IllegalArgumentException("周序号不能小于1！！");
        }
        int[] result = { week, year };
        int weeks = getWeekNumOfYear(year);
        if (week == weeks) {
            // 下一年
            result[1] += 1;
            // 第一周
            result[0] = 1;
        } else {
            result[0] += 1;
        }
        return result;
    }

    /**
     * 计算指定年度共有多少个周。(从周一开始)
     *
     * @param year
     * @return @
     */
    public static int getWeekNumOfYear(final int year) {
        return getWeekNumOfYear(year, Calendar.MONDAY);
    }

    /**
     * 计算指定年度共有多少个周。
     *
     * @param year
     * yyyy
     * @return @
     */
    public static int getWeekNumOfYear(final int year, int firstDayOfWeek) {
        // 每年至少有52个周 ，最多有53个周。
        int minWeeks = 52;
        int maxWeeks = 53;
        int result = minWeeks;
        int sIndex = 4;
        String date = getDateForDayOfWeek(year, maxWeeks, firstDayOfWeek);
        // 判断年度是否相符，如果相符说明有53个周。
        if (date.substring(0, sIndex).equals(year)) {
            result = maxWeeks;
        }
        return result;
    }

    public static int getWeeksOfWeekYear(final int year) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        cal.set(Calendar.YEAR, year);
        return cal.getWeeksInWeekYear();
    }

    /**
     * 获取指定年份的第几周的第几天对应的日期yyyy-MM-dd(从周一开始)
     *
     * @param year
     * @param weekOfYear
     * @param dayOfWeek
     * @return yyyy-MM-dd 格式的日期 @
     */
    public static String getDateForDayOfWeek(int year, int weekOfYear,
                                             int dayOfWeek) {
        return getDateForDayOfWeek(year, weekOfYear, dayOfWeek, Calendar.MONDAY);
    }

    /**
     * 获取指定年份的第几周的第几天对应的日期yyyy-MM-dd，指定周几算一周的第一天（firstDayOfWeek）
     *
     * @param year
     * @param weekOfYear
     * @param dayOfWeek
     * @param firstDayOfWeek
     * 指定周几算一周的第一天
     * @return yyyy-MM-dd 格式的日期
     */
    public static String getDateForDayOfWeek(int year, int weekOfYear,
                                             int dayOfWeek, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        return formatDateToString(cal.getTime(), DATE_FORMAT_YMD);
    }

    /**
     * 获取指定日期星期几
     *
     * @param datetime
     * @throws ParseException
     * @
     */
    public static int getWeekOfDate(String datetime) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(WEEK_DAYS);
        Date date = formatStringToDate(datetime, DATE_FORMAT_YMD);
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);

    }

    /**
     * 计算某年某周内的所有日期(从周一开始 为每周的第一天)
     *
     * @param yearNum
     * @param weekNum
     * @return @
     */
    public static List<String> getWeekDays(int yearNum, int weekNum) {
        return getWeekDays(yearNum, weekNum, Calendar.MONDAY);
    }

    /**
     * 计算某年某周内的所有日期(七天)
     *
     * @param year
     * @param weekOfYear
     * @param firstDayOfWeek
     * @return yyyy-MM-dd 格式的日期列表
     */
    public static List<String> getWeekDays(int year, int weekOfYear,
                                           int firstDayOfWeek) {
        List<String> dates = new ArrayList<String>();
        int dayOfWeek = firstDayOfWeek;
        for (int i = 0; i < WEEK_DAYS; i++) {
            dates.add(getDateForDayOfWeek(year, weekOfYear, dayOfWeek++,
                    firstDayOfWeek));
        }
        return dates;
    }

    /**
     * 获取目标日期的上周、或本周、或下周的年、周信息
     *
     * @param queryDate
     * 传入的时间
     * @param weekOffset
     * -1:上周 0:本周 1:下周
     * @param firstDayOfWeek
     * 每周以第几天为首日
     * @return
     * @throws ParseException
     */
    public static int[] getWeekAndYear(String queryDate, int weekOffset,
                                       int firstDayOfWeek) throws ParseException {

        Date date = formatStringToDate(queryDate, DATE_FORMAT_YMD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        calendar.setMinimalDaysInFirstWeek(WEEK_DAYS);
        int year = calendar.getWeekYear();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int[] result = { week, year };
        switch (weekOffset) {
            case 1:
                result = getNextYearWeek(year, week);
                break;
            case -1:
                result = getLastYearWeek(year, week);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 计算个两日期的天数
     *
     * @param startDate
     * 开始日期字串
     * @param endDate
     * 结束日期字串
     * @return
     * @throws ParseException
     */
    public static int getDaysBetween(String startDate, String endDate)
            throws ParseException {
        int dayGap = 0;
        if (startDate != null && startDate.length() > 0 && endDate != null
                && endDate.length() > 0) {
            Date end = formatStringToDate(endDate, DATE_FORMAT_YMD);
            Date start = formatStringToDate(startDate, DATE_FORMAT_YMD);
            dayGap = getDaysBetween(start, end);
        }
        return dayGap;
    }

    private static int getDaysBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / ONE_DAY_MILLS);
    }

    /**
     * 计算两个日期之间的天数差
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysGapOfDates(Date startDate, Date endDate) {
        int date = 0;
        if (startDate != null && endDate != null) {
            date = getDaysBetween(startDate, endDate);
        }
        return date;
    }

    /**
     * 计算两个日期之间的年份差距
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int getYearGapOfDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }
        Calendar helpCalendar = Calendar.getInstance();
        helpCalendar.setTime(firstDate);
        int firstYear = helpCalendar.get(Calendar.YEAR);
        helpCalendar.setTime(secondDate);
        int secondYear = helpCalendar.get(Calendar.YEAR);
        return secondYear - firstYear;
    }



    /**
     *
     * 根据指定的日期，增加或者减少天数
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    /**
     * 根据指定的日期，类型，增加或减少数量
     *
     * @param date
     * @param calendarField
     * @param amount
     * @return
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }





    /**
     * 获取当前日期的最大日期 时间2014-12-21 23:59:59
     * @return
     */
    public static Date getCurDateWithMaxTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }




    /**
     * 根据时间点获取时间区间
     *
     * @param hours
     * @return
     */
    public static List<String[]> getTimePointsByHour(int[] hours) {
        List<String[]> hourPoints = new ArrayList<String[]>();
        String sbStart = ":00:00";
        String sbEnd = ":59:59";
        for (int i = 0; i < hours.length; i++) {
            String[] times = new String[2];
            times[0] = hours[i] + sbStart;
            times[1] = (hours[(i + 1 + hours.length) % hours.length] - 1)
                    + sbEnd;
            hourPoints.add(times);
        }
        return hourPoints;
    }

    /**
     * 获取当前日期的最小日期时间 2014-12-21 00:00:00
     * @return
     */
    public static Date getCurDateWithMinTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }



    //获取当天的开始时间
    public static java.util.Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //获取当天的结束时间
    public static java.util.Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    //获取昨天的开始时间
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    //获取昨天的结束时间
    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    //获取明天的开始时间
    public static Date getBeginDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }

    //获取明天的结束时间
    public static Date getEndDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    //获取本周的结束时间
    public static Date getEndDayOfWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    //获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    //获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    //获取本年的开始时间
    public static java.util.Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        // cal.set
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);

        return getDayStartTime(cal.getTime());
    }

    //获取本年的结束时间
    public static java.util.Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    //获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    //获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    //两个日期相减得到的天数
    public static int getDiffDays(Date beginDate, Date endDate) {

        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }

        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);

        int days = new Long(diff).intValue();

        return days;
    }

    //两个日期相减得到的毫秒数
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }

    //获取两个日期中的最大日期
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }

    //获取两个日期中的最小日期
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }

    //返回某月该季度的第一个月
    public static Date getFirstSeasonDate(Date date) {
        final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = SEASON[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        return cal.getTime();
    }

    //返回某个日期下几天的日期
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }

    //返回某个日期前几天的日期
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }

    //获取某年某月到某年某月按天的切片日期集合（间隔天数的日期集合）
    public static List getTimeList(int beginYear, int beginMonth, int endYear,
                                   int endMonth, int k) {
        List list = new ArrayList();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getTimeList(beginYear, j, k));

            }
        } else {
            {
                for (int j = beginMonth; j < 12; j++) {
                    list.add(getTimeList(beginYear, j, k));
                }

                for (int i = beginYear + 1; i < endYear; i++) {
                    for (int j = 0; j < 12; j++) {
                        list.add(getTimeList(i, j, k));
                    }
                }
                for (int j = 0; j <= endMonth; j++) {
                    list.add(getTimeList(endYear, j, k));
                }
            }
        }
        return list;
    }

    //获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
    public static List getTimeList(int beginYear, int beginMonth, int k) {
        List list = new ArrayList();
        Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
        int max = begincal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i < max; i = i + k) {
            list.add(begincal.getTime());
            begincal.add(Calendar.DATE, k);
        }
        begincal = new GregorianCalendar(beginYear, beginMonth, max);
        list.add(begincal.getTime());
        return list;
    }
}
