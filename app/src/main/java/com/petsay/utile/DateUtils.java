package com.petsay.utile;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/8
 * @Description
 */
public class DateUtils {

    public static String getFormatTime(){
        return DateFormat.format("yyyy-MM-dd kk:mm", new Date()).toString();
    }

    public static String getFormatTime(Date date){
        if(date == null)
            return "";
        else
            return DateFormat.format("MM-dd kk:mm",date).toString();
    }

    private final static int minute=60*1000;
    private final static int hour=60*minute;
    private final static long day=24*hour;
    public static String calculateTime(long time)
    {
        long TodayBaseTime=getTodayBaseTime();
        String str="";
        //		strs[0]=DateFormat.format("kk:mm", createTime).toString();
        long during=TodayBaseTime-time;

        if (System.currentTimeMillis()-time<=2*60*1000) {
            //两分钟
            str="刚刚";
        }else if (System.currentTimeMillis()-time<=hour) {
            //一小时
            str=(System.currentTimeMillis()-time)/minute+"分钟前";
        }else if (time-TodayBaseTime>=0) {
            //今天
            str="今天"+DateFormat.format("kk:mm", time).toString();
        }else if (during>0&&during<=7*day) {
            //七天内
            str=(during/day+1)+"天前";
        }else if (during>0&&during<=365*day) {
            //今年内
            str=DateFormat.format("MM-dd", time).toString();
        }else {
            str=DateFormat.format("yyyy-MM-dd", time).toString();
        }
        return str;
    }

    public static long getTodayBaseTime()
    {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        long a = time - date.getHours() * 60 * 1000 * 60 - date.getMinutes()* 60 * 1000 - date.getSeconds() * 1000;
        return a;
    }

    public static String formatTimeToString(long time, String type)
    {
        return DateFormat.format(type, time).toString();
    }

    public static long formatTimeToLong(String time, String type)
    {
        long day = 0;
        SimpleDateFormat myFormatter = new SimpleDateFormat(type);
        try
        {
            Date startDate = myFormatter.parse(time);
            day = startDate.getTime();
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return day;
    }


}
