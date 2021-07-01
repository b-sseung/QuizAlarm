package org.techtown.quizalram;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.techtown.quizalram.main.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmBase {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void alarmtlqkf(int alarm_num, Calendar calendar, String label, boolean on, String day, String ring, String vibrator){

        Intent main_intent = new Intent(MainActivity.context, Alarm_Reciver.class);

        PendingIntent pIntent;

        AlarmManager alarm_manager = MainActivity.alarm_manager;

        ArrayList<String> days = new ArrayList<String>();
        Log.d("1818", "alarmtlqkf 켜짐");

        if (!day.equals("")){
            days = returnRepeateDay(day);
        }


        main_intent.putExtra("label", label);
        main_intent.putExtra("ring", ring);
        main_intent.putExtra("vibrator", vibrator);

        Calendar calendar1 = calendar;
        if (on) {
            if (days.size() == 0) {
                main_intent.putExtra("alarm_num", alarm_num);
                if (System.currentTimeMillis() > calendar1.getTimeInMillis()){
                    calendar1.add(Calendar.DATE, 1);
                    Log.d("1818", "하루 추가됨");
                }
                main_intent.putExtra("뭐가 울리는지", "왜 안떠 : " + alarm_num);
                pIntent = PendingIntent.getBroadcast(MainActivity.context, alarm_num, main_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pIntent);

                Log.d("1818", "설정 1");

            } else {

                long intervalDay = 24 * 60 * 60 * 1000;// 24시간

                long selectTime=calendar.getTimeInMillis();
                long currenTime=System.currentTimeMillis();

                //만약 설정한 시간이, 현재 시간보다 작다면 알람이 부정확하게 울리기 때문에 다음날 울리게 설정
                if(currenTime>selectTime){
                    selectTime += intervalDay;
                }

                boolean[] week = checkWeek(days);

                main_intent.putExtra("weekday", week);
                main_intent.putExtra("뭐가 울리는지", "왜 안떠 : " + alarm_num);
                pIntent = PendingIntent.getBroadcast(MainActivity.context, alarm_num, main_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // 지정한 시간에 매일 알림
                alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime,  intervalDay, pIntent);

                Log.d("1818", "설정 2");

            }
        } else {
            pIntent = PendingIntent.getBroadcast(MainActivity.context, alarm_num, main_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm_manager.cancel(pIntent);

            Log.d("1818", "설정 3");

        }
    }

    public static ArrayList<String> returnRepeateDay(String day){
        ArrayList<String> days = new ArrayList<String>();

        if (day.equals("월 ~ 금")){
            days.add("월");
            days.add("화");
            days.add("수");
            days.add("목");
            days.add("금");
        } else if (day.equals("월 ~ 토")){
            days.add("월");
            days.add("화");
            days.add("수");
            days.add("목");
            days.add("금");
            days.add("토");
        } else if (day.equals("매일")){
            days.add("월");
            days.add("화");
            days.add("수");
            days.add("목");
            days.add("금");
            days.add("토");
            days.add("일");
        } else if (day.equals("주말만")){
            days.add("토");
            days.add("일");
        } else {
            String[] days1 = day.replaceAll(" ", "").split("");

            for (int i = 0; i < days1.length; i++){
                days.add(days1[i]);
            }
        }

        return days;
    }
    public static int setalarm(String day){
        Log.d("1818", "day : " + day);
        int num = 0;
        switch(day){
            case "월" :
                num = 2;
                break;
            case "화" :
                num = 3;
                break;
            case "수" :
                num = 4;
                break;
            case "목" :
                num = 5;
                break;
            case "금" :
                num = 6;
                break;
            case "토" :
                num = 7;
                break;
            case "일" :
                num = 1;
                break;
        }
        Log.d("1818", "daynum : " + num);

        return num;
    }

    public static boolean[] checkWeek(ArrayList<String> days){

        boolean mon = false, tue = false, wed = false, thud = false, fri = false, satur = false, sun = false;

        for (int i = 0; i < days.size(); i++){

            switch (days.get(i)){
                case "월" :
                    mon = true;
                    break;
                case "화" :
                    tue = true;
                    break;
                case "수" :
                    wed = true;
                    break;
                case "목" :
                    thud = true;
                    break;
                case "금" :
                    fri = true;
                    break;
                case "토" :
                    satur = true;
                    break;
                case "일" :
                    sun = true;
                    break;
            }

        }

        boolean[] week = {false, sun, mon, tue, wed, thud, fri, satur};

        return week;
    }

}
