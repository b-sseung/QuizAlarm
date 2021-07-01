package org.techtown.quizalram;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.techtown.quizalram.main.MainActivity;

import java.io.IOException;

import static android.app.Service.START_NOT_STICKY;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class RingtonePlayingService extends Service {

    Intent open_intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
    }

    private void startMyOwnForeground(){
        //Oreo 버전 부터는 Notification에 채널 ID를 지정해줘야 합니다. 그리고 Notification을 등록하기 위해서는 아이콘, 제목, 내용이 전부 있어야 가능합니다.

        String NOTIFICATION_CHANNEL_ID = "create_service";
        String channelName = "alarm_service";
        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);  //채널ID, 사용자가 볼 이름, 중요도
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null; //assert는 강제로 앱을 종료시켜 버그를 잡아내는 방법 -> manager가 null이면 강제 종료
            manager.createNotificationChannel(chan); //알림채널을 notificationmanager에 전달하여 등록
        }

        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        open_intent = new Intent(getApplicationContext(), Open_Alarm.class);
        open_intent.putExtra("label", intent.getStringExtra("label"));
        open_intent.putExtra("ring", intent.getStringExtra("ring"));
        open_intent.putExtra("vibrator", intent.getStringExtra("vibrator"));

        if (intent.getIntExtra("alarm_num", -1) != -1){
            open_intent.putExtra("alarm_num", intent.getIntExtra("alarm_num", -1));
        }

        startActivity(open_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


        Toast.makeText(getApplicationContext(), "알람시작", Toast.LENGTH_SHORT).show();

        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("1818", "서비스 종료");

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(101);
    }
}