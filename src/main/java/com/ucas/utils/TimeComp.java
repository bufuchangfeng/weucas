package com.ucas.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Compare input time with current time (Accurate to minute)
 *
 *  If input-time > current-time, then return diff > 0.
 *  If input-time < current-time, then return diff < 0.
 *  If input-time = current-time, then return diff = 0.
 */


public class TimeComp {
//    public static void main(String[] args){
//        String dateStr = "2019-11-14 13:21";
//        GetTime(dateStr);
//    }

    public static Integer GetTime(String dateStr){

        Date nowDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        System.out.println(nowDate.getTime());

        try {
            Date getDate = dateFormat.parse(dateStr);
            Integer diff = (int) (getDate.getTime() - nowDate.getTime())/(1000*60); // 比较分钟
//            System.out.println(diff);
            return diff;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

