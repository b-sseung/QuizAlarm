package org.techtown.quizalram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.techtown.quizalram.main.MainActivity;

import java.util.Calendar;

public class Alarm_Reciver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        this.context = context;

        Intent service_intent = new Intent(context, RingtonePlayingService.class);


        if (intent.getBooleanArrayExtra("weekday") != null){
            boolean[] week = intent.getBooleanArrayExtra("weekday");

            Calendar cal = Calendar.getInstance();
            Log.d("1818","요일 : " + cal.get(Calendar.DAY_OF_WEEK)+"");

            if (!week[cal.get(Calendar.DAY_OF_WEEK)]) return; // 체크한 요일이 아니면
        } else {
            final int alarm_num = intent.getIntExtra("alarm_num", -1);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Intent main_intent = new Intent(context, MainActivity.class);
                    main_intent.putExtra("alarm_num", alarm_num);
                    context.startActivity(main_intent);
                }
            });
            service_intent.putExtra("alarm_num", alarm_num);
        }

        service_intent.putExtra("뭐가 울리는지", intent.getStringExtra("뭐가 울리는지"));
        service_intent.putExtra("label", intent.getStringExtra("label"));
        service_intent.putExtra("ring", intent.getStringExtra("ring"));
        service_intent.putExtra("vibrator", intent.getStringExtra("vibrator"));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(service_intent);
        }else{
            this.context.startService(service_intent);
        }
    }
}