package net.muxi.huashiapp.utils;

import android.text.TextUtils;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.data.Course;
import com.muxistudio.common.util.DateUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.PreferenceUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by ybao on 16/7/27.
 */
public class TimeTableUtil {
    /**
     * 上课周数是否含有本周
     *
     * @param week    当前周
     * @param weekStr 周数字符串
     */

    public static final int IN_RANGE = 0;
    public static final int OUT_RANGE = 3;
    public static final int IN_WEEK = 1;
    public static final int NOT_WEEK = 2;
    //引入这个四个变量为了解决一个问题 , 一些素质课的courseView会挡住一些 本来课表的课
    public static boolean isThisWeek(int week, String weekStr) {
        if (TextUtils.isEmpty(weekStr)) {
            return false;
        }
        String[] weeks = weekStr.split(",");
        for (String s : weeks) {
            if (s.equals(String.valueOf(week))) {
                return true;
            }
        }
        return false;
    }



    /**
     * 获取某个位置的所有的课程
     *
     * @param course     当前位置的课程
     * @param allCourses 所有课程的列表
     */
    public static List<Course> getAllCoursesInPosition(Course course, List<Course> allCourses) {
        List<Course> curPosCourses = new ArrayList<>();
        for (Course oneCourse : allCourses) {
            if (oneCourse.getDay().equals(course.getDay())
                    && oneCourse.getStart() == course.getStart()
                    && oneCourse.getDuring() == course.getDuring()) {
                curPosCourses.add(oneCourse);
            }
        }
        return curPosCourses;
    }

    /**
     * 获取课程背景颜色
     *
     * @param colorNumber 颜色值
     */
    public static int getCourseBg(int colorNumber) {
        int color = 0;
        switch (colorNumber) {
            case 0:
                color = R.drawable.ripple_orange;
                break;
            case 1:
                color = R.drawable.ripple_blue;
                break;
            case 2:
                color = R.drawable.ripple_green;
                break;
            case 3:
                color = R.drawable.ripple_yellow;
                break;
        }
        return color;
    }

    public static int getCourseBgAccordDay(int day) {
        int[] colors = {R.drawable.ripple_green_light,
                R.drawable.ripple_yellow,
                R.drawable.ripple_blue,
                R.drawable.ripple_orange,
                R.drawable.ripple_green};
        for (int i = 0; i < 7; i++) {
            if (day == i) {
                return colors[i % 5];
            }
        }
        return colors[0];
    }

    public static int getCourseBgAccordDay(String day) {
        int[] colors = {R.drawable.ripple_green_light,
                R.drawable.ripple_yellow,
                R.drawable.ripple_blue,
                R.drawable.ripple_orange,
                R.drawable.ripple_green};
        /*for (int i = 0; i < Constants.WEEKDAYS_XQ.length; i++) {
            if (day.equals(Constants.WEEKDAYS_XQ[i])) {
                return colors[i % 5];
            }*/
        for ( int i = 0 ; i < 7 ; i++ ) {
            if ( day.equals( (i+1)+"" )) {
                return colors[i % 5];
            }
        }
        return colors[0];
    }

    public static String simplifyCourse(String course) {
        if (course.length() > 8) {
            return course.substring(0, 7) + "...";
        } else {
            return course;
        }
    }

    /**
     * 返回课程时间
     *
     * @param time    课程的第几节
     * @param isStart 是否是上课开始的时间
     */
    public static String getCourseTime(int time, boolean isStart) {
        String s = "";
        if (isStart) {
           switch (time){
               case 1:return "8:00";
               case 3:return "10:10";
               case 5:return "14:00";
               case 7:return "16:10";
               case 9:return "18:30";
               case 11:return "20:15";
               default:return " ";


           }
        }

        int end = time - 1 + 8;
        s += end < 10 ? "0" + end : end;
        s += ":";
        s += time % 2 == 0 ? "40" : "45";
        return s;
    }

    /**
     * 获取当前周的周次
     */
    public static int getCurWeek() {
       int selectWeek=PreferenceUtil.getInt(PreferenceUtil.SELECTED_WEEK,-1);
        DateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINESE);
        String cur=dataFormat.format(new Date());
       //selectWeek==-1表示用户没有自己设置周数
       if (selectWeek==-1){
           String startTime=PreferenceUtil.getString(PreferenceUtil.FIRST_WEEK_DATE,"NULL");
           if (startTime.equals("NULL")){
               return 1;
           }
           try {
               int dis=(int)getDistanceWeek(startTime,cur);
               return dis+1;
           }catch (ParseException e){
               return 1;
           }

       }else {
           String startTime=PreferenceUtil.getString(PreferenceUtil.SELECTED_WEEK_DATE,"NULL");
           try {
               int dis=(int)getDistanceWeek(startTime,cur);
               return selectWeek+dis;
           }catch (ParseException e) {
               e.printStackTrace();
               return 1;
           }
       }
    }


    /**
     *
     * @param start 起始日期 yyyy-MM-dd
     * @param end 现在的日期  yyyy-MM-dd
     * @return 相隔的周数
     * @throws ParseException
     */
    public static long getDistanceWeek(String start, String end) throws ParseException {
        int[][] convert = {{1,2,3,4,5,6,7},{7,1,2,3,4,5,6}};

        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        Date date1 = dataFormat.parse(start);
        Date date2 = dataFormat.parse(end);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);
        calendar1.setTime(date1);
        int weekDay1 = calendar1.get(Calendar.DAY_OF_WEEK);
        date1.setTime(date1.getTime()-(convert[1][weekDay1-1]-1)*1000 * 60 * 60 * 24);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setFirstDayOfWeek(Calendar.MONDAY);
        calendar2.setTime(date2);
        int weekDay2 = calendar2.get(Calendar.DAY_OF_WEEK);
        date2.setTime(date2.getTime()-(convert[1][weekDay2-1]-1)*1000 * 60 * 60 * 24);

        return (date2.getTime()-date1.getTime()) / (1000 * 60 * 60 * 24) /7;

    }
  /**
   * 保存当前的周数
   *
   */
  public static void saveCurWeek(int week) {
      DateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINESE);
      PreferenceUtil.saveString(PreferenceUtil.SELECTED_WEEK_DATE,dataFormat.format(new Date()));
      PreferenceUtil.saveInt(PreferenceUtil.SELECTED_WEEK,week);
  }

    /**
     * 获取选择的周期的周次
     *
     * @param date 一周的第一天日期
     */
    public static int getSelectedWeek(Date date) {
        String defalutDate = DateUtil.getTheDateInYear(date,
                1 - DateUtil.getDayInWeek(new Date(System.currentTimeMillis())));
        int selectWeek = (int) DateUtil.getDistanceWeek(
                PreferenceUtil.getString(PreferenceUtil.FIRST_WEEK_DATE, defalutDate),
                DateUtil.toDateInYear(new Date(System.currentTimeMillis()))) + 1;
        selectWeek = selectWeek <= Constants.WEEKS_LENGTH ? selectWeek : Constants.WEEKS_LENGTH;
        selectWeek = selectWeek >= 1 ? selectWeek : 1;
        return selectWeek;
    }


    public static List<Course> getTodayCourse(List<Course> allCourseList) {
        List<Course> courseList = new ArrayList<>();
        for (int i = 0; i < allCourseList.size(); i++) {
            String weeks = Course.listToString(allCourseList.get(i).getWeeks());
            String day = allCourseList.get(i).getDay();
            if (isThisWeek(getCurWeek(), weeks)&& day.equals(DateUtil.getDayInWeek(new Date(System.currentTimeMillis()))+"")) {
                courseList.add(allCourseList.get(i));
            }
        }
        Collections.sort(courseList,(c1,c2) -> c1.start - c2.start);
        return courseList;
    }

    /**
     * 将 weekday 转换为 int 类型 星期一 -> 0
     */
    public static int weekday2num(String weekday) {
        int i = 0;
        Logger.d(weekday);
        if (isNumeric(weekday)) {
                i = Integer.parseInt(weekday);
        }
        if(i==0) {
            while (!weekday.equals(Constants.WEEKDAYS_XQ[i])) {
                i++;
                if (i >= 7) {
                    break;
                }
            }
        }
        return i;
    }

    public static boolean isNumeric(String str){
            for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean isSingleWeeks(List<Integer> weekList) {
        boolean b = true;
        if (weekList.size() < 2) {
            return false;
        }
        if (weekList.get(0) % 2 == 1) {
            for (int i = 0; i < weekList.size() - 1; i++) {
                if (weekList.get(i + 1) - weekList.get(i) != 2) {
                    b = false;
                    break;
                }
            }
            return b;
        } else {
            return false;
        }
    }

    public static boolean isDoubleWeeks(List<Integer> weekList) {
        boolean b = true;
        if (weekList.size() < 2) {
            return false;
        }
        if (weekList.get(0) % 2 == 0) {
            for (int i = 0; i < weekList.size() - 1; i++) {
                if (weekList.get(i + 1) - weekList.get(i) != 2) {
                    b = false;
                    break;
                }
            }
            return b;
        } else {
            return false;
        }
    }

    public static boolean
    isContinuOusWeeks(List<Integer> weekList) {
        if (weekList.size() < 2) {
            return false;
        }
      return weekList.get(weekList.size() - 1) - weekList.get(0) == weekList.size() - 1;
    }

    public static String getDisplayWeeks(List<Integer> weekList) {
        String s;
        int start;
        int end;
        if (isSingleWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1) + 1;
            s = String.format(Locale.CHINESE,"%d-%d周单", start, end);
        } else if (isDoubleWeeks(weekList)) {
            start = weekList.get(0) - 1;
            end = weekList.get(weekList.size() - 1);
            s = String.format(Locale.CHINESE,"%d-%d周双", start, end);
        } else if (isContinuOusWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1);
            s = String.format(Locale.CHINESE,"%d-%d周", start, end);
        } else {
            s = TextUtils.join(",", weekList);
            s += "周";
        }
        return s;
    }
}
