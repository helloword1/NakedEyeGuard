package com.goockr.nakedeyeguard.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JJT-ssd on 2016/11/22.
 */

public class DateExtension {


    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date要转换的date类型的时间
    private static long dateToLong(Date date) {
        return date.getTime();
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    private static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
//    /**
//     生成当前时间对应的字符串
//     　  - returns: 对应时间格式
//     */
    public static String descriptionDate(long dateTime){
        Date approvalTime= new Date(dateTime);
        long test =  approvalTime.getTime();
        //１.创建时间格式化对象let formatter = NSDateFormatter()
        //如果不指定以下代码真机中肯能无法转换,设置时区
        //创建一个日历类
        Calendar calendar = Calendar.getInstance();
        int today = calendar.getTime().getDate();
        Date calendarDate =  calendar.getTime();
        int year = calendar.getTime().getYear();
        //定义变量记录时间格式
        String formatterStr = "HH:mm";
        if ( today==approvalTime.getDate() ) {
            //今天
            long seconds =approvalTime.getHours()*3600 + approvalTime.getMinutes()*60 +approvalTime.getSeconds();
            long newTime =calendarDate.getHours()*3600+calendarDate.getMinutes()*60+calendarDate.getSeconds();
            String minStr = String.valueOf((newTime-seconds)/60);
            String hourStr = String.valueOf((newTime-seconds)/3600);
            if ((newTime-seconds)<60 ){
                return "刚刚";
            }else if((newTime-seconds)<(60*60)){
                return  minStr+"分钟前";
            }else {
                return  hourStr+"小时前";
            }
        }else if (today==(approvalTime.getDate()+1)){
            formatterStr = "昨天 "+formatterStr;
        }else if (today==(approvalTime.getDate()+2)){
            formatterStr = "前天 "+formatterStr;
        } else{
            formatterStr = "yy/MM/dd";
            //该方法可以获取两个时间之间的差值  NSCalendarUnit.Year年的差值

//            if (year>(approvalTime.getYear()+1)){
//                //更早时间
//                formatterStr = "yyyy/MM/dd";//+formatterStr;
//            }else{
                //一年以内
  //              formatterStr = "yy/MM/dd";//+formatterStr;
//            }
        }
        SimpleDateFormat formatter=new SimpleDateFormat(formatterStr, Locale.getDefault());
        return   formatter.format(approvalTime);

    }

    public static String dateLong2String(long dateTime){
        Date longTime= new Date(dateTime);
        //定义变量记录时间格式
        String formatterStr = "mm:ss";
        SimpleDateFormat formatter=new SimpleDateFormat(formatterStr, Locale.getDefault());
        return   formatter.format(longTime);

    }
}

