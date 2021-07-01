package org.techtown.quizalram;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.quizalram.db.AlarmDatabase;
import org.techtown.quizalram.main.MainActivity;
import org.techtown.quizalram.main.Main_Quiz_Title;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Open_Alarm extends AppCompatActivity {

    TextView hour, minute, label;
    static TextView question;
    EditText edit;

    static String answer;
    MediaPlayer alarm_play = new MediaPlayer();
    AudioManager audio;
    int audio_mode;

    Vibrator vibrator;
    AudioAttributes audioAttributes;

    Uri alarm_uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_open);

        hour = findViewById(R.id.open_alarm_hour);
        minute = findViewById(R.id.open_alarm_minute);
        label = findViewById(R.id.open_alarm_label);
        question = findViewById(R.id.open_alarm_question);
        edit = findViewById(R.id.open_alarm_edit);

        if (getIntent() != null){
            label.setText(getIntent().getStringExtra("label"));
        }
        if (getIntent().getStringExtra("vibrator") != null){
            if (getIntent().getStringExtra("vibrator").equals("on")){

                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

                vibrator.vibrate(pattern, 0);
            }
        }

        setQuiz2();
        loadQuizData();

//        핸드폰 설정(벨소리, 진동, 무음)을 확인하고 벨소리로 바꾸기 -> RINGER_MODE_NORMAL : 벨소리 / VIBRATE : 진동 / SILENT : 무음
//        audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
//        audio_mode = audio.getRingerMode();
//        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audio_mode = audio.getRingerMode();

        if (getIntent().getStringExtra("ring") != null){
            alarm_uri = Uri.parse(getIntent().getStringExtra("ring"));
            try {
                alarm_play.setDataSource(getApplicationContext(), alarm_uri);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    //USAGE_ALARM으로 설정하면 알람 소리 조절 불가능, RING으로 하면 벨소리 = 알람소리

                    if (audio_mode == AudioManager.RINGER_MODE_NORMAL){
                        audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();
                    } else {
                        audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();
                    }

                    alarm_play.setAudioAttributes(audioAttributes);

                } else {
                    alarm_play.setAudioStreamType(AudioManager.STREAM_RING);
                }

                alarm_play.setLooping(true);
                alarm_play.prepare();
                alarm_play.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Date date = new Date(System.currentTimeMillis());

        hour.setText(new SimpleDateFormat("HH").format(date.getTime()));
        minute.setText(new SimpleDateFormat("mm").format(date.getTime()));

        //액티비티 열릴 때 키보드 안보이게 설정
        InputMethodManager input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(edit.getWindowToken(), 0);

        //화면이 꺼져있어도 액티비티가 열리게 설정
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        TextView closeButton = findViewById(R.id.open_alarm_button1);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().toString().equals(answer)){

                    if (getIntent().getIntExtra("alarm_num", -1) != -1){
                        modifyAlarmSetTurn(getIntent().getIntExtra("alarm_num", -1));
                    }

                    if (alarm_uri != null){
                        alarm_play.stop();
                    }
                    audio.setRingerMode(audio_mode);
                    if (vibrator != null){
                        vibrator.cancel();
                    }
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    edit.setText("");
                }
            }
        });
    }

    public void loadQuizData(){
        AlarmDatabase.println("loadAlarmListData called.");

        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

        String sql = "select _id, QUESTION, ANSWER from " + AlarmDatabase.TABLE_QUIZ + " order by _id asc"; //내림차순 desc

        int recordCount = -1;
        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);


        if (database != null){
            Cursor outCursor = database.rawQuery(sql);

            recordCount = outCursor.getCount();
            AlarmDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String question = outCursor.getString(1);
                String answer = outCursor.getString(2);

                questions.add(question);
                answers.add(answer);
            }

            outCursor.close();
        }

        Random rnd = new Random();

        int num = rnd.nextInt(questions.size());

        Open_Alarm.question.setText(questions.get(num));
        Open_Alarm.answer = answers.get(num);
    }

    @Override
    public void onBackPressed() {

    }

    public void setQuiz2(){

        String sql = "select _id, TITLE, QUESTION, ANSWER, VALUE from " + AlarmDatabase.TABLE_QUIZ + " order by _id desc";

        int recordCount = -1;
        AlarmDatabase database = AlarmDatabase.getInstance(getApplicationContext());


        Cursor outCursor = database.rawQuery(sql);

        if (outCursor.getCount() == 0){

            String[] questions = new String[]{"2+2 = ", "4+4 = ", "38-19-12 = ", "96-27-69 = ", "70-23+39 = ", "32-17+99 = ", "56+29 = ",
                    "5×8+7 = ", "9×5+13 = ", "6+8-5 = ", "17+57-29 = ", "1+6 = ", "3+8 = ", "2+0 = ", "112+119 = ", "12×8 = ", "12×8-6 = ",
                    "9×22 = ", "11+27 = ", "20×9+22 = ", "1+9+9+7 = ", "9×15 = ", "9+15 = ", "19+97 = ", "24×10-9 = ", "3×6×4 = ", "52-16-36 = ",
                    "44÷11-4 = ", "369÷9 = ", "10÷2×5 = "};
            String[] answers = new String[]{"4", "8", "7", "0", "86", "114", "85", "47", "58", "9", "45", "7", "11", "2", "231", "96", "90",
                    "198", "38", "202", "26", "135", "24", "116", "231", "72", "0", "0", "41", "25"};

            for (int i = 0; i < questions.length; i++){
                String sql2 = "insert into " + AlarmDatabase.TABLE_QUIZ +
                        "(TITLE, QUESTION, ANSWER, VALUE) values("
                        + "'" + "수학" + "', " + "'" + questions[i] + "', " + "'" + answers[i] + "', " + "'" + "true" + "')";

                AlarmDatabase.println("saveQuestion : " + sql2);

                database.execSQL(sql2);
            }
        }
    }

    public void modifyAlarmSetTurn(int id){

        String sql = "update " + AlarmDatabase.TABLE_ALARM + " set "
                + " TURN = '" + "false" + "'"
                + " where " + " _id = " + id;

        Log.d("1818", "알람번호" + id);

        Log.d("1818", sql);

        AlarmDatabase database = AlarmDatabase.getInstance(getApplicationContext());
        database.rawQuery(sql);
    }
}
